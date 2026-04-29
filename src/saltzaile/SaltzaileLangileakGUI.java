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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import db.DBLangileak;
import eredua.Langilea;

/**
 * Enpresako langileen kudeaketa egiteko interfazea (zerrendatu, eguneratu eta ezabatu).
 */
public class SaltzaileLangileakGUI extends JFrame {

	private DBLangileak dbLangileak;
	private JTextField id, izena, abizena, emaila, telefonoa, kData, nagusia;
	private JComboBox<Langilea> langileaCB;
	private ArrayList<Langilea> langileGuztiak;

	/**
	 * Langileen kudeaketa-leihoa sortzen du eta osagaiak hasieratzen ditu.
	 */
	public SaltzaileLangileakGUI() {

		setTitle("Saltzaile-Langileak");
		setSize(500, 500);
		setLayout(new BorderLayout(15, 15));

		dbLangileak = new DBLangileak();
		langileGuztiak = dbLangileak.getLangileak();

		JPanel aukeratuLangile = new JPanel(new FlowLayout(FlowLayout.CENTER));
		aukeratuLangile.add(new JLabel("Langileak:"));
		langileaCB = new JComboBox<Langilea>();

		for (Langilea l : langileGuztiak) {
			langileaCB.addItem(l);
		}

		aukeratuLangile.add(langileaCB);

		add(aukeratuLangile, BorderLayout.NORTH);

		JPanel langilePanela = new JPanel(new GridLayout(8, 2, 10, 10));
		langilePanela.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

		id = new JTextField();
		id.setEditable(false);
		izena = new JTextField();
		abizena = new JTextField();
		emaila = new JTextField();
		telefonoa = new JTextField();
		kData = new JTextField();
		kData.setEditable(false);
		nagusia = new JTextField();
		nagusia.setEditable(false);

		langilePanela.add(new JLabel("ID-a"));
		langilePanela.add(id);
		langilePanela.add(new JLabel("Izena:"));
		langilePanela.add(izena);
		langilePanela.add(new JLabel("Abizena:"));
		langilePanela.add(abizena);
		langilePanela.add(new JLabel("Emaila:"));
		langilePanela.add(emaila);
		langilePanela.add(new JLabel("Telefonoa:"));
		langilePanela.add(telefonoa);
		langilePanela.add(new JLabel("Kontratazio Data:"));
		langilePanela.add(kData);
		langilePanela.add(new JLabel("Nagusia:"));
		langilePanela.add(nagusia);

		JButton eguneratuPasahitza = new JButton("Eguneratu Pasahitza");

		eguneratuPasahitza.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Langilea l = (Langilea) langileaCB.getSelectedItem();
				if (l != null) {
					PasahitzaBerria pasahitzaEguneratu = new PasahitzaBerria(SaltzaileLangileakGUI.this, l.getId());
					pasahitzaEguneratu.setVisible(true);
				}
			}
		});

		langilePanela.add(eguneratuPasahitza);

		add(langilePanela, BorderLayout.CENTER);

		langileaCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Langilea l = (Langilea) langileaCB.getSelectedItem();
				if (l != null) {
					id.setText(String.valueOf(l.getId()));
					izena.setText(l.getIzena());
					abizena.setText(l.getAbizena());
					emaila.setText(l.getEmaila());
					telefonoa.setText(l.getTelefonoa());
					kData.setText(String.valueOf(l.getkData()));
					nagusia.setText(String.valueOf(l.getNagusia()));
				}
			}
		});

		// CRUD
		JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton gehitu = new JButton("Gehitu");
		JButton eguneratu = new JButton("Eguneratu");
		JButton ezabatu = new JButton("Ezabatu");
		JButton irten = new JButton("Irten");

		behekoPanela.add(gehitu);
		behekoPanela.add(eguneratu);
		behekoPanela.add(ezabatu);
		behekoPanela.add(irten);
		add(behekoPanela, BorderLayout.SOUTH);

		gehitu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Gehitu leihoa irekitzen...");
				SaltzaileLangileaGehituGUI gehituDialog = new SaltzaileLangileaGehituGUI(SaltzaileLangileakGUI.this,
						dbLangileak, langileGuztiak);
				gehituDialog.setVisible(true);
			}
		});

		eguneratu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Langilea l = (Langilea) langileaCB.getSelectedItem();

				if (l != null) {

					String izenBerria = izena.getText().trim();
					String abizenBerria = abizena.getText().trim();
					String emailBerria = emaila.getText().trim();
					String telefonoBerria = telefonoa.getText().trim();

					if (izenBerria.isEmpty() || abizenBerria.isEmpty() || emailBerria.isEmpty()
							|| telefonoBerria.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Langilea ezin da hutsik egon", "Errorea",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					try {
						l.setIzena(izenBerria);
						l.setAbizena(abizenBerria);
						l.setEmaila(emailBerria);
						l.setTelefonoa(telefonoBerria);

						if (dbLangileak.eguneratuLangileak(l)) {
							JOptionPane.showMessageDialog(null, "Langilea ondo eguneratu da.");
						}

					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Langilea izan behar du.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		ezabatu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Langilea l = (Langilea) langileaCB.getSelectedItem();
				if (l != null) {
					int aukera = JOptionPane.showConfirmDialog(null,
							"Ziur zaude " + l.getIzena() + " ezabatu nahi duzula?", "Baieztatu",
							JOptionPane.YES_NO_OPTION);
					if (aukera == JOptionPane.YES_OPTION) {
						if (dbLangileak.ezabatuLangilea(l.getId())) {
							langileGuztiak.remove(l);
							langileaCB.removeItem(l);

							JOptionPane.showMessageDialog(null, "Langilea ezabatu da.");
						}
					}
				}
			}
		});

		irten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SaltzaileMenuGUI().setVisible(true);
				dispose();
			}
		});

		setLocationRelativeTo(null);

	}

	/**
	 * Datu-basetik langileak eskuratu eta hautaketa-zerrendan (ComboBox) jartzen ditu.
	 */
	public void cbDatuakEguneratu() {
		langileGuztiak = dbLangileak.getLangileak();

		langileaCB.removeAllItems();

		for (Langilea l : langileGuztiak) {
			langileaCB.addItem(l);
		}

	}

}