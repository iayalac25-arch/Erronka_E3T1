package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import eredua.Langilea;

/**
 * Langileen eta saltzaileen datu-baseko eragiketak kudeatzen dituen klasea (CRUD eta pasahitzak).
 */
public class DBLangileak extends DB {

	/**
	 * Sisteman erregistratuta dauden langile guztien zerrenda lortzen du datu-basetik.
	 * * @return Langile objektuen zerrenda.
	 */
	public ArrayList<Langilea> getLangileak() {
		ArrayList<Langilea> lista = new ArrayList<Langilea>();

		String sql = "SELECT L.ID, L.IZENA, L.ABIZENA, L.EMAILA, L.TELEFONOA, "
				+ "TO_CHAR(L.KONTRATAZIO_DATA, 'YYYY-MM-DD') AS KONTRATAZIO_DATA, "
				+ "NVL(N.IZENA, '---') AS NAGUSI_IZENA " + "FROM LANGILE L " + "INNER JOIN SALTZAILE S ON L.ID = S.ID "
				+ "LEFT JOIN LANGILE N ON L.ID_NAGUSI = N.ID " + "ORDER BY L.ID";

		try {
			Connection conn = konexioa();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Langilea l = new Langilea(rs.getInt("ID"), rs.getString("IZENA"), rs.getString("ABIZENA"),
						rs.getString("EMAILA"), rs.getString("TELEFONOA"), rs.getString("KONTRATAZIO_DATA"),
						rs.getString("NAGUSI_IZENA"));
				lista.add(l);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("ERROREA: " + e);
			e.printStackTrace();
		}

		return lista;
	}

	/**
	 * Datu-basean langile baten informazio pertsonala eta kontaktua eguneratzen ditu.
	 * * @param l Eguneratu nahi den langile objektua.
	 * @return Eguneraketa ondo joan bada true, bestela false.
	 */
	public boolean eguneratuLangileak(Langilea l) {

		boolean ondo = false;

		try {
			Connection conn = konexioa();
			String sql1 = "UPDATE LANGILE SET IZENA=?, ABIZENA=?, EMAILA=?, TELEFONOA=?  WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql1);

			ps.setString(1, l.getIzena());
			ps.setString(2, l.getAbizena());
			ps.setString(3, l.getEmaila());
			ps.setString(4, l.getTelefonoa());
			ps.setInt(5, l.getId());

			ps.executeUpdate();
			ps.close();

			conn.close();
			ondo = true;

		} catch (SQLException e) {
			System.out.println("ERROREA Eguneratzean: " + e);
		}
		return ondo;

	}

	/**
	 * Langile bat datu-basetik ezabatzen du bere ID-aren arabera (Trigger-a bidez babeskopia eginda).
	 * * @param id Ezabatu nahi den langilearen identifikatzailea.
	 * @return Ezabaketa ondo joan bada true, bestela false.
	 */
	public boolean ezabatuLangilea(int id) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sqlLogin = "DELETE FROM SALTZAILE WHERE ID = ?";
			PreparedStatement ps1 = conn.prepareStatement(sqlLogin);
			ps1.setInt(1, id);
			ps1.executeUpdate();
			ps1.close();

			String sqlLangile = "DELETE FROM LANGILE WHERE ID = ?";
			PreparedStatement ps2 = conn.prepareStatement(sqlLangile);
			ps2.setInt(1, id);
			ps2.executeUpdate();
			ps2.close();

			ondo = true;

		} catch (SQLException e) {
			System.out.println("ERROREA langilea ezabatzen: " + e);
		}
		return ondo;

	}

	/**
	 * Langile berri batentzat erabilgarri dagoen hurrengo ID-a kalkulatzen du.
	 * * @return Langile berriaren ID zenbakia.
	 */
	public int hurrengoId() {
		int id = 1;
		try {
			Connection conn = konexioa();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM LANGILE");
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
	 * Langile berri bat (eta bere saltzaile profila) datu-basean txertatzen ditu.
	 * * @param lBerria Datu-basean gordeko den langile berria.
	 * @return Txertaketa ondo joan bada true, bestela false.
	 */
	public boolean txertatuLangilea(Langilea lBerria) {
		boolean ondo = false;
		try {
			Connection conn = konexioa();

			String sqlLangile = "INSERT INTO LANGILE (ID, IZENA, ABIZENA, EMAILA, TELEFONOA, KONTRATAZIO_DATA, ID_NAGUSI) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement ps1 = conn.prepareStatement(sqlLangile);
			ps1.setInt(1, lBerria.getId());
			ps1.setString(2, lBerria.getIzena());
			ps1.setString(3, lBerria.getAbizena());
			ps1.setString(4, lBerria.getEmaila());
			ps1.setString(5, lBerria.getTelefonoa());
			ps1.setString(6, lBerria.getkData());
			ps1.setString(7, lBerria.getNagusia());

			ps1.executeUpdate();
			ps1.close();

			String sqlLogin = "INSERT INTO SALTZAILE (ID, ERABILTZAILEA, PASAHITZA) VALUES (?, ?, ?)";
			PreparedStatement ps2 = conn.prepareStatement(sqlLogin);
			ps2.setInt(1, lBerria.getId());
			ps2.setString(2, lBerria.getIzena());
			ps2.setString(3, lBerria.getPasahitza());

			ps2.executeUpdate();
			ps2.close();

			conn.close();
			ondo = true;
		} catch (SQLException e) {
			System.out.println("ERROREA Gehitzean: " + e);
		}
		return ondo;
	}

	/**
	 * Saltzaile/Langile baten pasahitza eguneratzen du SALTZAILE taulan.
	 * * @param langileId Langilearen identifikatzailea.
	 * @param pasahitzBerria Ezarri nahi den pasahitz berria.
	 * @return Eguneraketa ondo joan bada true, bestela false.
	 */
	public boolean eguneratuPasahitza(int langileId, String pasahitzBerria) {
		boolean ondo = false;
		// AHORA BUSCAMOS POR EL ID DEL VENDEDOR EN LUGAR DE SU NOMBRE
		String sql = "UPDATE SALTZAILE SET PASAHITZA = ? WHERE ID = ?";

		try {
			Connection conn = konexioa();
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, pasahitzBerria);
			ps.setInt(2, langileId);

			int filasModificadas = ps.executeUpdate();
			if (filasModificadas > 0) {
				ondo = true;
			}

			ps.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERROREA Pasahitza aldatzean: " + e.getMessage());
		}
		return ondo;
	}

}