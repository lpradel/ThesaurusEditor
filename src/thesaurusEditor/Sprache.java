package thesaurusEditor;

import java.io.Serializable;

/**
 * Diese Klasse stellt die verschiedenen Sprachen ,in der die Konzepte übersetzt werden.
 * @author sopr057
 *
 */
public class Sprache implements Serializable{

	private String kuerzel;
	private String sprache;
	
	public Sprache(String kuerzel,String sprache){
		this.kuerzel = kuerzel;
		this.sprache = sprache;
	}
	/**
	 * Setze das entsprechende Kürzel.
	 * @param kuerzel 
	 */
	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}
	
	/**
	 * Gibt das Kürzel aus.
	 * @return das entsprechende Kürzel
	 */
	public String getKuerzel() {
		return this.kuerzel;
	}
	
	/**
	 * Setze die Sprache
	 * @param sprache
	 */
	public void setSprache(String sprache) {
		this.sprache = sprache;
	}
	
	/**
	 * Gibt die Sprache aus.
	 * @return Die entsprechende Sprache
	 */
	public String getSprache() {
		return this.sprache;
	}
	
	/**
	 * Überprüft, ob die Sprache dem übergebene Objekt gleicht. 
	 * @param o Das Objekt, das mit der Sprache vergleichen wird.
	 * @return Gibt true aus, wenn die Sprache und das Objekt übereinstimmen, sonst false.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Sprache && ((Sprache) o).kuerzel.equals(this.kuerzel)
				&& ((Sprache) o).sprache.equals(this.sprache)) {
			return true;
		} else
			return false;
	}

        @Override
        public String toString(){
            return this.sprache+" - "+this.kuerzel;
        }
	
}
