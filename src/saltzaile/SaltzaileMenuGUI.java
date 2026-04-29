package saltzaile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import login.Login;

/**
 * Saltzailearen menü nagusia, bertatik produktuen, langileen eta eskaeren kudeaketara joateko.
 */
public class SaltzaileMenuGUI extends JFrame {

	/**
	 * Saltzailearen menu leihoa sortzen du eta bertako botoiak lotzen ditu.
	 */
	public SaltzaileMenuGUI() {
		// Leihoaren ezarpen nagusiak
		setTitle("Gamestop - Saltzailea");
		setSize(600, 300);
		setLayout(new BorderLayout(15, 15));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Goiko izenburua
		JLabel izenburua = new JLabel("Saltzaile Menua", SwingConstants.CENTER);
		izenburua.setFont(new Font("", Font.BOLD, 20));
		izenburua.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		add(izenburua, BorderLayout.NORTH);

		// Erdiko botoi nagusiak
		JPanel botoiak = new JPanel();
		botoiak.setLayout(new GridLayout(1, 3, 20, 0));
		botoiak.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		JButton produktuak = new JButton("Produktuak");
		JButton eskaerak = new JButton("Eskaerak");
		JButton langileak = new JButton("Langileak");

		botoiak.add(produktuak);
		botoiak.add(eskaerak);
		botoiak.add(langileak);

		add(botoiak, BorderLayout.CENTER);

		// Irten botoia
		JPanel irten = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton irtenBotoia = new JButton("Irten");
		irtenBotoia.setPreferredSize(new Dimension(80, 30));
		irten.add(irtenBotoia);
		add(irten, BorderLayout.SOUTH);

		produktuak.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				SaltzaileProduktuakGUI kudeaketaLeihoa = new SaltzaileProduktuakGUI();
				kudeaketaLeihoa.setVisible(true);
			}
		});

		langileak.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				SaltzaileLangileakGUI kudeaketaLeihoa = new SaltzaileLangileakGUI();
				kudeaketaLeihoa.setVisible(true);
			}
		});

		eskaerak.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();	
				SaltzaileEskaerakGUI kudeaketaLeihoa = new SaltzaileEskaerakGUI();
				kudeaketaLeihoa.setVisible(true);
			}
		});

		irtenBotoia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Login kudeaketaLeihoa = new Login();
				kudeaketaLeihoa.setVisible(true);
				dispose();
			}
		});

		setLocationRelativeTo(null);
	}

}