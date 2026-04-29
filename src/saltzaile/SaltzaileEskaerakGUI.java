package saltzaile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import db.DBSaltzaileEskaerak;
import eredua.Eskaria;
import eredua.EskaeraList;

/**
 * Saltzaileek sistemako eskaera guztiak kudeatzeko eta estatistikak bistaratzeko interfazea.
 */
public class SaltzaileEskaerakGUI extends JFrame {

	private JTable goikoTaula;
	private JTable behekoTaula;
	private DBSaltzaileEskaerak se;

	/**
	 * Eskaeren kudeaketa-leihoa sortzen du.
	 */
	public SaltzaileEskaerakGUI() {

		se = new DBSaltzaileEskaerak();

		setTitle("Saltzaile-Eskaerak");
		setSize(700, 450);
		setLayout(new BorderLayout(10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		DefaultTableModel modelo1 = new DefaultTableModel(new Object[] { "ID", "Bezero izena", "Data", "Egoera" }, 0) {
		};

		goikoTaula = new JTable(modelo1);
		goikoTaula.setRowHeight(25);

		JScrollPane scroll1 = new JScrollPane(goikoTaula);

		DefaultTableModel modelo2 = new DefaultTableModel(
				new Object[] { "Produktua", "Kantitatea", "Prezioa", "Guztira" }, 0);

		behekoTaula = new JTable(modelo2);
		behekoTaula.setRowHeight(25);

		JScrollPane scroll2 = new JScrollPane(behekoTaula);

		scroll1.setPreferredSize(new Dimension(400, 70));
		scroll2.setPreferredSize(new Dimension(400, 120));

		scroll1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		scroll2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		JPanel erdiPan = new JPanel(new GridLayout(2, 1, 10, 10));
		erdiPan.add(scroll1);
		erdiPan.add(scroll2);

		JPanel panBotoi = new JPanel();

		JButton b1 = new JButton("Top Produktu");
		JButton b2 = new JButton("Produktu txarra");
		JButton b3 = new JButton("Top saltzaile");
		JButton b4 = new JButton("Irten");

		panBotoi.add(b1);
		panBotoi.add(b2);
		panBotoi.add(b3);
		panBotoi.add(b4);

		b1.addActionListener(e -> {
			ArrayList<String> datuak = se.lortuTop5("ProduktuTop5");
			new Top5Frame("Produktu onenak", datuak).setVisible(true);
		});

		b2.addActionListener(e -> {
			ArrayList<String> datuak = se.lortuTop5("TxarProduktuTop5");
			new Top5Frame("Produkturik txarrenak", datuak).setVisible(true);
		});

		b3.addActionListener(e -> {
			ArrayList<String> datuak = se.lortuTop5("SaltzaileTop5");
			new Top5Frame("Top saltzaileak", datuak).setVisible(true);
		});

		b4.addActionListener(e -> {
			dispose();
			new SaltzaileMenuGUI().setVisible(true);
		});

		goikoTaula.getSelectionModel().addListSelectionListener(e -> {

			if (!e.getValueIsAdjusting()) {

				int row = goikoTaula.getSelectedRow();

				if (row != -1) {

					int id = Integer.parseInt(goikoTaula.getValueAt(row, 0).toString());

					kargatuBehekoTaula(se.eskatutakoProduktuak(id));
				}
			}
		});

		kargatuGoikoTaula(se.eskariGuztiak());

		add(erdiPan, BorderLayout.CENTER);
		add(panBotoi, BorderLayout.SOUTH);
	}

	/**
	 * Data baten testu-formatua txukuntzen du pantailan erakusteko.
	 * * @param data Jatorrizko data testu gisa.
	 * @return Data formatu garbian, ordu barik.
	 */
	private String formateatuData(String data) {
		if (data == null || data.length() < 10)
			return "";
		return data.substring(0, 10).replace("-", "/");
	}

	/**
	 * Goiko taulan eskaera orokorren zerrenda kargatzen du.
	 * * @param lista Erakutsiko diren eskaeren zerrenda.
	 */
	private void kargatuGoikoTaula(ArrayList<Eskaria> lista) {

		DefaultTableModel model = (DefaultTableModel) goikoTaula.getModel();
		model.setRowCount(0);

		for (Eskaria e : lista) {
			model.addRow(new Object[] { e.getID(), e.getBezeroIzena(), formateatuData(e.getData()), e.getEgoera() });
		}
	}

	/**
	 * Beheko taulan hautatutako eskaeraren lerroak (produktuak) kargatzen ditu.
	 * * @param lista Erakutsiko diren eskaera-lerroen zerrenda.
	 */
	private void kargatuBehekoTaula(ArrayList<EskaeraList> lista) {

		DefaultTableModel model = (DefaultTableModel) behekoTaula.getModel();
		model.setRowCount(0);

		for (EskaeraList el : lista) {
			model.addRow(
					new Object[] { el.getIzena(), el.getKopuru(), el.getPrezioa(), el.getKopuru() * el.getPrezioa() });
		}
	}

}