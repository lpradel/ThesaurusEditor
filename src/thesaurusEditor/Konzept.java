package thesaurusEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Buendelt alle benoetigten Sprachrepraesentationen, Verknuepfungen zu anderen
 * Konzepten und Schemata und die URI.
 * 
 * @author sopr052
 * 
 */
public class Konzept implements Serializable {

	private URI lnkURI;
	/**
	 * @supplierCardinality *
	 */
	private ArrayList<Konzept> lnkGeneralisierungen;
	/**
	 * @supplierCardinality *
	 */
	private volatile int hashCode = 0;

	private ArrayList<Konzept> lnkSpezialisierungen;
	/**
	 * @supplierCardinality *
	 */
	private ArrayList<Sprachrepraesentation> lnkSprachrepraesentationen;
	/**
	 * @supplierCardinality *
	 */
	private ArrayList<Schema> lnkSchemata;

	/**
	 * Erstellt ein neues Konzept, welches noch keine Informationen beinhaltet,
	 * ausser den zwingend benoetigten bevorzugten Bezeichner in der
	 * festgelegten Hauptsprache.
	 * 
	 * @param bezeichnung
	 *            Bevorzugter Bezeichner der gesetzt werden soll
	 * @param hauptsprache
	 *            Hauptsprache das Thesaurus, in dem die Bezeichnung
	 *            abgespeichert wird
	 */
	public Konzept(String bezeichnung, Sprache hauptsprache, URI uri) {
		this.lnkGeneralisierungen = new ArrayList<Konzept>();
		this.lnkSpezialisierungen = new ArrayList<Konzept>();
		this.lnkSchemata = new ArrayList<Schema>();
		this.lnkSprachrepraesentationen = new ArrayList<Sprachrepraesentation>();
		Sprachrepraesentation neu = new Sprachrepraesentation(hauptsprache,
				bezeichnung, BezeichnungsTyp.bevorzugt);
		this.lnkSprachrepraesentationen.add(neu);
		this.lnkURI = uri;
	}

	/**
	 * Gibt an, ob das Konzept zu einer gegebenen Sprache einen bevorzugten
	 * Bezeichner hat.
	 * 
	 * @param s
	 *            Gewuenschte Sprache, nach der gesucht werden soll
	 * @return true, falls bev. Bezeichner fuer die Sprache vorhanden, sonst
	 *         false
	 */
	public boolean hatBevBezeichnung(Sprache s) {
		Sprachrepraesentation gesucht = getSprachrepraesentation(s);
		if (gesucht == null) {
			return false;
		}
		return !(gesucht.getBevorzugteBezeichnung().isEmpty());
	}

	/**
	 * Entfernt einen bevorzugten Bezeichner einer bestimmten Sprache aus einem
	 * Konzept
	 * 
	 * @param s
	 *            Sprache, aus der der bevorzugte Bezeichner geloescht werden
	 *            osll
	 */
	public void removeBezeichnung(String wort, Sprache s,
			BezeichnungsTyp kategorie) {
		Sprachrepraesentation gesucht = this.getSprachrepraesentation(s);
		if (gesucht == null) {
			return;
		}
		switch (kategorie) {
		case bevorzugt:
			gesucht.setBevorzugteBezeichnung(null);
			break;
		case alternativ:
			gesucht.removeAltBezeichnung(wort);
			break;
		case versteckt:
			gesucht.removeVersteckteBezeichnung(wort);
			break;
		}
	}

	public void removeBezeichnungen() {
		for (Sprachrepraesentation s : this.getSprachrepraesentationen()) {
			s.removeBezeichnungen();
		}
	}

	/**
	 * Fuegt dem Konzept einen Bezeichner (Wort) hinzu.
	 * 
	 * @param wort
	 *            Das hinzuzufuegende Wort, welches das Konzept beschreibt.
	 * @param s
	 *            Die Sprache, zu der das Wort gehoert.
	 * @param kategorie
	 *            Die Art des Bezeichners (bevorzugt, alternativ, versteckt)
	 */
	public void addBezeichnung(String wort, Sprache s, BezeichnungsTyp kategorie) {
		Sprachrepraesentation gesucht = this.getSprachrepraesentation(s);
		if (gesucht == null) {
			this.addSprachrepraesentation(s);
			gesucht = this.getSprachrepraesentation(s);
		}
		switch (kategorie) {
		case bevorzugt:
			gesucht.setBevorzugteBezeichnung(wort);
			break;
		case alternativ:
			if (!gesucht.getAltBezeichnungen().contains(wort)) {
				gesucht.addAltBezeichnung(wort);
			}
			break;
		case versteckt:
			if (!gesucht.getVersteckteBezeichnungen().contains(wort)) {
				gesucht.addVersteckteBezeichnung(wort);
			}
			break;
		case beispiel:
			gesucht.setBeispiel(wort);
			break;
		case beschreibung:
			gesucht.setBeschreibung(wort);
			break;
		case definition:
			gesucht.setDefinition(wort);
			break;
		}
	}

	/**
	 * Prueft, ob das Konzept einen Bezeichner namens "wort" hat
	 * 
	 * @param s
	 *            Die Sprache, in der das Wort gesucht werden soll
	 * @param wort
	 *            Das zu suchende Wort
	 * @return Gibt "kein" zurueck, falls das Wort nicht im Konzept enthalten
	 *         ist, sonst den Typen des Bezeichners, der gefunden wurde
	 */
	public BezeichnungsTyp enthaeltWort(Sprache s, String wort) {
		Sprachrepraesentation gesucht = this.getSprachrepraesentation(s);
		if (gesucht == null) {
			return BezeichnungsTyp.kein;
		}
		return gesucht.enthaeltWort(wort);
	}

	/**
	 * Fuegt dem Konzept eine neue Sprachrepraesentation hinzu
	 * 
	 * @param s
	 *            Definiert die benoetigte Sprache, in der das Konzept
	 *            verfuegbar sein soll
	 */
	public void addSprachrepraesentation(Sprache s) {
		if (getSprachrepraesentation(s) == null) {
			Sprachrepraesentation neu = new Sprachrepraesentation(s);
			this.lnkSprachrepraesentationen.add(neu);
		}
	}

	/**
	 * Gibt die Sprachrepraesentation zu einer bestimmten Sprache zurueck, die
	 * alle Bezeichner fuer diese Sprache enthaelt.
	 * 
	 * @param s
	 *            Gewuenschte Sprache
	 * @return Die jeweilige Sprachrepraesentation des Konzepts, falls keine
	 *         gefunden wurde: null
	 */
	public Sprachrepraesentation getSprachrepraesentation(Sprache s) {
		boolean gefunden = false;
		Sprachrepraesentation gesucht = null;
		for (int i = 0; !gefunden && i < this.lnkSprachrepraesentationen.size(); i++) {
			gesucht = this.lnkSprachrepraesentationen.get(i);
			gefunden = gesucht.getSprache().equals(s);
		}
		if (gefunden) {
			return gesucht;
		}
		return null;
	}

	/**
	 * Gibt die komplette Liste der Sprachrepraesentationen zurueck.
	 * 
	 * @return
	 */
	public List<Sprachrepraesentation> getSprachrepraesentationen() {
		return this.lnkSprachrepraesentationen;
	}

	/**
	 * Loescht die Sprachrepraesentation der angegebenen Sprache aus der Liste
	 * der Sprachrepraesentationen des Konzepts
	 * 
	 * @param s
	 */
	public void removeSprachrepraesentation(Sprache s) {
		Sprachrepraesentation gesucht = this.getSprachrepraesentation(s);
		if (gesucht != null) {
			this.lnkSprachrepraesentationen.remove(gesucht);
		}
	}

	/**
	 * Setzt die URI fuer dieses Konzept.
	 * 
	 * @param lnkURI
	 *            zu setzende Uri
	 */
	public void setURI(URI lnkURI) {
		this.lnkURI = lnkURI;
	}

	/**
	 * Gibt die URI fuer dieses Konzept zurueck.
	 * 
	 * @return
	 */
	public URI getURI() {
		return this.lnkURI;
	}

	/**
	 * Gibt alle Schemata zurueck, zu denen das Konzept zugeordnet ist.
	 * 
	 * @return Liste von Schemata
	 */
	public List<Schema> getSchemata() {
		return this.lnkSchemata;
	}

	/**
	 * Fuegt das Konzept zu einem Schema hinzu, indem das Schema in die Liste
	 * aufgenommen wird.
	 * 
	 * @param schema
	 *            Das hinzuzufuegende Schema
	 */
	public void addSchema(Schema schema) {
		if (!this.lnkSchemata.contains(schema)) {
			this.lnkSchemata.add(schema);
			boolean istTopKonzept = true;
			for (int i = 0; istTopKonzept && i < lnkGeneralisierungen.size(); i++) {
				if (lnkGeneralisierungen.get(i).getSchemata().contains(schema)) {
					istTopKonzept = false;
				}
			}
			if (istTopKonzept) {
				List<Konzept> nichtMehrTopKonzepte = new ArrayList<Konzept>();
				for (Konzept k : schema.getTopKonzepte()) {
					if (k.getGeneralisierungen().contains(this)) {
						nichtMehrTopKonzepte.add(k);
					}
				}
				for (Konzept k : nichtMehrTopKonzepte) {
					schema.removeTopKonzept(k);
				}
				schema.addTopKonzept(this);
			}
		}
	}

	/**
	 * Loescht das gewuenschte Schema und entfernt damit das Konzept aus dem
	 * Schema
	 * 
	 * @param schema
	 */
	public void removeSchema(Schema schema) {
		schema.removeTopKonzept(this);
		this.lnkSchemata.remove(schema);
		for (int i = 0; i < lnkSpezialisierungen.size(); i++) {
			Konzept aktKonzept = lnkSpezialisierungen.get(i);
			if (aktKonzept.getSchemata().contains(schema)) {
				boolean istTopKonzept = true;
				for (int j = 0; istTopKonzept
						&& j < aktKonzept.getGeneralisierungen().size(); j++) {
					if (aktKonzept.getGeneralisierungen().get(j).getSchemata()
							.contains(schema)) {
						istTopKonzept = false;
					}
				}
				if (istTopKonzept) {
					schema.addTopKonzept(aktKonzept);
				}
			}
		}
	}

	/**
	 * Gibt die Liste aller Konzepte zurueck, die eine Generalisierung des
	 * aktuellen Konzepts sind.
	 * 
	 * @return
	 */
	public List<Konzept> getGeneralisierungen() {
		return this.lnkGeneralisierungen;
	}

	/**
	 * Fuegt ein Konzept zur Liste aller Konzepte hinzu, die eine
	 * Generalisierung des aktuellen Konzepts sind.
	 * 
	 * @param generalisierung
	 */
	public void addGeneralisierung(Konzept generalisierung) {
		if (!this.lnkGeneralisierungen.contains(generalisierung)) {
			this.lnkGeneralisierungen.add(generalisierung);
			for (int i = 0; i < lnkSchemata.size(); i++) {
				if (generalisierung.getSchemata().contains(lnkSchemata.get(i))) {
					lnkSchemata.get(i).removeTopKonzept(this);
				}
			}
		}
	}

	/**
	 * Loescht ein Konzept aus der Liste aller Konzepte, die eine
	 * Generalisierung des aktuellen Konzepts sind.
	 * 
	 * @param generalisierung
	 */
	public void removeGeneralisierung(Konzept generalisierung) {
		if (this.lnkGeneralisierungen.contains(generalisierung)) {
			this.lnkGeneralisierungen.remove(generalisierung);
			for (int i = 0; i < lnkSchemata.size(); i++) {
				boolean hatTopKonzept = false;
				for (int j = 0; !hatTopKonzept
						&& j < lnkGeneralisierungen.size(); j++) {
					if (lnkGeneralisierungen.get(j).getSchemata().contains(
							lnkSchemata.get(i))) {
						hatTopKonzept = true;
					}
				}
				if (!hatTopKonzept) {
					lnkSchemata.get(i).addTopKonzept(this);
				}
			}
		}
	}

	/**
	 * Gibt die Liste aller Konzepte zurueck, die eine Spezialisierung des
	 * aktuellen Konzepts sind.
	 * 
	 * @return
	 */
	public List<Konzept> getSpezialisierungen() {
		return this.lnkSpezialisierungen;
	}

	/**
	 * Fuegt ein Konzept zur Liste aller Konzepte hinzu, die eine
	 * Spezialisierung des aktuellen Konzepts sind.
	 * 
	 * @param generalisierung
	 */
	public void addSpezialisierung(Konzept spezialisierung) {
		if (!this.lnkSpezialisierungen.contains(spezialisierung)) {
			this.lnkSpezialisierungen.add(spezialisierung);
		}
	}

	/**
	 * Loescht ein Konzept aus der Liste aller Konzepte, die eine
	 * Spezialisierung des aktuellen Konzepts sind.
	 * 
	 * @param spezialisierung
	 */
	public void removeSpezialisierung(Konzept spezialisierung) {
		this.lnkSpezialisierungen.remove(spezialisierung);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Konzept) {
			return this.equals((Konzept) o);
		}
		return false;
	}

	/**
	 * Vergleicht ein Konzept mit diesem.
	 * 
	 * @param other
	 *            Das zu vergleichende Objekt
	 * @return true, wenn beide gleich sind. False sonst.
	 */
	public boolean equals(Konzept other) {
		return this.getURI().equals(other.getURI());
		/*
		 * return this.lnkGeneralisierungen.equals(other.lnkGeneralisierungen)
		 * && this.lnkSchemata.equals(other.lnkSchemata) &&
		 * this.lnkSpezialisierungen.equals(other.lnkSpezialisierungen) &&
		 * this.lnkSprachrepraesentationen
		 * .equals(other.lnkSprachrepraesentationen) &&
		 * this.lnkURI.equals(other.lnkURI);
		 */
	}

	public int hashCode() {
		final int multiplier = 23;
		if (hashCode == 0) {
			int code = 133;
			code = multiplier * code + lnkURI.getUri().hashCode();
			hashCode = code;
		}
		return hashCode;
	}

	/**
	 * Gibt den bevorzugten Bezeichner der Hauptsprache wieder
	 * 
	 * @return String
	 */
	public String toString() {
		return this.getSprachrepraesentationen().get(0)
				.getBevorzugteBezeichnung();
	}

	/**
	 * Hoffentlich saubere Kopierfunktion für Konzept (Achtung: Die Verweise auf
	 * Generalisierungen, Spezialisierungen, Schemata und
	 * Sprachrepreasentationen bleiben verlinkt auf das aktuelle Konzept!)
	 * 
	 * @return eine Kopie des Konzepts
	 */
	public Konzept copy() {
		Konzept k = new Konzept("", null, this.getURI());
		k.lnkGeneralisierungen = (ArrayList) this.getGeneralisierungen();
		k.lnkSprachrepraesentationen = (ArrayList) this
				.getSprachrepraesentationen();
		k.lnkSchemata = (ArrayList) this.getSchemata();
		k.lnkSpezialisierungen = (ArrayList) this.getSpezialisierungen();
		return k;
	}

	/**
	 * Generiert eine HTML Ausgabe zur weiteren Verwendung für die Tooltipps,
	 * beinhaltet alle nützlichen Informationen über das Konzept.
	 * 
	 * @return HTML Ausgabe
	 */
	public String getToolTipText() {
		String ausgabe = "";
		for (Sprachrepraesentation s : this.getSprachrepraesentationen()) {
			String bevorzugt = "";
			if (!s.getSprache().equals(
					Thesaurus.createThesaurus(null, ausgabe).getHauptSprache())) {

				bevorzugt = s.getBevorzugteBezeichnung().isEmpty() ? ""
						: "<i><b>" + "Bevorzugte Bezeichnung: </b>"
								+ s.getBevorzugteBezeichnung() + "</i><br>";
			}
			String alternativ = s.getAltBezeichnungen().toString();
			alternativ = alternativ.substring(1, alternativ.length() - 1);
			if (!alternativ.isEmpty())
				alternativ = "<b>Alternative Bezeichnungen</b>: " + alternativ
						+ "<br>";
			String definition = (s.getDefinition().isEmpty() ? ""
					: "<b>Definition:</b> " + s.getDefinition() + "<br>");
			String beispiel = (s.getBeispiel().isEmpty() ? ""
					: " <b>Beispiele: </b>" + s.getBeispiel() + "<br>");
			String beschreibung = (s.getBeschreibung().isEmpty() ? ""
					: "<b>Beschreibung</b>: " + s.getBeschreibung() + "<br>");
			String gesamt = bevorzugt + alternativ + definition + beschreibung
					+ beispiel;
			ausgabe += (gesamt.isEmpty() ? "" : "<h4>"
					+ s.getSprache().toString() + "</h4>" + gesamt);
		}

		return (ausgabe.isEmpty() ? "<html>Keine weiteren Einträge vorhanden.</html>"
				: "<html> " + ausgabe + "</html>");
	}
}
