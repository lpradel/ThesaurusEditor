package thesaurusEditor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse kapselt, die Bezeichner für eine bestimmte Sprache. Sie enthält den bevorzugten Bezeichner, alle alternativen und versteckten Bezeichner. Auch die Bemerkung, wird in der jeweiligen Sprache gemerkt.
 * Das wichtigste: Eine Sprachrepräsentation steht für eine Sprache.
 * @author Sarah
 *
 */
public class Sprachrepraesentation implements Serializable {
	/**
	 * Dient einer Bemerkung, die evt. zu einem Konzept hinzugefügt werden kann.
	 * Wenn der String leer ist, wird dieser auf String.empty gesetzt
	 */
	private String beschreibung;
	
	/**
	 * Gibt eine Definition an
	 * Wenn der String leer ist, wird dieser auf String.empty gesetzt
	 */
	private String definition;
	
	/**
	 * Dient der Haltung der Beispiele
	 * Wenn der String leer ist, wird dieser auf String.empty gesetzt
	 */
	private String beispiel;
	/**
	 * Merkt sich die Sprache, in welcher alle Bezeichner und die Bemerkung vorliegen.
	 * Wenn der String leer ist, wird dieser auf String.empty gesetzt
	 */
	private Sprache lnkSprache;

	/**
	 * Merkt sich den bevorzugten Bezeichner.
	 */
	private String bevorzugteBezeichnung;
	
	/**
	 * Merkt sich eine Liste alle alternativen Bezeichner.
	 * @supplierCardinality *
	 */
	
	private List<String> altBezeichnungen;
	
	/**
	 * Bezeichnet eine Liste aller versteckten Bezeichner.
	 * @supplierCardinality *
	 */
	
	private List<String> versteckteBezeichnungen;
	
	/**
	 * Dieser Konstruktor legt eine leere Sprachrepräsentation an, indem nur die Sprache festgelegt wird.
	 * @param lnkSprache
	 */
	public Sprachrepraesentation(Sprache lnkSprache){
		this.init(lnkSprache);
	}
	
	/**
	 * Dieser Konstruktor erlaubt es eine Sprachrepräsentation zu erstellen, die bereits ein Wort mit dem BezeichnungsTyp b enthalten soll.
	 * @param lnkSprache Gibt die Sprache an, in der die Sprachrepräsentation ist.
	 * @param wort Gibt das schon einzufügende Wort an
	 * @param b Gibt den BezeichnerTyp des Wortes wort an
	 */
	public Sprachrepraesentation(Sprache lnkSprache, String wort, BezeichnungsTyp b){
		this.init(lnkSprache);
		
		if (b == BezeichnungsTyp.bevorzugt){
			this.bevorzugteBezeichnung = wort;
		}
		
		else if (b == BezeichnungsTyp.alternativ){
			this.altBezeichnungen.add(wort);
		}
		else if (b == BezeichnungsTyp.versteckt){
			this.versteckteBezeichnungen.add(wort);
		}
		// Wenn der Bezeichnungstyp kein ist, passiert einfach gar nichts
	}
	
	/**
	 * Initialisiert die Sprachrepräsentation, setzt Sprache und erzeugt die Liste
	 * @param lnkSprache Sprache der Sprachrepräsentation
	 */
	private void init(Sprache lnkSprache){
		this.lnkSprache = lnkSprache;
		this.altBezeichnungen = new ArrayList<String>();
		this.versteckteBezeichnungen = new ArrayList<String>();
		this.beschreibung = "";
		this.beispiel = "";
		this.bevorzugteBezeichnung = "";
		this.definition = "";
	}

	/**
	 * Prüft ob ein Wort in einem der Bezeichner vorhanden ist und gibt den Typ des Bezeichners zurück.
	 * @param wort beschreibt das zu suchende Wort
	 * @return Es wird der BezeichnungsTyp des Wortes innerhalb der Sprachrepräsentation zurückgegeben.
	 */
	public BezeichnungsTyp enthaeltWort(String wort) {
		if (!bevorzugteBezeichnung.isEmpty() && istEnthalten(wort,bevorzugteBezeichnung)) 
			return BezeichnungsTyp.bevorzugt;

		for(String s:altBezeichnungen){
			if (istEnthalten(wort,s)){
				return BezeichnungsTyp.alternativ;
			}
		}
		
		for (String s:versteckteBezeichnungen){
			if(istEnthalten(wort,s)){
				return BezeichnungsTyp.versteckt;
			}
		}
		
		return BezeichnungsTyp.kein;
	}
	
	private boolean istEnthalten(String suche, String wort)
	{
		String sucheLow = suche.toLowerCase().trim();
		String wortLow = wort.toLowerCase().trim();
		return wortLow.contains(sucheLow);
	}

	/**
	 * Setzt die Beschreibung
	 * @param beschreibung
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * gibt die Beschreibung für dieses Konzept zurück
	 * @return
	 */
	public String getBeschreibung() {
		return beschreibung;
	}
	
	//Sprache setzen ist nicht erlaubt, dann lieber alles löschen
	
	/**
	 * Gibt die Sprache der Sprachrepräsentation zurück.
	 */
	public Sprache getSprache() {
		return lnkSprache;
	}
	
	/**
	 * Setzt den bevorzugten Bezeichner neu und fügt den alten bevorzugten Bezeichner, in die Liste der alternativen ein, falls er dort noch nicht vorhanden ist.
	 * @param bevorzugt Neuer bevorzugter Bezeichner
	 */
	public void setBevorzugteBezeichnung(String bevorzugt){
		this.bevorzugteBezeichnung = bevorzugt;
	}
	
	/**
	 * Der bevorzugte Bezeichner wird zurückgegeben
	 * @return
	 */
	public String getBevorzugteBezeichnung(){
		return bevorzugteBezeichnung;
	}
	
	/**
	 * Gibt eine Kopie der Liste der alternativen Bezeichner zurück 
	 * @return
	 */
	public List<String> getAltBezeichnungen(){
		ArrayList<String> hilf = new ArrayList<String>();
		for (String s:this.altBezeichnungen){
			hilf.add(s);
		}
		return hilf;
	}
	
	/**
	 * Fügt der Liste der alternativen Bezeichner einen weiteren Bezeichner hinzu, falls dieser nicht vorhanden ist 
	 * @param alternativ
	 */
	public void addAltBezeichnung(String alternativ){
		if (!this.altBezeichnungen.contains(alternativ)){
			this.altBezeichnungen.add(alternativ);
		}
	}
	
	/**
	 * Lösche das als Parameter übergebene Wort aus der Liste der alternativen Bezeichner.
	 * @param alternativ
	 */
	public void removeAltBezeichnung(String alternativ){
		this.altBezeichnungen.remove(alternativ); //hier könnte man eine Excpetion werfen evt. falls das Wort gar nicht enthalten ist. Remove wirft keine  
	}
	
	/**
	 * Gibt eine Kopie der Liste der versteckten Bezeichnungen zurück.
	 * @return
	 */
	public List<String> getVersteckteBezeichnungen(){
		ArrayList<String> hilf = new ArrayList<String>();
		for (String s:this.versteckteBezeichnungen){
			hilf.add(s);
		}
		return hilf;
	}
	
	/**
	 * Fügt der Liste der Versteckten Bezeichnungen, die übergebene Bezeichnung hinzu.
	 * @param versteckt
	 */
	public void addVersteckteBezeichnung(String versteckt){
		if (!this.versteckteBezeichnungen.contains(versteckt)){
			this.versteckteBezeichnungen.add(versteckt);
		}
	}
	
	/**
	 * Löscht die übergebene Bezeichnung, falls vorhanden, aus der List der versteckten Bezeichnungen
	 * @param versteckt
	 */
	public void removeVersteckteBezeichnung(String versteckt){
		this.versteckteBezeichnungen.remove(versteckt);
	}

	public void removeBezeichnungen () {
	    this.versteckteBezeichnungen.clear();
	    this.altBezeichnungen.clear();
	}

	/**
	 * Setzt die Definition 
	 * @param definition
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * Gibt die Definition zurück
	 * @return
	 */
	public String getDefinition() {
		return definition;
	}
	
	/**
	 * Setzt das Beispiel
	 * @param beispiel
	 */
	public void setBeispiel(String beispiel) {
		this.beispiel = beispiel;
	}

	/**
	 * Gibt das Beispiel zurück
	 * @return
	 */
	public String getBeispiel() {
		return beispiel;
	}
	
	/**
	 * Prüft auf Gleichheit
	 * @return
	 */
	public boolean equals(Sprachrepraesentation andere){
		return (beschreibung.equals(andere.getBeschreibung()))
		&& definition.equals(andere.getDefinition())
		&& beispiel.equals(andere.getBeispiel())
		&& lnkSprache.equals(andere.getSprache())
		&& bevorzugteBezeichnung.equals(andere.getBevorzugteBezeichnung())
		&& altBezeichnungen.equals(andere.getAltBezeichnungen())
		&& versteckteBezeichnungen.equals(andere.getVersteckteBezeichnungen());
	}
}
