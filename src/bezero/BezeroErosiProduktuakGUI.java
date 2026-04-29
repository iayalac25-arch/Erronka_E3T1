package bezero;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db.DBProduktuak;
import eredua.Kategoria;
import eredua.Produktua;
import login.Login;

/**
 * Bezeroek produktuak erosteko erabiltzen duten interfaze nagusia.
 */
public class BezeroErosiProduktuakGUI extends JFrame {

	private JComboBox<Kategoria> kategoriaFiltroa;
	private JComboBox<Produktua> produktua;
	private JTextField salneurria, stock;
	private JButton erosi, irten;
	private DBProduktuak db;
	private int bezeroId;
	private ArrayList<Produktua> alProduktu;
	private ArrayList<Produktua> produktuGuztiak;
	private ArrayList<Kategoria> kategoriaGuztiak;

	/**
	 * Leihoa hasieratzen du bezeroaren ID-a eta saskia jasota.
	 * @param bezeroId Bezeroaren identifikatzailea.
	 * @param saskia Uneko erosketa-saskia (produktuen zerrenda).
	 */
	public BezeroErosiProduktuakGUI(int bezeroId, ArrayList<Produktua> saskia) {

		this.bezeroId = bezeroId;
		this.alProduktu = (saskia != null) ? saskia : new ArrayList<Produktua>();
		this.db = new DBProduktuak();

		kategoriaGuztiak = db.getKategoriak();
		produktuGuztiak = db.getProduktuak();

		setTitle("GameStop-Bezeroa");
		setSize(450, 450);
		setLayout(new BorderLayout(15, 15));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel izenburua = new JLabel("Erosi Produktuak", SwingConstants.CENTER);
		izenburua.setFont(new Font("", Font.BOLD, 15));
		izenburua.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		add(izenburua, BorderLayout.NORTH);

		JMenuBar menuBar = new JMenuBar();
		JMenu menuProduktuak = new JMenu("Erosi Produktuak");
		JMenu menuNireDatuak = new JMenu("Nire Datuak");
		JMenu menuNireEskaerak = new JMenu("Nire Eskaerak");
		JMenu menuOrga = new JMenu("Orga");

		menuBar.add(menuProduktuak);
		menuBar.add(menuNireDatuak);
		menuBar.add(menuNireEskaerak);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(menuOrga);

		setJMenuBar(menuBar);

		menuNireDatuak.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dispose();
				BezeroNireDatuakGUI leihoa = new BezeroNireDatuakGUI(bezeroId, alProduktu);
				leihoa.toFront();
				leihoa.setVisible(true);
			}
		});

		menuNireEskaerak.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				dispose();
				NireEskaerakGUI leihoa = new NireEskaerakGUI(BezeroErosiProduktuakGUI.this, true, bezeroId, alProduktu);
				leihoa.toFront();
				leihoa.setVisible(true);
			}
		});

		menuOrga.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				dispose();
				Orga leihoa = new Orga(alProduktu, bezeroId);
				leihoa.toFront();
				leihoa.setVisible(true);
			}
		});

		JPanel erdikoPanela = new JPanel(new GridLayout(4, 2, 10, 20));
		erdikoPanela.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

		kategoriaFiltroa = new JComboBox<Kategoria>();
		kategoriaFiltroa.addItem(new Kategoria(-1, "Guztiak"));
		for (Kategoria k : kategoriaGuztiak) {
			kategoriaFiltroa.addItem(k);
		}

		produktua = new JComboBox<Produktua>();

		salneurria = new JTextField("- €");
		salneurria.setEditable(false);
		salneurria.setHorizontalAlignment(SwingConstants.CENTER);

		stock = new JTextField("-");
		stock.setEditable(false);
		stock.setHorizontalAlignment(SwingConstants.CENTER);

		erdikoPanela.add(new JLabel("Kategoria:"));
		erdikoPanela.add(kategoriaFiltroa);
		erdikoPanela.add(new JLabel("Produktua:"));
		erdikoPanela.add(produktua);
		erdikoPanela.add(new JLabel("Prezioa:"));
		erdikoPanela.add(salneurria);
		erdikoPanela.add(new JLabel("Unitateak :"));
		erdikoPanela.add(stock);

		add(erdikoPanela, BorderLayout.CENTER);

		JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		erosi = new JButton("Erosi");
		irten = new JButton("Irten");

		behekoPanela.add(erosi);
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
					salneurria.setText(p.getSalneurria() + " €");
					stock.setText(p.getStock() + " unitate");
				} else {
					salneurria.setText("- €");
					stock.setText("-");
				}
			}
		});

		erosi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Produktua p = (Produktua) produktua.getSelectedItem();

				if (p != null && p.getStock() > 0) {
					if (db.erosiProduktua(p.getId())) {

						p.setStock(p.getStock() - 1);
						stock.setText(p.getStock() + " unitate");
						alProduktu.add(p);

					} else {
						JOptionPane.showMessageDialog(null,
								"Errorea datu-basean eguneratzean (Agian ez dago stock errealik).", "Errorea",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (p != null && p.getStock() <= 0) {
					JOptionPane.showMessageDialog(null, "Ez dago stock.", "Errorea", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		irten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login loginLeihoa = new Login();
				loginLeihoa.setVisible(true);
			}
		});

		filtratuProduktuak();

		this.requestFocus();
		setLocationRelativeTo(null);
	}

	/**
	 * Aukeratutako kategoriaren arabera produktuak iragazten ditu.
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