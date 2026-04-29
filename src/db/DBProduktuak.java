package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import eredua.Kategoria;
import eredua.Produktua;

/**
 * Produktuen eta kategorien datu-baseko eragiketak kudeatzen dituen klasea (CRUD eta erosketak).
 */
public class DBProduktuak extends DB {

	/**
	 * Datu-baseko kategoria guztiak lortzen ditu (adibidez, kontsoletako jokoak).
	 * @return Kategoria objektuen zerrenda bat.
	 */
	public ArrayList<Kategoria> getKategoriak() {
		ArrayList<Kategoria> lista = new ArrayList<Kategoria>();
		String sql = "SELECT ID, IZENA FROM KATEGORIA ORDER BY ID";

		try (Connection conn = konexioa(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				Kategoria k = new Kategoria(rs.getInt("ID"), rs.getString("IZENA"));
				lista.add(k);
			}
		} catch (Exception e) {
			System.out.println("Errorea kategoriak irakurtzean: " + e.getMessage());
		}
		return lista;
	}

	/**
	 * Datu-baseko produktu guztiak eta haien stock-a inbentariotik lortzen ditu.
	 * @return Produktu objektuen zerrenda bat.
	 */
	public ArrayList<Produktua> getProduktuak() {
		ArrayList<Produktua> alProduktua = new ArrayList<>();
		String sql = "SELECT P.ID, P.IZENA, P.SALNEURRIA, P.ID_KATEGORIA, NVL(SUM(I.KOPURUA), 0) AS STOCK_TOTAL "
				+ "FROM PRODUKTU P " + "LEFT JOIN INBENTARIO I ON P.ID = I.ID_PRODUKTU "
				+ "GROUP BY P.ID, P.IZENA, P.SALNEURRIA, P.ID_KATEGORIA " + "ORDER BY P.ID";
		try (Connection conn = konexioa(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				alProduktua.add(new Produktua(rs.getInt("ID"), rs.getString("IZENA"), rs.getString("SALNEURRIA"),
						rs.getInt("ID_KATEGORIA"), rs.getInt("STOCK_TOTAL"), 8));
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		return alProduktua;
	}

	/**
	 * Produktu baten salneurria eta stock-a eguneratzen ditu.
	 * Prezio negatiboak blokeatzen dituen Trigger-a du.
	 * @param p Eguneratu nahi den produktua.
	 * @return Eguneraketa ondo joan bada true, bestela false.
	 */
	public boolean eguneratuProduktua(Produktua p) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sql1 = "UPDATE PRODUKTU SET SALNEURRIA = ? WHERE ID = ?";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			double salneurriaZuzena = Double.parseDouble(p.getSalneurria().replace(",", "."));
			ps1.setDouble(1, salneurriaZuzena);
			ps1.setInt(2, p.getId());
			ps1.executeUpdate();
			ps1.close();

			int unekoStockTotala = 0;
			String sqlStock = "SELECT NVL(SUM(KOPURUA), 0) FROM INBENTARIO WHERE ID_PRODUKTU = ?";
			PreparedStatement psStock = conn.prepareStatement(sqlStock);
			psStock.setInt(1, p.getId());
			ResultSet rsStock = psStock.executeQuery();
			if (rsStock.next()) {
				unekoStockTotala = rsStock.getInt(1);
			}
			rsStock.close();
			psStock.close();

			int diferentzia = p.getStock() - unekoStockTotala;

			if (diferentzia > 0) {
				String sqlFind = "SELECT ID_BILTEGI FROM INBENTARIO WHERE ID_PRODUKTU = ? AND ROWNUM = 1";
				PreparedStatement psFind = conn.prepareStatement(sqlFind);
				psFind.setInt(1, p.getId());
				ResultSet rsFind = psFind.executeQuery();

				if (rsFind.next()) {
					int biltegiAukeratua = rsFind.getInt(1);
					String sqlUpdate = "UPDATE INBENTARIO SET KOPURUA = KOPURUA + ? WHERE ID_PRODUKTU = ? AND ID_BILTEGI = ?";
					PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
					psUpdate.setInt(1, diferentzia);
					psUpdate.setInt(2, p.getId());
					psUpdate.setInt(3, biltegiAukeratua);
					psUpdate.executeUpdate();
					psUpdate.close();
				} else {
					String sqlInsert = "INSERT INTO INBENTARIO (ID_PRODUKTU, ID_BILTEGI, KOPURUA) VALUES (?, 8, ?)";
					PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
					psInsert.setInt(1, p.getId());
					psInsert.setInt(2, diferentzia);
					psInsert.executeUpdate();
					psInsert.close();
				}
				rsFind.close();
				psFind.close();

			} else if (diferentzia < 0) {
				int kenduBeharra = Math.abs(diferentzia);
				String sqlList = "SELECT ID_BILTEGI, KOPURUA FROM INBENTARIO WHERE ID_PRODUKTU = ? AND KOPURUA > 0 ORDER BY KOPURUA DESC";
				PreparedStatement psList = conn.prepareStatement(sqlList);
				psList.setInt(1, p.getId());
				ResultSet rsList = psList.executeQuery();

				while (rsList.next() && kenduBeharra > 0) {
					int idBiltegi = rsList.getInt("ID_BILTEGI");
					int biltegiStock = rsList.getInt("KOPURUA");
					int kendukoDena = Math.min(kenduBeharra, biltegiStock);

					String sqlSub = "UPDATE INBENTARIO SET KOPURUA = KOPURUA - ? WHERE ID_PRODUKTU = ? AND ID_BILTEGI = ?";
					PreparedStatement psSub = conn.prepareStatement(sqlSub);
					psSub.setInt(1, kendukoDena);
					psSub.setInt(2, p.getId());
					psSub.setInt(3, idBiltegi);
					psSub.executeUpdate();
					psSub.close();
					kenduBeharra -= kendukoDena;
				}
				rsList.close();
				psList.close();
			}

			conn.close();
			ondo = true;
		} catch (SQLException e) {
			String mezua = e.getMessage().split("\n")[0];
			JOptionPane.showMessageDialog(null, mezua, "Prezio Errorea", JOptionPane.ERROR_MESSAGE);
			System.out.println("ERROREA Eguneratzean: " + e);
		}
		return ondo;
	}

	/**
	 * Kategoria bateko produktu guztiei deskontua aplikatzen dien prozedura deitzen du.
	 * @param idKategoria Deskontua jasoko duen kategoriaren ID-a.
	 * @param deskontua Aplikatu nahi den deskontu ehunekoa (0-50).
	 * @return true ondo aplikatu bada, false erroren bat egon bada.
	 */
	public boolean aplikatuDeskontuaKategoriari(int idKategoria, int deskontua) {
		boolean ondo = false;
		String sql = "{call KATEGORIA_DESKONTUA(?, ?)}";

		try (Connection conn = konexioa(); java.sql.CallableStatement cs = conn.prepareCall(sql)) {

			// Parametroak ezarri
			cs.setInt(1, idKategoria);
			cs.setInt(2, deskontua);

			// Prozedura exekutatu
			cs.execute();
			ondo = true;

		} catch (SQLException e) {
			// Oracle-k botatako errorea (RAISE_APPLICATION_ERROR) harrapatu eta garbitu
			String mezuGarbia = e.getMessage().split("\n")[0];
			JOptionPane.showMessageDialog(null, mezuGarbia, "Deskontu Errorea", JOptionPane.ERROR_MESSAGE);
			System.out.println("SQL Errorea deskontuan: " + e.getMessage());
		}
		return ondo;
	}
	
	/**
	 * Produktu bat datu-basetik ezabatzen du bere ID-a erabiliz.
	 * @param id Ezabatu nahi den produktuaren identifikatzailea.
	 * @return Ezabaketa ondo joan bada true, bestela false.
	 */
	public boolean ezabatuProduktua(int id) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sql1 = "DELETE FROM INBENTARIO WHERE ID_PRODUKTU = ?";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setInt(1, id);
			ps1.executeUpdate();
			ps1.close();

			String sql2 = "DELETE FROM PRODUKTU WHERE ID = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, id);
			ps2.executeUpdate();
			ps2.close();

			conn.close();
			ondo = true;
		} catch (SQLException e) {
			System.out.println("ERROREA Ezabatzean: " + e);
		}
		return ondo;

	}

	/**
	 * Produktu berri bat datu-basean eta inbentarioan txertatzen du.
	 * @param p Datu-basean gordeko den produktu berria.
	 * @return Txertaketa ondo joan bada true, bestela false.
	 */
	public boolean txertatuProduktua(Produktua p) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sql1 = "INSERT INTO PRODUKTU (ID, IZENA, DESKRIBAPENA, BALIOA, SALNEURRIA, ID_KATEGORIA) VALUES (?, ?, ' ', ?, ?, ?)";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setInt(1, p.getId());
			ps1.setString(2, p.getIzena());

			double salneurriaZuzena = Double.parseDouble(p.getSalneurria().replace(",", "."));
			ps1.setDouble(3, salneurriaZuzena * 0.7);
			ps1.setDouble(4, salneurriaZuzena);
			ps1.setInt(5, p.getIdKategoria());
			ps1.executeUpdate();
			ps1.close();

			String sql2 = "INSERT INTO INBENTARIO (ID_PRODUKTU, ID_BILTEGI, KOPURUA) VALUES (?, ?, ?)";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, p.getId());
			ps2.setInt(2, p.getidBiltegi());
			ps2.setInt(3, p.getStock());
			ps2.executeUpdate();
			ps2.close();

			conn.close();
			ondo = true;
		} catch (SQLException e) {
			String mezua = e.getMessage().split("\n")[0];
			JOptionPane.showMessageDialog(null, mezua, "Prezio Errorea", JOptionPane.ERROR_MESSAGE);
			System.out.println("ERROREA Gehitzean: " + e);
		}
		return ondo;
	}

	/**
	 * Produktu berri batentzat erabilgarri dagoen hurrengo ID-a kalkulatzen du.
	 * @return Produktu berriaren ID zenbakia.
	 */
	public int hurrengoId() {
		int id = 1;
		try {
			Connection conn = konexioa();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM PRODUKTU");
			if (rs.next() && rs.getObject(1) != null) {
				id = rs.getInt(1) + 1;
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Errorea hurrengo IDa lortzen: " + e);
		}
		return id;
	}

	/**
	 * Datu-basean produktu baten erosketa erregistratzen du inbentarioko stock-a murriztuz.
	 * @param idProd Erosi nahi den produktuaren identifikatzailea.
	 * @return Erosketa ondo burutu bada true, bestela false.
	 */
	public boolean erosiProduktua(int idProd) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sqlFind = "SELECT ID_BILTEGI FROM INBENTARIO WHERE ID_PRODUKTU = ? AND KOPURUA > 0 FETCH FIRST 1 ROWS ONLY";
			PreparedStatement psFind = conn.prepareStatement(sqlFind);
			psFind.setInt(1, idProd);
			ResultSet rsFind = psFind.executeQuery();

			if (rsFind.next()) {
				int biltegiStockDuna = rsFind.getInt("ID_BILTEGI");

				String sqlUpdate = "UPDATE INBENTARIO SET KOPURUA = KOPURUA - 1 WHERE ID_PRODUKTU = ? AND ID_BILTEGI = ?";
				PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
				psUpdate.setInt(1, idProd);
				psUpdate.setInt(2, biltegiStockDuna);

				int aldatuta = psUpdate.executeUpdate();
				psUpdate.close();

				if (aldatuta > 0) {
					ondo = true;
				}
			}

			rsFind.close();
			psFind.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Errorea erostean: " + e);
		}
		return ondo;
	}

}