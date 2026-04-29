package saltzaile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBLangileak;
import eredua.Langilea;

/**
 * Langile (edo saltzaile) berri bat sisteman txertatzeko erabiltzen den leihoa.
 */
public class SaltzaileLangileaGehituGUI extends JDialog {

	private JComboBox<Langilea> langileaCB;
	private DBLangileak dbLangileak;
	private JTextField id, izena, abizena, emaila, telefonoa, kData, nagusia, pasahitza;

	/**
	 * Langilea gehitzeko leihoa sortzen du.
	 * @param saltzaileLangileakGUI Guraso-leihoa.
	 * @param dbLangileak Datu-basearekin komunikatzeko objektua.
	 * @param langileGuztiak Sisteman dauden langileen zerrenda.
	 */
	public SaltzaileLangileaGehituGUI(SaltzaileLangileakGUI saltzaileLangileakGUI, DBLangileak dbLangileak,
			ArrayList<Langilea> langileGuztiak) {
		super(saltzaileLangileakGUI, "Langile berria gehitu", true);
		this.dbLangileak = dbLangileak;

		setSize(400, 350);
		setLayout(new BorderLayout(10, 10));

		JPanel erdikoPanela = new JPanel(new GridLayout(9, 2, 10, 10));
		erdikoPanela.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

		id = new JTextField(String.valueOf(dbLangileak.hurrengoId()));
		id.setEditable(false);
		izena = new JTextField();
		abizena = new JTextField();
		emaila = new JTextField();
		telefonoa = new JTextField();
		kData = new JTextField();
		nagusia = new JTextField();
		pasahitza = new JTextField();

		langileaCB = new JComboBox<Langilea>();
		for (Langilea l : langileGuztiak) {
			if (l.getId() != -1) {
				langileaCB.addItem(l);
			}
		}

		erdikoPanela.add(new JLabel("ID-a"));
		erdikoPanela.add(id);
		erdikoPanela.add(new JLabel("Izena:"));
		erdikoPanela.add(izena);
		erdikoPanela.add(new JLabel("Abizena:"));
		erdikoPanela.add(abizena);
		erdikoPanela.add(new JLabel("Emaila:"));
		erdikoPanela.add(emaila);
		erdikoPanela.add(new JLabel("Telefonoa:"));
		erdikoPanela.add(telefonoa);
		erdikoPanela.add(new JLabel("Kontratazio Data:"));
		erdikoPanela.add(kData);
		erdikoPanela.add(new JLabel("Nagusia:"));
		erdikoPanela.add(nagusia);
		erdikoPanela.add(new JLabel("Pasahitza:"));
		erdikoPanela.add(pasahitza);

		add(erdikoPanela, BorderLayout.CENTER);

		JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		JButton gorde = new JButton("Gorde");
		JButton utzi = new JButton("Utzi");

		behekoPanela.add(gorde);
		behekoPanela.add(utzi);
		add(behekoPanela, BorderLayout.SOUTH);

		utzi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		gorde.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int idZenbakia = Integer.parseInt(id.getText().trim());
					String izenTestua = izena.getText().trim();
					String abizenaTestua = abizena.getText().trim();
					String emailaTestua = emaila.getText().trim();
					String telefonoTestua = telefonoa.getText().trim();
					String dataTestua = kData.getText().trim();
					String nagusiaTestua = nagusia.getText().trim();
					String pasahitzaTestua = pasahitza.getText().trim();

					if (izenTestua.isEmpty() || abizenaTestua.isEmpty() || emailaTestua.isEmpty()
							|| telefonoTestua.isEmpty() || dataTestua.isEmpty() || nagusiaTestua.isEmpty()
							|| pasahitzaTestua.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Eremu guztiak bete behar dituzu.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					Langilea lBerria = new Langilea(idZenbakia, izenTestua, abizenaTestua, emailaTestua, telefonoTestua,
							dataTestua, nagusiaTestua, pasahitzaTestua);

					if (dbLangileak.txertatuLangilea(lBerria)) {
						JOptionPane.showMessageDialog(null, "Langilea ondo gorde da!");

						SaltzaileLangileakGUI GUI = (SaltzaileLangileakGUI) getParent();
						GUI.cbDatuakEguneratu();

						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Errorea datu-basean txertatzean.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "ID, Stock eta Biltegi eremuek zenbakiak izan behar dute.",
							"Formatu Errorea", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		setLocationRelativeTo(saltzaileLangileakGUI);
	}

}
