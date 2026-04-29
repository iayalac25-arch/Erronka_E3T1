package eredua;

/**
 * Eskaera baten barruan dagoen produktu baten lerroa (xehetasunak) errepresentatzen duen eredua.
 */
public class EskaeraList {

	private String izena;
	private int kopuru;
	private int prezioa;

	public EskaeraList() {

	}

	/**
	 * Eskaera-lerroa hasieratzen du.
	 * * @param izena Produktuaren izena.
	 * @param kopuru Erositako unitate kopurua.
	 * @param prezioa Produktuaren unitateko prezioa.
	 */
	public EskaeraList(String izena, int kopuru, int prezioa) {

		this.izena = izena;
		this.kopuru = kopuru;
		this.prezioa = prezioa;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public int getKopuru() {
		return kopuru;
	}

	public void setKopuru(int kopuru) {
		this.kopuru = kopuru;
	}

	public int getPrezioa() {
		return prezioa;
	}

	public void setPrezioa(int prezioa) {
		this.prezioa = prezioa;
	}

	@Override
	public String toString() {
		return "EskaeraList [izena=" + izena + ", kopuru=" + kopuru + ", prezioa=" + prezioa + "]";
	}

}
