package db;

import java.sql.*;
import javax.swing.DefaultComboBoxModel;

/**
 * Bezeroen datu-baseko eragiketak kudeatzen dituen klasea. Logina, datuak
 * lortzea eta eguneratzea ahalbidetzen du.
 */
public class DBBezero extends DB {

	/**
	 * Bezero guztien izen-abizenak lortzen ditu ComboBox batean erakusteko.
	 * * @return Bezeroen izenak dituen DefaultComboBoxModel objektua.
	 */
	public DefaultComboBoxModel<String> lortuBezeroIzenak() {
		DefaultComboBoxModel<String> modeloa = new DefaultComboBoxModel<>();
		modeloa.addElement("Hautatu bezero bat");

		String sql = "SELECT IZENA, ABIZENA FROM BEZERO ORDER BY IZENA";

		try (Connection conn = konexioa(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				String IzenGuztia = rs.getString("IZENA") + " " + rs.getString("ABIZENA");
				modeloa.addElement(IzenGuztia);
			}
		} catch (SQLException e) {
			System.out.println("Errorea bezeroak lortzean: " + e.getMessage());
		}
		return modeloa;
	}

	/**
	 * Bezero baten helbidea eta emaila lortzen ditu.
	 * 
	 * @param izena   Bezeroaren izena.
	 * @param abizena Bezeroaren abizena.
	 * @return Helbidea eta emaila dituen String array-a.
	 */
	public String[] lortuBezeroDatuak(String izena, String abizena) {
		String[] datuak = new String[2];
		String sql = "SELECT HELBIDEA, EMAILA FROM BEZERO WHERE IZENA = ? AND ABIZENA = ?";

		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, izena);
			ps.setString(2, abizena);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				datuak[0] = rs.getString("HELBIDEA") != null ? rs.getString("HELBIDEA") : "";
				datuak[1] = rs.getString("EMAILA") != null ? rs.getString("EMAILA") : "";
			}
		} catch (SQLException e) {
			System.out.println("Errorea datuak lortzean: " + e.getMessage());
		}
		return datuak;
	}

	/**
	 * Bezero baten datu guztiak lortzen ditu bere ID-a erabiliz.
	 * 
	 * @param id Bezeroaren identifikatzailea.
	 * @return Izena, abizena, helbidea eta emaila dituen array-a.
	 */
	public String[] lortuDatuGuztiakIdBidez(int id) {
		String[] datuak = new String[4];
		String sql = "SELECT IZENA, ABIZENA, HELBIDEA, EMAILA FROM BEZERO WHERE ID = ?";

		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				datuak[0] = rs.getString("IZENA");
				datuak[1] = rs.getString("ABIZENA");
				datuak[2] = rs.getString("HELBIDEA") != null ? rs.getString("HELBIDEA") : "";
				datuak[3] = rs.getString("EMAILA") != null ? rs.getString("EMAILA") : "";
			}
		} catch (SQLException e) {
			System.out.println("Errorea datuak lortzean: " + e.getMessage());
		}
		return datuak;
	}

	/**
	 * Bezero baten pasahitza eguneratzen du datu-basean.
	 * 
	 * @param id             Bezeroaren identifikatzailea.
	 * @param pasahitzBerria Ezarri nahi den pasahitz berria.
	 * @return Eguneraketa ondo joan bada true, bestela false.
	 */
	public boolean eguneratuPasahitza(int id, String pasahitzBerria) {
		String sql = "UPDATE BEZERO_PASAHITZA SET PASAHITZA = ? WHERE ID = ?";
		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, pasahitzBerria);
			ps.setInt(2, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println("Errorea pasahitza eguneratzean: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Bezero baten saio-hasiera (logina) egiaztatzen du BEZERO_PASAHITZA taulan.
	 * 
	 * @param erabiltzailea Bezeroaren erabiltzailea.
	 * @param pasahitza     Bezeroaren pasahitza.
	 * @return Kredentzialak zuzenak badira true, bestela false.
	 */
	public boolean egiaztatuLoginaBezero(String erabiltzailea, String pasahitza) {
		String sql = "SELECT * FROM BEZERO_PASAHITZA WHERE ERABILTZAILEA = ? AND PASAHITZA = ?";
		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, erabiltzailea.trim());
			ps.setString(2, pasahitza.trim());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			System.out.println("Errorea loginean: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saltzaile baten saio-hasiera egiaztatzen du SALTZAILE taulan.
	 * 
	 * @param erabiltzailea Saltzailearen erabiltzailea.
	 * @param pasahitza     Saltzailearen pasahitza.
	 * @return Kredentzialak zuzenak badira true, bestela false.
	 */
	public boolean egiaztatuLoginaSaltzaile(String erabiltzailea, String pasahitza) {
		String sql = "SELECT * FROM SALTZAILE WHERE ERABILTZAILEA = ? AND PASAHITZA = ?";

		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, erabiltzailea.trim());
			ps.setString(2, pasahitza.trim());

			ResultSet rs = ps.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			System.out.println("Errorea loginean: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Bezero baten ID-a lortzen du bere erabiltzaile eta pasahitzaren bidez.
	 * 
	 * @param erabiltzailea Bezeroaren erabiltzailea.
	 * @param pasahitza     Bezeroaren pasahitza.
	 * @return Bezeroaren ID-a, edo -1 ez bada aurkitzen.
	 */
	public int lortuBezeroId(String erabiltzailea, String pasahitza) {
		int id = -1;
		String sql = "SELECT ID FROM BEZERO_PASAHITZA WHERE ERABILTZAILEA = ? AND PASAHITZA = ?";
		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, erabiltzailea.trim());
			ps.setString(2, pasahitza.trim());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			System.out.println("Errorea IDa lortzean loginetik: " + e.getMessage());
		}
		return id;
	}

	/**
	 * Bezero baten ID-a lortzen du bere izen eta abizenen bidez. * @param izena
	 * Bezeroaren izena.
	 * 
	 * @param abizena Bezeroaren abizena.
	 * @return Bezeroaren ID-a, edo -1 ez bada aurkitzen.
	 */
	public int lortuBezeroIdIzenarekin(String izena, String abizena) {
		int id = -1;
		String sql = "SELECT ID FROM BEZERO WHERE IZENA = ? AND ABIZENA = ?";
		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, izena.trim());
			ps.setString(2, abizena.trim());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			System.out.println("Errorea bezero IDa lortzean izenarekin: " + e.getMessage());
		}
		return id;
	}

	/**
	 * Bezero baten datu pertsonalak eguneratzen ditu datu-basean.
	 * 
	 * @param id       Bezeroaren identifikatzailea.
	 * @param izena    Bezeroaren izen berria.
	 * @param abizena  Bezeroaren abizen berria.
	 * @param helbidea Bezeroaren helbide berria.
	 * @param emaila   Bezeroaren email berria.
	 * @return Eguneraketa ondo joan bada true, bestela false.
	 */
	public boolean eguneratuBezeroDatuak(int id, String izena, String abizena, String helbidea, String emaila) {
		String sql = "UPDATE BEZERO SET IZENA = ?, ABIZENA = ?, HELBIDEA = ?, EMAILA = ? WHERE ID = ?";

		try (Connection conn = konexioa(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, izena.trim());
			ps.setString(2, abizena.trim());
			ps.setString(3, helbidea.trim());
			ps.setString(4, emaila.trim());
			ps.setInt(5, id);

			int eguneratutakoIlarak = ps.executeUpdate();
			return eguneratutakoIlarak > 0;

		} catch (SQLException e) {
			System.out.println("Errorea bezeroaren datuak eguneratzean: " + e.getMessage());
			return false;
		}
	}

}