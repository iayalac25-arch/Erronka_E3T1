package db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Datu-basearekin konexioa ezartzeko eta kudeatzeko klase nagusia.
 * Gainerako DB klaseek hemendik heredatzen dute.
 */
public class DB {
	private String url;
	private String user;
	private String pass;

	/**
	 * Defektuzko sortzailea. Oracle datu-basearen konexio lehenetsia konfiguratzen du.
	 */
	public DB() {
		this.url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		this.user = "ERRONKA";
		this.pass = "ERRONKA";
	}

	/**
	 * Sortzailea parametroekin. Datu-baseko konexio pertsonalizatu bat sortzeko erabiltzen da.
	 * @param url Datu-basearen URL-a.
	 * @param user Erabiltzaile izena.
	 * @param pass Pasahitza.
	 */
	public DB(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}

	/**
	 * Datu-basearekin konexioa sortzen du.
	 * @return Datu-basearekin ezarritako Connection objektua.
	 */
	public Connection konexioa() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			return conn;
		} catch (Exception e) {
			System.out.println("Konexio errorea: " + e);
		}
		return conn;
	}

}
