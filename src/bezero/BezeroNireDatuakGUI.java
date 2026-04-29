package bezero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import db.DBBezero;
import eredua.Produktua;

import java.awt.*;
import java.util.ArrayList;

/**
 * Bezeroaren datu pertsonalak bistaratzeko eta eguneratzeko interfaze grafikoa.
 */
public class BezeroNireDatuakGUI extends JFrame {

	private JTextField txtIzena, txtAbizena, txtHelbidea, txtEmaila;
	private JButton botoiaPasahitza, botoiaEguneratu, botoiaUtzi;
	private int logeautakoBezeroId;
	private ArrayList<Produktua> alProduktu;

	/**
	 * Datuen leihoa hasieratzen du. * @param bezeroId Logeatutako bezeroaren
	 * identifikatzailea.
	 * 
	 * @param saskia Erosketa-saskiaren egoera mantentzeko.
	 */
	public BezeroNireDatuakGUI(int bezeroId, ArrayList<Produktua> saskia) {

		this.logeautakoBezeroId = bezeroId;
		this.alProduktu = saskia;
		DBBezero db = new DBBezero();

		setTitle("GameStop-Bezeroa");
		setSize(450, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

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
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dispose();
				new BezeroErosiProduktuakGUI(logeautakoBezeroId, alProduktu).setVisible(true);
			}
		});

		menuNireEskaerak.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dispose();
				new NireEskaerakGUI(BezeroNireDatuakGUI.this, true, logeautakoBezeroId, alProduktu).setVisible(true);
			}
		});

		// ACCESO A ORGA AÑADIDO
		menuOrga.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dispose();
				new Orga(alProduktu, logeautakoBezeroId).setVisible(true);
			}
		});

		JPanel PanelNagusia = new JPanel(new BorderLayout(10, 10));
		PanelNagusia.setBorder(new EmptyBorder(20, 20, 20, 20));
		JPanel DatuenPanela = new JPanel(new GridLayout(5, 2, 10, 10));

		DatuenPanela.add(new JLabel("Izena:"));
		txtIzena = new JTextField();
		DatuenPanela.add(txtIzena);

		DatuenPanela.add(new JLabel("Abizena:"));
		txtAbizena = new JTextField();
		DatuenPanela.add(txtAbizena);

		DatuenPanela.add(new JLabel("Helbidea:"));
		txtHelbidea = new JTextField();
		DatuenPanela.add(txtHelbidea);

		DatuenPanela.add(new JLabel("Emaila:"));
		txtEmaila = new JTextField();
		DatuenPanela.add(txtEmaila);

		DatuenPanela.add(new JLabel(""));
		botoiaPasahitza = new JButton("Pasahitza eguneratu");
		DatuenPanela.add(botoiaPasahitza);

		String[] nireDatuak = db.lortuDatuGuztiakIdBidez(this.logeautakoBezeroId);
		txtIzena.setText(nireDatuak[0]);
		txtAbizena.setText(nireDatuak[1]);
		txtHelbidea.setText(nireDatuak[2]);
		txtEmaila.setText(nireDatuak[3]);

		JPanel pnlBehea = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		botoiaEguneratu = new JButton("Eguneratu");
		botoiaUtzi = new JButton("Utzi");
		pnlBehea.add(botoiaEguneratu);
		pnlBehea.add(botoiaUtzi);

		PanelNagusia.add(DatuenPanela, BorderLayout.CENTER);
		PanelNagusia.add(pnlBehea, BorderLayout.SOUTH);

		add(PanelNagusia);

		botoiaEguneratu.addActionListener(e -> {
			String izenBerria = txtIzena.getText().trim();
			String abizenBerria = txtAbizena.getText().trim();
			String helbideBerria = txtHelbidea.getText().trim();
			String emailBerria = txtEmaila.getText().trim();

			if (izenBerria.isEmpty() || abizenBerria.isEmpty() || helbideBerria.isEmpty() || emailBerria.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Eremu guztiak bete behar dira.", "Abisua",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			DBBezero dbBezero = new DBBezero();
			boolean ondo = dbBezero.eguneratuBezeroDatuak(logeautakoBezeroId, izenBerria, abizenBerria, helbideBerria,
					emailBerria);

			if (ondo) {
				JOptionPane.showMessageDialog(this, "Zure datuak ondo eguneratu dira!", "Eguneratua",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Errorea datuak gordetzean.", "Errorea", JOptionPane.ERROR_MESSAGE);
			}
		});

		botoiaUtzi.addActionListener(e -> {
			dispose();
			new BezeroErosiProduktuakGUI(logeautakoBezeroId, alProduktu).setVisible(true);
		});

		botoiaPasahitza.addActionListener(e -> {
			new PasahitzBerria(this, logeautakoBezeroId).setVisible(true);
		});

		setLocationRelativeTo(null);
	}

}