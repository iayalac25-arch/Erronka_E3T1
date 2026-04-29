package eredua;

/**
 * Denda bateko produktu bat errepresentatzen duen eredua (stock-a, prezioa, kategoria...).
 */
public class Produktua {
	private int id;
	private String izena;
	private String salneurria;
	private int idKategoria;
	private int stock;
	private int idBiltegi;

	/**
	 * Produktu oso bat datu-basetik irakurtzeko eraikitzailea.
	 * * @param id Produktuaren identifikatzailea.
	 * @param izena Produktuaren izena.
	 * @param salneurria Produktuaren prezioa (String gisa).
	 * @param idKategoria Kategoriaren ID-a.
	 * @param stock Inbentarioan dagoen unitate kopurua.
	 * @param idBiltegi Biltegiaren ID-a.
	 */
	public Produktua(int id, String izena, String salneurria, int idKategoria, int stock, int idBiltegi) {
		this.id = id;
		this.izena = izena;
		this.salneurria = salneurria;
		this.idKategoria = idKategoria;
		this.stock = stock;
		this.idBiltegi = idBiltegi;
	}

	public int getId() {
		return id;
	}

	public String getIzena() {
		return izena;
	}

	public String getSalneurria() {
		return salneurria;
	}

	public int getIdKategoria() {
		return idKategoria;
	}

	public int getStock() {
		return stock;
	}

	public void setSalneurria(String salneurria) {
		this.salneurria = salneurria;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getidBiltegi() {
		return idBiltegi;
	}

	@Override
	public String toString() {
		return izena;
	}
}
