package db;

import java.util.ArrayList;
import eredua.Eskaria;
import java.sql.*;
import eredua.EskaeraList;

/**
 * Saltzaileentzat eskaeren eta estatistiken informazioa lortzen duen klasea.
 */
public class DBSaltzaileEskaerak extends DB {

	/**
	 * Sistemako eskaera guztiak lortzen ditu bezeroen izenekin eta egoerekin elkartuta.
	 * * @return Eskaria objektuen zerrenda bat.
	 */
	public ArrayList<Eskaria> eskariGuztiak() {
		ArrayList<Eskaria> eskariak = new ArrayList<>();

		String sql = "SELECT e.ID, " + "NVL(b.IZENA || ' ' || b.ABIZENA, 'Bezero Ezezaguna') AS BEZEROA, "
				+ "e.ESKAERA_DATA, " + "NVL(eg.DESKRIBAPENA, 'Ezezaguna') AS EGOERA_TEXTUA " + "FROM ESKARI e "
				+ "LEFT JOIN BEZERO b ON e.ID_BEZERO = b.ID " + "LEFT JOIN ESKARI_EGOERA eg ON e.ID_EGOERA = eg.ID "
				+ "ORDER BY e.ESKAERA_DATA DESC";

		try (Connection conn = konexioa(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				Eskaria e = new Eskaria(rs.getInt("ID"), rs.getString("BEZEROA"), rs.getString("ESKAERA_DATA"),
						rs.getString("EGOERA_TEXTUA"));
				eskariak.add(e);
			}
		} catch (SQLException e) {
			System.out.println("Errorea eskaerak lortzean: " + e.getMessage());
		}
		return eskariak;
	}

	/**
	 * Eskaera jakin batek dituen produktu guztiak (lerroak) lortzen ditu.
	 * * @param kodea Bilatu nahi den eskaeraren identifikatzailea.
	 * @return Eskaera horretako produktuen zerrenda.
	 */
	public ArrayList<EskaeraList> eskatutakoProduktuak(int kodea) {
		ArrayList<EskaeraList> eskProduktu = new ArrayList<>();

		String sql = "SELECT p.izena, el.kopurua, el.salneurria " + "FROM eskari e "
				+ "JOIN ESKARI_LERRO el ON e.id = el.id_eskari " + "JOIN produktu p ON el.id_produktu = p.id "
				+ "WHERE e.id = ?";

		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, kodea);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				EskaeraList el = new EskaeraList(rs.getString(1), rs.getInt(2), rs.getInt(3));
				eskProduktu.add(el);
			}

		} catch (SQLException e) {
			System.out.println("Errorea: " + e.getMessage());
		}
		return eskProduktu;
	}

	/**
	 * TOP5 prozedura deitzen du saltzaile onenak edo produktu onenak/txarrenak lortzeko.
	 * * @param eginkizuna Bilatu nahi den estatistika ("SaltzaileTop5", "ProduktuTop5", edo "TxarProduktuTop5").
	 * @return Estatistikaren emaitzak testu (String) formatuan.
	 */
	public ArrayList<String> lortuTop5(String eginkizuna) {
		ArrayList<String> emaitzak = new ArrayList<>();
		String sql = "{call TOP5(?, ?)}";

		try (Connection conn = konexioa(); CallableStatement cs = conn.prepareCall(sql)) {
			cs.setString(1, eginkizuna);

			cs.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);

			cs.execute();

			try (ResultSet rs = (ResultSet) cs.getObject(2)) {
				while (rs.next()) {
					String testua = rs.getString(1);
					if (eginkizuna.equals("SaltzaileTop5")) {
						testua += " " + rs.getString(2);
					}
					emaitzak.add(testua);
				}
			}
		} catch (SQLException e) {
			System.out.println("Errorea TOP5 prozeduran (" + eginkizuna + "): " + e.getMessage());
		}
		return emaitzak;
	}
}
