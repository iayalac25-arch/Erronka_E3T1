package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import eredua.Eskaerak;

/**
 * Bezeroen eskaerak datu-basean kudeatzen dituen klasea.
 */
public class DBEskaerak extends DB {

	/**
	 * Bezero baten eskaera guztiak lortzen ditu, barneko produktuen eta prezioen informazioarekin.
	 * @param bezeroId Bezeroaren identifikatzailea.
	 * @return Bezero horren eskaera guztien zerrenda.
	 */
	public ArrayList<Eskaerak> lortuEskaerakBezeroarentzat(int bezeroId) {
		ArrayList<Eskaerak> eskaerakLista = new ArrayList<>();
		String sqlEskaerak = "SELECT ID, ESKAERA_DATA FROM ESKARI WHERE ID_BEZERO = ? ORDER BY ESKAERA_DATA DESC";

		try (Connection conn = konexioa(); PreparedStatement pstmtEskaerak = conn.prepareStatement(sqlEskaerak)) {

			pstmtEskaerak.setInt(1, bezeroId);
			ResultSet rsEskaerak = pstmtEskaerak.executeQuery();

			while (rsEskaerak.next()) {
				int eskariId = rsEskaerak.getInt("ID");
				Date data = rsEskaerak.getDate("ESKAERA_DATA");
				ArrayList<String> produktuak = new ArrayList<>();
				double guztira = 0;

				String sqlLerroak = "SELECT P.IZENA, EL.KOPURUA, EL.SALNEURRIA " + "FROM ESKARI_LERRO EL "
						+ "JOIN PRODUKTU P ON EL.ID_PRODUKTU = P.ID " + "WHERE EL.ID_ESKARI = ?";

				try (PreparedStatement pstmtLerroak = conn.prepareStatement(sqlLerroak)) {
					pstmtLerroak.setInt(1, eskariId);
					ResultSet rsLerroak = pstmtLerroak.executeQuery();

					while (rsLerroak.next()) {
						String produktuIzena = rsLerroak.getString("IZENA");
						int kopurua = rsLerroak.getInt("KOPURUA");
						double salneurria = rsLerroak.getDouble("SALNEURRIA");

						produktuak.add(produktuIzena + " (x" + kopurua + ")");
						guztira += kopurua * salneurria;
					}
				}

				eskaerakLista.add(new Eskaerak(eskariId, data, produktuak, guztira));
			}

		} catch (SQLException e) {
			System.out.println("Errorea eskaerak lortzean: " + e.getMessage());
		}

		return eskaerakLista;
	}

	/**
	 * Saskian dauden produktuen eskaera berri bat datu-basean gordetzen du.
	 * Transakzio baten bidez (AutoCommit(false)) eskaera eta eskaera-lerroak sartzen ditu.
	 * @param bezeroId Eskaera egiten duen bezeroaren ID-a.
	 * @param saskia Erosi nahi diren produktuen zerrenda.
	 * @return Eskaera ondo gorde bada true, bestela false.
	 */
	public boolean sartuEskaria(int bezeroId, ArrayList<eredua.Produktua> saskia) {
		if (saskia.isEmpty())
			return false;

		boolean ondo = false;
		Connection conn = null;
		try {
			conn = konexioa();
			conn.setAutoCommit(false);
			int hurrengoId = 1;
			String sqlMax = "SELECT NVL(MAX(ID), 0) FROM ESKARI";
			try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlMax)) {
				if (rs.next())
					hurrengoId = rs.getInt(1) + 1;
			}

			String sqlEskari = "INSERT INTO ESKARI (ID, ID_BEZERO, ESKAERA_DATA) VALUES (?, ?, SYSDATE)";
			try (PreparedStatement psEskari = conn.prepareStatement(sqlEskari)) {
				psEskari.setInt(1, hurrengoId);
				psEskari.setInt(2, bezeroId);
				psEskari.executeUpdate();
			}

			int hurrengoIdLerro = 1;
			String sqlMaxLerro = "SELECT NVL(MAX(ID_LERRO), 0) FROM ESKARI_LERRO";
			try (Statement stLerro = conn.createStatement(); ResultSet rsLerro = stLerro.executeQuery(sqlMaxLerro)) {
				if (rsLerro.next())
					hurrengoIdLerro = rsLerro.getInt(1) + 1;
			}

			String sqlLerro = "INSERT INTO ESKARI_LERRO (ID_LERRO, ID_ESKARI, ID_PRODUKTU, KOPURUA, SALNEURRIA) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement psLerro = conn.prepareStatement(sqlLerro)) {
				for (eredua.Produktua p : saskia) {
					psLerro.setInt(1, hurrengoIdLerro);
					psLerro.setInt(2, hurrengoId);
					psLerro.setInt(3, p.getId());
					psLerro.setInt(4, 1);

					double prezioa = Double.parseDouble(p.getSalneurria().replace(",", "."));
					psLerro.setDouble(5, prezioa);
					psLerro.executeUpdate();
					hurrengoIdLerro++;
				}
			}

			conn.commit();
			ondo = true;
		} catch (Exception e) {
			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			System.out.println("Errorea eskaera gordetzean: " + e.getMessage());
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
		return ondo;
	}
	
	/**
	 * Datu-baseko BEZERO_GASTUA_TOTALA funtzioari deitzen dio bezero baten gastu historikoa kalkulatzeko.
	 * @param bezeroId Kalkulatu nahi den bezeroaren ID-a.
	 * @return Bezero horrek dendan gastatu duen diru kopuru totala.
	 */
	public double kalkulatuBezeroarenGastua(int bezeroId) {
		double gastuTotala = 0.0;
		// Funtzio bati deitzeko sintaxia: { ? = call FUNTZIO_IZENA(?) }
		String sql = "{ ? = call BEZERO_GASTUA_TOTALA(?) }";

		try (java.sql.Connection conn = konexioa(); java.sql.CallableStatement cs = conn.prepareCall(sql)) {

			// 1. Irteerako parametroa erregistratu (funtzioak itzuliko duen emaitza)
			cs.registerOutParameter(1, java.sql.Types.DOUBLE);
			
			// 2. Sarrerako parametroa ezarri (bezeroaren ID-a)
			cs.setInt(2, bezeroId);

			// 3. Funtzioa exekutatu
			cs.execute();

			// 4. Emaitza jaso
			gastuTotala = cs.getDouble(1);

		} catch (java.sql.SQLException e) {
			System.out.println("SQL Errorea bezeroaren gastua kalkulatzean: " + e.getMessage());
		}
		
		return gastuTotala;
	}

}
