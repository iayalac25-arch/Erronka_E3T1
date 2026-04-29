package eredua;

/**
 * Langile edo saltzaile baten informazio pertsonala eta laborala gordetzen duen eredua.
 */
public class Langilea {

	private int id;
	private String izena;
	private String abizena;
	private String emaila;
	private String telefonoa;
	private String kData;
	private String nagusia;
	private String pasahitza;

	/**
	 * Langile bat datu-basetik irakurtzeko eraikitzailea.
	 * * @param id Langilearen identifikatzailea.
	 * @param izena Langilearen izena.
	 * @param abizena Langilearen abizena.
	 * @param emaila Langilearen posta elektronikoa.
	 * @param telefonoa Langilearen telefono zenbakia.
	 * @param kData Kontratazio data (String formatuan).
	 * @param nagusia Bere nagusiaren izena.
	 */
	public Langilea(int id, String izena, String abizena, String emaila, String telefonoa, String kData,
			String nagusia) {
		this.id = id;
		this.izena = izena;
		this.abizena = abizena;
		this.emaila = emaila;
		this.telefonoa = telefonoa;
		this.kData = kData;
		this.nagusia = nagusia;
	}

	/**
	 * Langile berri bat sortzeko eraikitzailea (pasahitza barne).
	 * * @param id Langilearen identifikatzailea.
	 * @param izena Langilearen izena.
	 * @param abizena Langilearen abizena.
	 * @param emaila Langilearen posta elektronikoa.
	 * @param telefonoa Langilearen telefono zenbakia.
	 * @param kData Kontratazio data.
	 * @param nagusia Bere nagusiaren izena.
	 * @param pasahitza Sistemara sartzeko pasahitza.
	 */
	public Langilea(int id, String izena, String abizena, String emaila, String telefonoa, String kData, String nagusia,
			String pasahitza) {
		this(id, izena, abizena, emaila, telefonoa, kData, nagusia); // Llama al de arriba
		this.pasahitza = pasahitza;
	}

	public int getId() {
		return id;
	}

	public String getIzena() {
		return izena;
	}

	public String getAbizena() {
		return abizena;
	}

	public String getEmaila() {
		return emaila;
	}

	public String getTelefonoa() {
		return telefonoa;
	}

	public String getkData() {
		return kData;
	}

	public String getNagusia() {
		return nagusia;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public void setAbizena(String abizena) {
		this.abizena = abizena;
	}

	public void setEmaila(String emaila) {
		this.emaila = emaila;
	}

	public void setTelefonoa(String telefonoa) {
		this.telefonoa = telefonoa;
	}

	public void setkData(String kData) {
		this.kData = kData;
	}

	public void setNagusia(String nagusia) {
		this.nagusia = nagusia;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	@Override
	public String toString() {
		return id + " " + izena + " " + abizena;
	}

}
