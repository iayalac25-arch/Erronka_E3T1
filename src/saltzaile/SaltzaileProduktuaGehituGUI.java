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
import db.DBProduktuak;
import eredua.Kategoria;
import eredua.Produktua;

/**
 * Produktu berri bat sistemaren datu-basean eta inbentarioan txertatzeko erabiltzen den leihoa.
 */
public class SaltzaileProduktuaGehituGUI extends JDialog {

	private JTextField id, izena, salneurria, stock, biltegi;
	private JComboBox<Kategoria> kategoria;
	private DBProduktuak db;

	/**
	 * Produktua gehitzeko leihoa sortzen du.
	 * @param saltzaileProduktuakGUI Guraso-leihoa (produktu zerrendaren leihoa).
	 * @param db Datu-basearekin konektatzen den objektua.
	 * @param kategoriaGuztiak Sisteman dauden kategoria guztien zerrenda.
	 */
	public SaltzaileProduktuaGehituGUI(SaltzaileProduktuakGUI saltzaileProduktuakGUI, DBProduktuak db,
			ArrayList<Kategoria> kategoriaGuztiak) {
		super(saltzaileProduktuakGUI, "Produktu Berria Gehitu", true);
		this.db = db;

		setSize(400, 350);
		setLayout(new BorderLayout(10, 10));

		JPanel erdikoPanela = new JPanel(new GridLayout(6, 2, 10, 15));
		erdikoPanela.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		id = new JTextField(String.valueOf(db.hurrengoId()));
		id.setEditable(false);
		izena = new JTextField();
		salneurria = new JTextField();
		stock = new JTextField();
		biltegi = new JTextField("8");

		kategoria = new JComboBox<Kategoria>();
		for (Kategoria k : kategoriaGuztiak) {
			if (k.getId() != -1) {
				kategoria.addItem(k);
			}
		}

		erdikoPanela.add(new JLabel("ID Kodea:"));
		erdikoPanela.add(id);
		erdikoPanela.add(new JLabel("Izena:"));
		erdikoPanela.add(izena);
		erdikoPanela.add(new JLabel("Kategoria:"));
		erdikoPanela.add(kategoria);
		erdikoPanela.add(new JLabel("Salneurria (€):"));
		erdikoPanela.add(salneurria);
		erdikoPanela.add(new JLabel("Stock kopurua:"));
		erdikoPanela.add(stock);
		erdikoPanela.add(new JLabel("Biltegi ID:"));
		erdikoPanela.add(biltegi);

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
					String salneurriTestua = salneurria.getText().trim().replace(",", ".");
					int stockZenbakia = Integer.parseInt(stock.getText().trim());
					int biltegiId = Integer.parseInt(biltegi.getText().trim());

					Kategoria katAukeratua = (Kategoria) kategoria.getSelectedItem();

					if (izenTestua.isEmpty() || salneurriTestua.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Eremu guztiak bete behar dituzu.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					Produktua pBerria = new Produktua(idZenbakia, izenTestua, salneurriTestua, katAukeratua.getId(),
							stockZenbakia, biltegiId);

					if (db.txertatuProduktua(pBerria)) {
						JOptionPane.showMessageDialog(null, "Produktua ondo gorde da!");
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

		setLocationRelativeTo(saltzaileProduktuakGUI);
	}
}
