package eredua;

/**
 * Saltzailearen ikuspegitik eskaera baten datu orokorrak gordetzen dituen eredua.
 */
public class Eskaria {

	private int ID;
	private String bezeroIzena;
	private String data;
	private String egoera;

	/**
	 * Eskaria hasieratzen du.
	 * * @param iD Eskaeraren identifikatzailea.
	 * @param bezeroIzena Eskaera egin duen bezeroaren izen-abizenak.
	 * @param data Eskaera egin zen data (String formatuan).
	 * @param egoera Eskaeraren uneko egoera (Prozesatzen, Bidalita...).
	 */
	public Eskaria(int iD, String bezeroIzena, String data, String egoera) {
		this.ID = iD;
		this.bezeroIzena = bezeroIzena;
		this.data = data;
		this.egoera = egoera;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getBezeroIzena() {
		return bezeroIzena;
	}

	public void setBezeroIzena(String bezeroIzena) {
		this.bezeroIzena = bezeroIzena;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEgoera() {
		return egoera;
	}

	public void setEgoera(String egoera) {
		this.egoera = egoera;
	}

	@Override
	public String toString() {
		return "Eskaria [ID=" + ID + ", bezeroIzena=" + bezeroIzena + ", data=" + data + ", egoera=" + egoera + "]";
	}
}