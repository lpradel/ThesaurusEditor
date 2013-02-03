package thesaurusEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stellt ein Schema bereit und verbindet dieses mit den Top-Konzepten
 * und der URI
 */
public class Schema implements Serializable{

	private String name;
	private URI lnkURI;
	
	private List<Konzept> lnkTopKonzepte;
	
	/**
	 * Konstruktor der Schema Klasse, sollte alle vorbereitenden
	 * Dinge fuer die andern Funktionen tun
	 * @param name Namen des Schemas
	 */
	public Schema(String name, URI uri) {
		this.lnkTopKonzepte = new ArrayList<Konzept>();
		this.name = name;
		this.lnkURI = uri;
	}

	/**
	 * Setzt die neue URI fuer das Schema
	 * @param lnkURI neue URI
	 */
	public void setURI(URI lnkURI) {
		this.lnkURI = lnkURI;
	}

	/**
	 * Gibt die URI des Schemas zurueck
	 * @return URI des Schemas
	 */
	public URI getURI() {
		return this.lnkURI;
	}

	/**
	 * Fuegt dem Schema ein TopKonzept hinzu
	 * @param topKonzept
	 */
	public void addTopKonzept(Konzept topKonzept) {
		if ( ! lnkTopKonzepte.contains(topKonzept) )
		{
			this.lnkTopKonzepte.add(topKonzept);
		}
	}

	/**
	 * Loescht ein bestimmtes TopKonzept aus der Liste
	 * der TopKonzepte
	 * @param topKonzept
	 */
	public void removeTopKonzept(Konzept topKonzept) {
		this.lnkTopKonzepte.remove(topKonzept);
	}

	/**
	 * Gibt eine Liste mit allen Top-Konzepten des
	 * Schemas zurueck
	 * @return Liste mit Konzepten
	 */
	public List<Konzept> getTopKonzepte() {
		return this.lnkTopKonzepte;
	}

	/**
	 * Gibt den Namen des Schemas zurueck
	 * @return Namen
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setzt den Namen des Schemas
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

        @Override
        public String toString(){
            return this.name;
        }

        @Override
        public boolean equals(Object other){
            if (other instanceof Schema){
            return this.getURI().equals(((Schema)other).getURI());
            }
            else return false;
        }

}
