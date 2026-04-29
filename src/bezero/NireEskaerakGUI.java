package bezero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.util.ArrayList;
import db.DBEskaerak;
import eredua.Eskaerak;
import eredua.Produktua;

/**
 * Bezero baten eskaeren zerrenda eta xehetasunak erakusten dituen leihoa.
 */
public class NireEskaerakGUI extends JDialog {

	private JComboBox<Eskaerak> datakCB;
	private JTextField dataTestu, guztiraTestu;
	private JTextArea produktuakTestu;
	private DBEskaerak dbEskaerak;
	private JButton botoiaUtzi, botoiaNireGastua;
	private int logeautakoBezeroId;
	private ArrayList<Eskaerak> eskaeraGuztiak;
	private ArrayList<Produktua> alProduktu;

	/**
	 * Eskaeren leihoa hasieratzen du.
	 * @param parent Guraso-leihoa.
	 * @param modal Modalitatea.
	 * @param idBezeroa Bezeroaren identifikatzailea.
	 * @param saskia Erosketa-saskia.
	 */
	public NireEskaerakGUI(JFrame parent, boolean modal, int idBezeroa, ArrayList<Produktua> saskia) {
		super(parent, "Nire Eskaerak", modal);

		this.logeautakoBezeroId = idBezeroa;
		this.dbEskaerak = new DBEskaerak();
		this.alProduktu = (saskia != null) ? saskia : new ArrayList<Produktua>();

		setSize(450, 400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		JPanel panelNagusia = new JPanel(new BorderLayout(10, 10));
		panelNagusia.setBorder(new EmptyBorder(20, 20, 20, 20));

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

		menuProduktuak.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new BezeroErosiProduktuakGUI(logeautakoBezeroId, alProduktu).setVisible(true);
			}
		});

		menuNireDatuak.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new BezeroNireDatuakGUI(logeautakoBezeroId, alProduktu).setVisible(true);
			}
		});

		menuOrga.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new Orga(alProduktu, logeautakoBezeroId).setVisible(true);
			}
		});

		JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
		goikoPanela.add(new JLabel("Hautatu eskaera data:"));
		datakCB = new JComboBox<>();
		goikoPanela.add(datakCB);
		panelNagusia.add(goikoPanela, BorderLayout.NORTH);

		JPanel erdikoPanela = new JPanel(new BorderLayout(10, 10));
		erdikoPanela.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Eskaeraren Xehetasunak"),
				new EmptyBorder(10, 10, 10, 10)));

		JPanel dataGuztiraPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		dataTestu = new JTextField();
		dataTestu.setEditable(false);
		guztiraTestu = new JTextField();
		guztiraTestu.setEditable(false);
		dataGuztiraPanel.add(new JLabel("Eskaera Data:"));
		dataGuztiraPanel.add(dataTestu);
		dataGuztiraPanel.add(new JLabel("Guztira:"));
		dataGuztiraPanel.add(guztiraTestu);

		JPanel produktuPanel = new JPanel(new BorderLayout(5, 5));
		produktuakTestu = new JTextArea();
		produktuakTestu.setEditable(false);
		JScrollPane scroll = new JScrollPane(produktuakTestu);
		produktuPanel.add(new JLabel("Produktuak:"), BorderLayout.NORTH);
		produktuPanel.add(scroll, BorderLayout.CENTER);

		erdikoPanela.add(dataGuztiraPanel, BorderLayout.NORTH);
		erdikoPanela.add(produktuPanel, BorderLayout.CENTER);

		panelNagusia.add(erdikoPanela, BorderLayout.CENTER);

		JPanel behekoPanela = new JPanel(new FlowLayout());
		botoiaNireGastua = new JButton("Gastu Totala");
		botoiaUtzi = new JButton("Utzi");
		
		behekoPanela.add(botoiaNireGastua);
		behekoPanela.add(botoiaUtzi);
		panelNagusia.add(behekoPanela, BorderLayout.SOUTH);

		add(panelNagusia);

		kargatuEskaerak();

		botoiaNireGastua.addActionListener(e -> {
			double gastua = dbEskaerak.kalkulatuBezeroarenGastua(logeautakoBezeroId);
			
			JOptionPane.showMessageDialog(this, 
				"Dendan guztira gastatu duzun dirua: " + String.format("%.2f", gastua) + " €", 
				"Nire Gastu Historikoa", JOptionPane.INFORMATION_MESSAGE);
		});

		datakCB.addActionListener(e -> eguneratuXehetasunak());
		botoiaUtzi.addActionListener(e -> {
			dispose();
			BezeroErosiProduktuakGUI leihoa = new BezeroErosiProduktuakGUI(logeautakoBezeroId, alProduktu);
			leihoa.setVisible(true);
		});
	}

	/**
	 * Hautatutako eskaeraren xehetasunak testu-eremuetan eguneratzen ditu.
	 */
	private void eguneratuXehetasunak() {
		Eskaerak hautatutakoEskaria = (Eskaerak) datakCB.getSelectedItem();
		if (hautatutakoEskaria != null) {
			dataTestu.setText(hautatutakoEskaria.getData().toString());

			String produktuak = String.join("\n", hautatutakoEskaria.getProduktuak());
			produktuakTestu.setText(produktuak);

			guztiraTestu.setText(String.format("%.2f €", hautatutakoEskaria.getGuztira()));

		} else {
			dataTestu.setText("");
			produktuakTestu.setText("");
			guztiraTestu.setText("");
		}

	}

	/**
	 * Datu-basetik bezeroaren eskaerak lortu eta zabaltzen den zerrendan (ComboBox) kargatzen ditu.
	 */
	private void kargatuEskaerak() {
		datakCB.removeAllItems();
		eskaeraGuztiak = dbEskaerak.lortuEskaerakBezeroarentzat(logeautakoBezeroId);
		if (eskaeraGuztiak == null || eskaeraGuztiak.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ez duzu eskaerarik egin.", "Informazioa",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			for (Eskaerak e : eskaeraGuztiak) {
				datakCB.addItem(e);
			}
			datakCB.setSelectedIndex(0);
			eguneratuXehetasunak();
		}
	}

}