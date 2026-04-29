package login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import bezero.BezeroErosiProduktuakGUI;
import db.DBBezero;
import saltzaile.SaltzaileMenuGUI;

import java.awt.*;

/**
 * Erabiltzaileak (bezeroak zein saltzaileak) sistemara sartzeko erabiltzen duten saio-hasierako leihoa.
 */
public class Login extends JFrame {

	private JTextField txtErabiltzailea;
	private JTextField txtPasahitza;
	private JButton botoiaSartu, botoiaUtzi;

	/**
	 * Login leihoa eta bere osagai grafiko guztiak hasieratzen ditu.
	 */
	public Login() {
		setTitle("GameStop");
		setSize(350, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panelNagusia = new JPanel(new BorderLayout(10, 20));
		panelNagusia.setBorder(new EmptyBorder(30, 30, 30, 30));

		JLabel lblTituloa = new JLabel("IDENTIFIKATU", SwingConstants.CENTER);
		panelNagusia.add(lblTituloa, BorderLayout.NORTH);

		JPanel panelLogin = new JPanel(new GridLayout(2, 2, 10, 15));
		panelLogin.add(new JLabel("Erabiltzailea:"));
		txtErabiltzailea = new JTextField();
		panelLogin.add(txtErabiltzailea);

		panelLogin.add(new JLabel("Pasahitza:"));
		txtPasahitza = new JTextField();
		panelLogin.add(txtPasahitza);

		panelNagusia.add(panelLogin, BorderLayout.CENTER);

		JPanel panelBotoiak = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		botoiaSartu = new JButton("Sartu");
		botoiaUtzi = new JButton("Utzi");
		panelBotoiak.add(botoiaSartu);
		panelBotoiak.add(botoiaUtzi);

		panelNagusia.add(panelBotoiak, BorderLayout.SOUTH);
		add(panelNagusia);

		botoiaUtzi.addActionListener(e -> System.exit(0));

		botoiaSartu.addActionListener(e -> {
			String erabiltzaile = txtErabiltzailea.getText().trim();
			String pasahitza = txtPasahitza.getText().trim();

			if (erabiltzaile.isEmpty() || pasahitza.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Bi eremuak bete behar dira");

			} else {
				DBBezero db = new DBBezero();

				if (db.egiaztatuLoginaBezero(erabiltzaile, pasahitza)) {
					int bezeroId = db.lortuBezeroId(erabiltzaile, pasahitza);
					BezeroErosiProduktuakGUI leihoa = new BezeroErosiProduktuakGUI(bezeroId,
							new java.util.ArrayList<>());
					leihoa.setVisible(true);
					dispose();

				} else if (db.egiaztatuLoginaSaltzaile(erabiltzaile, pasahitza)) {
					SaltzaileMenuGUI leihoaSaltzaile = new SaltzaileMenuGUI();
					leihoaSaltzaile.setVisible(true);
					dispose();

				} else {
					JOptionPane.showMessageDialog(this, "Erabiltzaile edo pasahitz okerra");
				}
			}
		});
	}

	public static void main(String[] args) {
		new Login().setVisible(true);
	}
}