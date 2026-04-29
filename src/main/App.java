package main;

import javax.swing.SwingUtilities;
import login.Login;

/**
 * Aplikazioaren abiarazte-puntua.
 * Hilo grafikoa sortu eta Login leihoa erakusten du.
 */
public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Login loginPantaila = new Login();
					loginPantaila.setVisible(true);
				} catch (Exception e) {
					System.out.println("Errorea aplikazioa abiaraztean: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
}