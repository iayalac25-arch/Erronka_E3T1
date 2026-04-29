package bezero;

import javax.swing.*;

import db.DBEskaerak;
import eredua.Produktua;
import java.awt.*;
import java.util.ArrayList;

/**
 * Bezeroaren erosketa-orga (saskia) erakusten duen interfazea, ordainketa egiteko aukerarekin.
 */
public class Orga extends JFrame {

	private JButton ordainduBotoia, irtenBotoia;

	/**
	 * Orgaren leihoa sortzen du.
	 * * @param produktuZerrenda Saskian dauden produktuen zerrenda.
	 * @param bezeroId Bezeroaren identifikatzailea eskaera lotzeko.
	 */
	public Orga(ArrayList<Produktua> produktuZerrenda, int bezeroId) {

		setTitle("GameStop-Orga");
		setSize(500, 400);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu menuProduktuak = new JMenu("Erosi Produktuak");
		JMenu menuNireDatuak = new JMenu("Nire Datuak");
		JMenu menuNireEskaerak = new JMenu("Nire Eskaerak");

		menuBar.add(menuProduktuak);
		menuBar.add(menuNireDatuak);
		menuBar.add(menuNireEskaerak);
		menuBar.add(Box.createHorizontalGlue());

		setJMenuBar(menuBar);

		menuProduktuak.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new BezeroErosiProduktuakGUI(bezeroId, produktuZerrenda).setVisible(true);
			}
		});

		menuNireDatuak.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new BezeroNireDatuakGUI(bezeroId, produktuZerrenda).setVisible(true);
			}
		});

		menuNireEskaerak.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				dispose();
				new NireEskaerakGUI(null, true, bezeroId, produktuZerrenda).setVisible(true);
			}
		});

		JPanel goikoBarra = new JPanel(new BorderLayout());
		goikoBarra.setBackground(new Color(128, 0, 32));
		goikoBarra.setPreferredSize(new Dimension(100, 60));

		JLabel titulua = new JLabel("GameStop", SwingConstants.CENTER);
		titulua.setForeground(Color.WHITE);
		titulua.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel azpititulua = new JLabel("Orga", SwingConstants.CENTER);
		azpititulua.setForeground(Color.WHITE);
		azpititulua.setFont(new Font("Arial", Font.BOLD, 14));

		goikoBarra.add(titulua, BorderLayout.NORTH);
		goikoBarra.add(azpititulua, BorderLayout.CENTER);

		add(goikoBarra, BorderLayout.NORTH);

		JPanel kutxa = new JPanel(new BorderLayout());
		kutxa.setBorder(BorderFactory.createTitledBorder(""));

		JPanel zerrendaPanela = new JPanel();
		zerrendaPanela.setLayout(new BoxLayout(zerrendaPanela, BoxLayout.Y_AXIS));

		double guztira = 0;

		for (Produktua produktua : produktuZerrenda) {

			JPanel lerroa = new JPanel(new BorderLayout());
			lerroa.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			JLabel izenaLabel = new JLabel(produktua.getIzena());
			JLabel prezioaLabel = new JLabel(produktua.getSalneurria() + " €");

			JButton kenduBotoia = new JButton("x");
			kenduBotoia.setForeground(new Color(128, 0, 32));

			JPanel eskuina = new JPanel();
			eskuina.setLayout(new BoxLayout(eskuina, BoxLayout.X_AXIS));

			prezioaLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
			kenduBotoia.setAlignmentY(Component.CENTER_ALIGNMENT);

			eskuina.add(prezioaLabel);
			eskuina.add(Box.createHorizontalStrut(10));
			eskuina.add(kenduBotoia);

			lerroa.add(izenaLabel, BorderLayout.WEST);
			lerroa.add(eskuina, BorderLayout.EAST);

			zerrendaPanela.add(lerroa);

			guztira += Double.valueOf(produktua.getSalneurria());

			kenduBotoia.addActionListener(e -> {
				produktuZerrenda.remove(produktua);
				dispose();
				new Orga(produktuZerrenda, bezeroId).setVisible(true);
			});
		}

		JScrollPane scroll = new JScrollPane(zerrendaPanela);
		kutxa.add(scroll, BorderLayout.CENTER);

		JLabel guztiraLabel = new JLabel("Guztira: " + String.format("%.2f", guztira) + " €");
		guztiraLabel.setForeground(new Color(128, 0, 32));
		guztiraLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		guztiraLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		kutxa.add(guztiraLabel, BorderLayout.SOUTH);

		add(kutxa, BorderLayout.CENTER);

		JPanel behekoPanela = new JPanel();

		ordainduBotoia = new JButton("Ordaindu");
		ordainduBotoia.setBackground(new Color(128, 0, 32));
		ordainduBotoia.setForeground(Color.WHITE);

		irtenBotoia = new JButton("Irten");

		behekoPanela.add(ordainduBotoia);
		behekoPanela.add(irtenBotoia);

		add(behekoPanela, BorderLayout.SOUTH);

		irtenBotoia.addActionListener(e -> {
			dispose();
			new BezeroErosiProduktuakGUI(bezeroId, produktuZerrenda).setVisible(true);
		});

		ordainduBotoia.addActionListener(e -> {
			if (produktuZerrenda.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Orga hutsik dago.");
				return;
			}

			DBEskaerak dbEsk = new DBEskaerak();

			if (dbEsk.sartuEskaria(bezeroId, produktuZerrenda)) {
				JOptionPane.showMessageDialog(this, "Eskaera ondo burutu da! Eskerrik asko.");
				produktuZerrenda.clear();

				dispose();
				Orga orgaBerria = new Orga(produktuZerrenda, bezeroId);
				orgaBerria.toFront();
				orgaBerria.setVisible(true);

			} else {
				JOptionPane.showMessageDialog(this, "Errorea eskaera prozesatzean.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}