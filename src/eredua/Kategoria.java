package eredua;

/**
 * Produktuen kategoria bat errepresentatzen duen eredua (adibidez: Jokoak, Kontsolak).
 */
public class Kategoria {
	private int id;
	private String izena;

	/**
	 * Kategoria objektua hasieratzen du.
	 * * @param id Kategoriaren identifikatzailea.
	 * @param izena Kategoriaren izena.
	 */
	public Kategoria(int id, String izena) {
		this.id = id;
		this.izena = izena;
	}

	public int getId() {
		return id;
	}

	public String getIzena() {
		return izena;
	}

	@Override
	public String toString() {
		return izena;
	}

}
