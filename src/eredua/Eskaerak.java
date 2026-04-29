package eredua;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Bezero baten eskaeraren informazioa gordetzen duen eredua (ID, data, produktuak eta prezio totala).
 */
public class Eskaerak {
	private int id;
	private Date data;
	private ArrayList<String> produktuak;
	private double guztira;

	/**
	 * Eskaera objektua hasieratzen du.
	 * * @param id Eskaeraren identifikatzailea.
	 * @param data Eskaera egin zen data.
	 * @param produktuak Eskaerak dituen produktuen izenen zerrenda.
	 * @param guztira Eskaeraren prezio totala.
	 */
	public Eskaerak(int id, Date data, ArrayList<String> produktuak, double guztira) {
		this.id = id;
		this.data = data;
		this.produktuak = produktuak;
		this.guztira = guztira;
	}

	public int getId() {
		return id;
	}

	public Date getData() {
		return data;
	}

	public ArrayList<String> getProduktuak() {
		return produktuak;
	}

	public double getGuztira() {
		return guztira;
	}

	@Override
	public String toString() {

		return data.toString();
	}
}
