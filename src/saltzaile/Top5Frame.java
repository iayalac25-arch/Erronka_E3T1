package saltzaile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;

/**
 * TOP5 estatistiken emaitzak formatu garbi batean bistaratzen dituen leiho txikia.
 */
public class Top5Frame extends JFrame {

	/**
	 * Estatistikak bistaratzeko leihoa sortzen du.
	 * * @param izenburua Leihoaren goiko aldean erakutsiko den testua.
	 * @param datuak Pantailaratuko diren datuen (top 5 emaitzen) zerrenda.
	 */
	public Top5Frame(String izenburua, ArrayList<String> datuak) {

		setTitle(izenburua);
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);

		JPanel goiburua = new JPanel();
		goiburua.setBackground(new Color(204, 0, 0));
		goiburua.setPreferredSize(new Dimension(400, 40));
		goiburua.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel lblGameStop = new JLabel("GameStop");
		lblGameStop.setForeground(Color.WHITE);
		lblGameStop.setFont(new Font("Arial", Font.BOLD, 16));
		goiburua.add(lblGameStop);
		add(goiburua, BorderLayout.NORTH);

		JPanel gorputza = new JPanel();
		gorputza.setLayout(null);
		gorputza.setBackground(Color.WHITE);

		JLabel lblIzenburua = new JLabel(izenburua, SwingConstants.CENTER);
		lblIzenburua.setBounds(50, 20, 300, 30);
		lblIzenburua.setFont(new Font("Arial", Font.BOLD, 18));
		gorputza.add(lblIzenburua);

		for (int i = 0; i < 5; i++) {
			int yPos = 80 + (i * 45);

			JLabel lblTop = new JLabel("Top " + (i + 1));
			lblTop.setBounds(40, yPos, 60, 25);
			lblTop.setFont(new Font("Arial", Font.PLAIN, 14));
			gorputza.add(lblTop);

			JTextField txtDatu = new JTextField();
			txtDatu.setBounds(120, yPos, 220, 30);
			txtDatu.setEditable(false);
			txtDatu.setBackground(Color.WHITE);
			txtDatu.setBorder(BorderFactory.createLineBorder(Color.GRAY));

			if (i < datuak.size()) {
				txtDatu.setText(datuak.get(i));
			}

			gorputza.add(txtDatu);
		}

		JButton btnIrten = new JButton("Irten");
		btnIrten.setBounds(150, 350, 100, 40);
		btnIrten.setBackground(new Color(200, 200, 200));
		btnIrten.setFocusPainted(false);

		btnIrten.addActionListener(e -> dispose());

		gorputza.add(btnIrten);

		add(gorputza, BorderLayout.CENTER);
	}
}
