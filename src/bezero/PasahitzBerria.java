package bezero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import db.DBBezero;

import java.awt.*;

/**
 * Bezeroaren pasahitza aldatzeko erabiltzen den leiho lagungarria.
 */
public class PasahitzBerria extends JDialog {

	private JTextField txtPasahitz1, txtPasahitz2;
	private JButton botoiaEguneratu, botoiaUtzi;
	private int bezeroId;

	/**
	 * Pasahitza aldatzeko leihoa hasieratzen du.
	 * * @param gurasoa Nondik deitzen den (guraso-leihoa).
	 * @param id Bezeroaren identifikatzailea datu-basean eguneratzeko.
	 */
	public PasahitzBerria(JFrame gurasoa, int id) {
		super(gurasoa, "Pasahitza berria", true);
		this.bezeroId = id;

		setSize(350, 250);
		setLocationRelativeTo(gurasoa);

		JPanel panelNagusia = new JPanel(new BorderLayout(10, 20));
		panelNagusia.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel lblTituloa = new JLabel("Pasahitza berria", SwingConstants.CENTER);
		panelNagusia.add(lblTituloa, BorderLayout.NORTH);

		JPanel panelPashitzBerria = new JPanel(new GridLayout(2, 2, 10, 15));
		panelPashitzBerria.add(new JLabel("Pasahitz berria:"));
		txtPasahitz1 = new JTextField();
		panelPashitzBerria.add(txtPasahitz1);

		panelPashitzBerria.add(new JLabel("Errepikatu:"));
		txtPasahitz2 = new JTextField();
		panelPashitzBerria.add(txtPasahitz2);

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
			String pasahitza1 = txtPasahitz1.getText().trim();
			String pasahitza2 = txtPasahitz2.getText().trim();

			if (pasahitza1.isEmpty() || pasahitza2.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Bete bi eremuak");
			} else if (!pasahitza1.equals(pasahitza2)) {
				JOptionPane.showMessageDialog(this, "Bi pasahitzak berdinak izan behar dira");
			} else {
				DBBezero db = new DBBezero();
				boolean ondo = db.eguneratuPasahitza(this.bezeroId, pasahitza1);

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