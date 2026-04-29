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
import db.DBProduktuak;
import eredua.Kategoria;
import eredua.Produktua;

/**
 * Produktuen kudeaketarako interfazea. Saltzaileari produktuak iragazteko,
 * gehitzeko, aldatzeko eta ezabatzeko bidea ematen dio.
 */
public class SaltzaileProduktuakGUI extends JFrame {

	private JComboBox<Kategoria> kategoriaFiltroa;
	private JComboBox<Produktua> produktua;
	private JTextField id, izena, kategoria, salneurria, stock;
	private ArrayList<Produktua> produktuGuztiak;
	private ArrayList<Kategoria> kategoriaGuztiak;
	private DBProduktuak dbProduktuak;

	/**
	 * Produktuen kudeaketa-leihoa sortzen du eta osagaiak kargatzen ditu.
	 */
	public SaltzaileProduktuakGUI() {
		setTitle("Saltzaile-Produktuak");
		setSize(500, 500);
		setLayout(new BorderLayout(15, 15));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		dbProduktuak = new DBProduktuak();
		kategoriaGuztiak = dbProduktuak.getKategoriak();
		produktuGuztiak = dbProduktuak.getProduktuak();

		// Kategoria Filtroa
		JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
		goikoPanela.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		goikoPanela.add(new JLabel("Kategoria:"));
		kategoriaFiltroa = new JComboBox<Kategoria>();
		kategoriaFiltroa.addItem(new Kategoria(-1, "Guztiak"));
		for (Kategoria k : kategoriaGuztiak) {
			kategoriaFiltroa.addItem(k);
		}
		goikoPanela.add(kategoriaFiltroa);

		add(goikoPanela, BorderLayout.NORTH);

		// Produktu Datuak
		JPanel erdikoPanela = new JPanel(new GridLayout(6, 2, 10, 10));
		erdikoPanela.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

		JPanel aukeratu = new JPanel(new FlowLayout(FlowLayout.CENTER));
		aukeratu.add(new JLabel("Aukeratu Produktua:"));
		produktua = new JComboBox<Produktua>();
		aukeratu.add(produktua);

		id = new JTextField();
		id.setEditable(false);
		izena = new JTextField();
		izena.setEditable(false);
		kategoria = new JTextField();
		kategoria.setEditable(false);
		salneurria = new JTextField();
		stock = new JTextField();

		erdikoPanela.add(new JLabel("Aukeratu Produktua:"));
		erdikoPanela.add(produktua);
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

		add(erdikoPanela, BorderLayout.CENTER);

		// CRUD ETA DESKONTUA
		JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JButton gehitu = new JButton("Gehitu");
		JButton eguneratu = new JButton("Eguneratu");
		JButton deskontua = new JButton("Deskontua");
		JButton ezabatu = new JButton("Ezabatu");
		JButton irten = new JButton("Irten");

		behekoPanela.add(gehitu);
		behekoPanela.add(eguneratu);
		behekoPanela.add(deskontua); 
		behekoPanela.add(ezabatu);
		behekoPanela.add(irten);
		add(behekoPanela, BorderLayout.SOUTH);

		kategoriaFiltroa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filtratuProduktuak();
			}
		});

		produktua.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Produktua p = (Produktua) produktua.getSelectedItem();
				if (p != null) {
					id.setText(String.valueOf(p.getId()));
					izena.setText(p.getIzena());
					salneurria.setText(p.getSalneurria());
					stock.setText(String.valueOf(p.getStock()));

					for (Kategoria k : kategoriaGuztiak) {
						if (k.getId() == p.getIdKategoria()) {
							kategoria.setText(k.getIzena());
							break;
						}
					}
				}
			}
		});

		eguneratu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Produktua p = (Produktua) produktua.getSelectedItem();
				if (p != null) {
					try {
						String prezioZaharra = p.getSalneurria();
						int stockZaharra = p.getStock();
						p.setSalneurria(salneurria.getText().trim());
						p.setStock(Integer.parseInt(stock.getText().trim()));

						if (dbProduktuak.eguneratuProduktua(p)) {
							JOptionPane.showMessageDialog(null, "Produktua ondo eguneratu da.");
						} else {
							p.setSalneurria(prezioZaharra);
							p.setStock(stockZaharra);
							salneurria.setText(prezioZaharra);
							stock.setText(String.valueOf(stockZaharra));
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Stock-ak zenbakia izan behar du.", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		gehitu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaltzaileProduktuaGehituGUI gehituLeihoa = new SaltzaileProduktuaGehituGUI(SaltzaileProduktuakGUI.this,
						dbProduktuak, kategoriaGuztiak);

				gehituLeihoa.setVisible(true);

				produktuGuztiak = dbProduktuak.getProduktuak();
				filtratuProduktuak();
			}
		});

		ezabatu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Produktua p = (Produktua) produktua.getSelectedItem();
				if (p != null) {
					int aukera = JOptionPane.showConfirmDialog(null,
							"Ziur zaude " + p.getIzena() + " ezabatu nahi duzula?", "Baieztatu",
							JOptionPane.YES_NO_OPTION);
					if (aukera == JOptionPane.YES_OPTION) {
						if (dbProduktuak.ezabatuProduktua(p.getId())) {
							produktuGuztiak.remove(p);
							filtratuProduktuak();
							JOptionPane.showMessageDialog(null, "Produktua ezabatu da.");
						}
					}
				}
			}
		});

		deskontua.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Kategoria kat = (Kategoria) kategoriaFiltroa.getSelectedItem();

				if (kat == null || kat.getId() == -1) {
					JOptionPane.showMessageDialog(null, "Mesedez, hautatu kategoria espezifiko bat deskontua aplikatzeko.",
							"Abisua", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String s = JOptionPane.showInputDialog(null,
						kat.getIzena() + " kategoriari aplikatu nahi diozun deskontua (%):",
						"Deskontu Masiboa", JOptionPane.QUESTION_MESSAGE);

				if (s != null && !s.isEmpty()) {
					try {
						int portzentajea = Integer.parseInt(s);

						if (dbProduktuak.aplikatuDeskontuaKategoriari(kat.getId(), portzentajea)) {
							JOptionPane.showMessageDialog(null, "Deskontua ondo aplikatu da kategoria osoko produktuetan.");
							
							// Datuak berriro kargatu DBtik prezio berriak ikusteko
							produktuGuztiak = dbProduktuak.getProduktuak(); 
							filtratuProduktuak();
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Mesedez, sartu zenbaki oso bat.",
								"Errorea", JOptionPane.ERROR_MESSAGE);
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

		filtratuProduktuak();
		setLocationRelativeTo(null);
	}

	/**
	 * Datu-basetik produktuak lortzen ditu eta hautatutako kategoriaren arabera
	 * erakusten ditu.
	 */
	public void filtratuProduktuak() {
		produktua.removeAllItems();
		Kategoria filtroa = (Kategoria) kategoriaFiltroa.getSelectedItem();
		if (filtroa != null) {
			for (Produktua p : produktuGuztiak) {
				if (filtroa.getId() == -1 || p.getIdKategoria() == filtroa.getId()) {
					produktua.addItem(p);
				}
			}
		}
	}

}