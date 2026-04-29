package saltzaile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.DBLangileak;

/**
 * Saltzaile baten pasahitza eguneratzeko erabiltzen den leiho lagungarria.
 */
public class PasahitzaBerria extends JDialog {

	private JTextField txtPass1, txtPass2;
	private JButton botoiaEguneratu, botoiaUtzi;
	private int saltzaileId;

	/**
	 * Pasahitza aldatzeko leihoa hasieratzen du.
	 * * @param gurasoa Nondik deitzen den (guraso-leihoa).
	 * @param id Langilearen identifikatzailea datu-basean eguneratzeko.
	 */
	public PasahitzaBerria(SaltzaileLangileakGUI GUI, int id) {
		super(GUI, "Pasahitza berria", true);
		this.saltzaileId = id;

		setSize(350, 250);
		setLocationRelativeTo(GUI);

		JPanel panelNagusia = new JPanel(new BorderLayout(10, 20));
		panelNagusia.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lblTituloa = new JLabel("Pasahitza berria", SwingConstants.CENTER);
		lblTituloa.setFont(new Font("Arial", Font.BOLD, 16));
		panelNagusia.add(lblTituloa, BorderLayout.NORTH);

		JPanel panelPashitzBerria = new JPanel(new GridLayout(2, 2, 10, 15));
		panelPashitzBerria.add(new JLabel("Pasahitz berria:"));
		txtPass1 = new JTextField();
		panelPashitzBerria.add(txtPass1);

		panelPashitzBerria.add(new JLabel("Errepikatu:"));
		txtPass2 = new JTextField();
		panelPashitzBerria.add(txtPass2);

		panelNagusia.add(panelPashitzBerria, BorderLayout.CENTER);

		JPanel panelBotoiak = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		botoiaEguneratu = new JButton("Eguneratu");
		botoiaUtzi = new JButton("Utzi");
		panelBotoiak.add(botoiaEguneratu);
		panelBotoiak.add(botoiaUtzi);
		panelNagusia.add(panelBotoiak, BorderLayout.SOUTH);

		add(panelNagusia);

		botoiaUtzi.addActionListener(e -> dispose());

		botoiaEguneratu.addActionListener(e -> {
			String pasahitza1 = txtPass1.getText().trim();
			String pasahitza2 = txtPass2.getText().trim();

			if (pasahitza1.isEmpty() || pasahitza2.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Bete bi eremuak");
			} else if (!pasahitza1.equals(pasahitza2)) {
				JOptionPane.showMessageDialog(this, "Bi pasahitzak berdinak izan behar dira");
			} else {
				DBLangileak dbLangileak = new DBLangileak();
				boolean ondo = dbLangileak.eguneratuPasahitza(this.saltzaileId, pasahitza1);
				if (ondo) {
					JOptionPane.showMessageDialog(this, "Pasahitza ondo aldatu da datu basean");
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Errorea aldatzean.");
				}
			}
		});

	}

}
