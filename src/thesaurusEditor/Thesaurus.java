package thesaurusEditor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
/**
 * Klasse eines Thesaurus Projekts, in deren Objekten alle nötigen Objekte (Schemata, Konzepte, usw) gespeichert werden
 * und grundlegende Methoden für ein Thesauri-Projekt bereitgestellt werden.
 * @author Jan Eric Lenssen
 *
 */
public class Thesaurus extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Schema defaultSchema;

	private Sprache hauptSprache;
	
	private String baseDomain;
	
	/**
	 * Der Thesaurus sollte nach Möglichkeite alle Sprachen kennen, damit dieser weiß, welche Sprachen überhaupt eingesetzt werden
	 * @supplierCardinality *
	 */
	private List<Sprache> sprachen;

	private Exporter exporter;

	/**
	 * @supplierCardinality 1...*
	 */
	private static Thesaurus thesaurus;
	
	private List<Schema> schemata;

	/**
	 * @supplierCardinality *
	 */
	private List<Konzept> konzepte;
	
	private Object changedObject;
	
	/**
	 * Konstrukter, erstellt ein neues Thesauri-Projekt und initialisiert das DefaultSchema und die Hauptsprache.
	 */
	private Thesaurus(Sprache s, String domain) {
		this.setDefaultSchema(new Schema("default",URI.genSchemaURI(domain, "default")));
		hauptSprache = s;
		baseDomain = domain;
		sprachen = new ArrayList<Sprache>();
		sprachen.add(s);
		schemata = new ArrayList<Schema>();
		schemata.add(defaultSchema);
		konzepte = new ArrayList<Konzept>();
		exporter = new Exporter();
	}
	
	/**
	 * Singleton-Methode, die dafür sorgt, das nur ein Thesaurus-Objekt existiert.
	 * Wird anstatt dem Konstrukter aufgerufen und ruft den Konstruktor auf.
	 * @param s  Hauptsprache des neuen Thesaurus
	 * @param domain Hauptdomain des Thesaurus
	 * @return neues Thesaurus-Objekt
	 */
	public static Thesaurus createThesaurus(Sprache s, String domain) {
		if(thesaurus==null) {
			thesaurus = new Thesaurus(s, domain);
		}
		return thesaurus;
	}
	
	/**
	 * Methode die das aktuelle Thesaurus schließt, in dem sie das Singleton-Objekt dereferenziert.
	 */
	public void closeThesaurus() {
		thesaurus = null;
	}
	
	/**
	 * Fügt eine neue Sprache zu dem Thesaurus-Projekt hinzu.
	 * @param s	Sprache, die hinzugefügt werden soll.
	 */
	public void addSprache(Sprache s) {
		if(!sprachen.contains(s)) 
		{
			sprachen.add(s);
			changedObject = s;
			setChanged();
			notifyObservers(sprachen);
		}
	}
	
	/**
	 * Entfernt eine Sprache aus einem Thesauriobjekt, ruft auch für jedes Konzept die removeSprachrepraesentation auf,
	 * um alle Sprachrepraesentationen dieser Sprache zu entfernen
	 * @param s Sprache, die entfernt werden soll.
	 */
	public void removeSprache(Sprache s) {
		if(sprachen.contains(s))
		{
			changedObject = s;
			sprachen.remove(s);
			for(Konzept konzept: konzepte) {
				konzept.removeSprachrepraesentation(s);
			}
			setChanged();
			notifyObservers(sprachen);
		}
	}
	
	/**
	 * Gibt die Liste aller im Thesaurusprojekt unterstützten Sprachen zurück.
	 * @return Liste aller Sprachen
	 */
	public List<Sprache> getSprachen() {
            ArrayList<Sprache> hilf = new ArrayList<Sprache>();
		for (Sprache s:this.sprachen){
			hilf.add(s);
		}
		return hilf;
	}
	
	/**
	 * Entfernt ein Schema aus dem Thesauri-Projekt - sollte nicht auf das Default-Schema angewandt werden.
	 * @param s Schema, das entfernt werden soll.
	 */
	public void removeSchema(Schema s) {
		if(schemata.contains(s))
		{
			schemata.remove(s);
			changedObject = s;
			for(Konzept konzept: konzepte) {
				konzept.removeSchema(s);
			}
			setChanged();
			notifyObservers(schemata);
		}
	}
	
	/**
	 * Neues Schema zu Thesaurus-Projekt hinzufügen.
	 * @param s Schema, das hinzugefügt wird.
	 */
	public void addSchema(Schema s) {
		if(!schemata.contains(s))
		{
			changedObject = s;
			schemata.add(s);
			setChanged();
			notifyObservers(schemata);
		}
	}
	
	/**
	 * Konzept zu ThesaurusProjekt hinzufügen.
	 * @param k Konzept, das hinzugefügt werden soll.
	 */
	public void addKonzept(Konzept k) {
		if(!konzepte.contains(k))
		{
		    k.addSchema(defaultSchema);
			changedObject = k;
			konzepte.add(k);
			setChanged();
			notifyObservers(konzepte);
		}
	}
	
	/**
	 * Gibt die Hauptsprache des Thesaurus-Projekts
	 * @return Hauptsprache des Thesaurus-Projekts
	 */
	public Sprache getHauptSprache() {
		return hauptSprache;
	}
	
	/**
	 * Gibt das Default-Schema, in dem alle Konzepte enthalten sind, zurück.
	 * @return Default-Schema
	 */
	public Schema getDefaultSchema() {
		return defaultSchema;
	}
	
	/**
	 * Setzt das Default-Schema.
	 * @param schema Schema, welches Default-Schema werden soll.
	 */
	public void setDefaultSchema(Schema schema) {
		changedObject = schema;
		this.defaultSchema = schema;
		setChanged();
		notifyObservers(schema);
	}
	
	public String getBaseDomain() {
		return baseDomain;
	}
	
	public void setBaseDomain(String domain) {
		baseDomain = domain;
		changedObject = domain;
		setChanged();
		notifyObservers(domain);
	}
	
	/**
	 * Entfernt Konzept aus dem Thesaurus-Projekt. Entfernt auch alle Verbindungen zu anderen Konzepten und zu Schemata.
	 * @param k Konzept, das entfernt werden soll.
	 */
         public void removeKonzept(Konzept k) {
            if (konzepte.contains(k)) {
                //k aus den TopKonzepten löschen
                for (Schema schema : schemata) {
		    k.removeSchema(schema);
                }
                /*Wenn Generalisierungen UND Spezialisierungen existieren,
                müssen die Spezialisierungen (von k) als Generalisierung die
                Generalisierung von k bekommen und umgekehrt.*/
                if (!k.getGeneralisierungen().isEmpty() && !k.getSpezialisierungen().isEmpty()) {
                    for (Konzept spez : k.getSpezialisierungen()) {
                        for (Konzept gen : k.getGeneralisierungen()) {
                            spez.addGeneralisierung(gen);
                            gen.addSpezialisierung(spez);
                            //Dem Graphen Bescheid sagen
                            this.generalisierungChanged(gen, spez);
                        }
                    }
                }
                /*Jetzt aus allen Spez und Gen k löschen.
                Darf oben nicht passieren, da es vorkommen kann,
                dass es keine Spez und/oder keine gen gibt.*/
                for (Konzept konzept : konzepte) {
                    konzept.getGeneralisierungen().remove(k);
                    konzept.getSpezialisierungen().remove(k);
                }
                // k noch aus der Liste aller Konzepte löschen
                konzepte.remove(k);
                // hier noch ein paar Sachen für Observer stuff
                changedObject = k;
                setChanged();
                notifyObservers(konzepte);
            }
        }
	
	/**
	 * Gibt die Liste aller Schemata zurück.
	 * @return Liste aller Schemata.
	 */
	public List<Schema> getSchemata() {
		return schemata;
	}
	
	/**
	 * Gibt die Liste aller Konzepte zurück.
	 * @return Liste aller Konzepte.
	 */
	public List<Konzept> getKonzepte() {
		return konzepte;
	}
	
	/**
	 * Prüft, ob im Thesaurus Konzepte existieren, in der bevorzugte Bezeichner zu einer unterstützen Sprache fehlen.
	 * @return true, falls bevorzugte Bezeichner fehlen - false, falls nicht.
	 */
	public boolean hatKonzepteOhneBevBez() {
		if(!this.sucheOhneBevBezeichnung().isEmpty()) return true;
		return false;
	}
	
	/**
	 * Sucht alle Konzepte raus, in denen bevorzugte Bezeichner zu einer unterstützten Sprache fehlen.
	 * @return Liste aller Konzepte in denen bevorzugte Bezeichner fehlen.
	 */
	public List<Konzept> sucheOhneBevBezeichnung() {
		List<Konzept> list = new ArrayList<Konzept>();
		for(Konzept konzept: konzepte) {
			for(Sprache sprache: sprachen) {
				if(!konzept.hatBevBezeichnung(sprache)) {
					if(!list.contains(konzept)) list.add(konzept);
				}
			}
		}
		return list;
	}
	
	/**
	 * Sucht alle Konzepte aus der Menge aller Konzepte, die eine gesuchte Bezeichnung in einer Sprache enthalten
	 * und ordnet sie nach Art des gefundenen Wortes (bevorzugt, alternativ, versteckt) in ein Feld aus drei Listen ein.
	 * @param suchwort Gesuchte Bezeichnung.
	 * @param sprache Sprache, in der gesucht werden soll.
	 * @return Ein Feld mit 3 Listen aus Konzepten. In [0] die Konzepte, in denen das gesuchte Wort als
	 * bevorzugter Bezeichner gefunden wurde. In [1] als alternativer und in [2] als versteckter Bezeichner.
	 */
	public List<Konzept>[] suche(String suchwort, Sprache sprache) {
		List<Konzept>[] list = new ArrayList[3];
		list[0] = new ArrayList<Konzept>();
		list[1] = new ArrayList<Konzept>();
		list[2] = new ArrayList<Konzept>();
		
		for(Konzept konzept: konzepte) {
			if(konzept.enthaeltWort(sprache, suchwort)==BezeichnungsTyp.bevorzugt) list[0].add(konzept);
			if(konzept.enthaeltWort(sprache, suchwort)==BezeichnungsTyp.alternativ) list[1].add(konzept);
			if(konzept.enthaeltWort(sprache, suchwort)==BezeichnungsTyp.versteckt) list[2].add(konzept);
		}
		return list;
	}

        /**
         * Überprüft ob der Graf Zyklen hat
         * @return
         */
        public boolean hatZyklen(){
            if (konzepte.isEmpty()) return false; // Wenn keine Konzepte vorhanden, ist auch kein Zyklus vorhanden
            
            List<Integer> startPunkte= findTopKonzept(); // Zunächst wird nach allen Konzepten gesucht, die ganz oben stehen, die also keine Generalisierungen haben
            if (startPunkte.isEmpty()) // gibt es kein einziges Konzept, welches keine Generalisierungen hat, ist ein Zyklus vorhanden
                return true;
            for (int a = 0; a < startPunkte.size();a++){ // nur werden alle "Bäume" für sich durchsucht, ob sie Zyklen haben
            	boolean[] proofed = new boolean[konzepte.size()];
                for (int i = 0; i < proofed.length; i++){
                    proofed[i] = false;
                }
                if (hatZyklen(startPunkte.get(a),proofed)) return true;
            }
            
            return false; // hat keiner der Bäume Zyklen exsitieren insgesamt keine Zyklen

        }

        /**
         * Sucht von einem bestimmten Index in der Konzeptliste aus auf Zyklen
         * @param i     Index des Startkonzeptes in der Konzeptliste
         * @param proofed   boolean Array welches Besuchstatus enthaelt
         * @return
         */
        private boolean hatZyklen(int i, boolean[] proofed){ // i ist der Index des Konezeptes in der Konzeptliste
            if (!proofed[i]){       // Wenn der Knoten schon einmal besucht wurde ist proofed[i] true, dann liegt ein Zyklus vor 
              proofed[i] = true;
              List<Konzept> spezialisierungen = konzepte.get(i).getSpezialisierungen(); // alle Spezialisierungen werden auf Zyklen untersucht
              for (int x = 0; x < spezialisierungen.size();x++){
                  int index = getIndex(spezialisierungen.get(x)); // Index holen 
                  if(hatZyklen(index,proofed)){
                	  return true;
                  }
              }
              return false;
            }
            return true;
        }

        /**
         * Gibt den Index zu einem Konzept innerhalb der Konzeptliste zurück
         * @param k Konzept dessen Index gesucht werden soll
         * @return
         */
        private int getIndex(Konzept k){
            for (int i = 0; i < this.konzepte.size(); i++)
            {
                if (this.konzepte.get(i).equals(k)) return i;
            }

            throw new NullPointerException("Bei get Index tauchte der Index nicht auf, folglich existiert ein Konzept in einer Generalisierung/Spezialisierung aber nicht in unserer Hauptliste");
        }

        /**
         * Gibt die Indizes aller Konzepte innerhalb der Konzeptliste zurück, die keine Generalisierungen haben
         * @return
         */
        private List<Integer> findTopKonzept(){
            List<Integer> topKonzepte = new ArrayList<Integer>();
            for (int i = 0; i < this.konzepte.size(); i++)
            {
                if (this.konzepte.get(i).getGeneralisierungen().isEmpty()){
                    topKonzepte.add(i);
                }
            }
            return topKonzepte;
        }
        
        public Object getChange()
        {
        	return changedObject;
        }

	/**
	 * Methode um dem Graphen mitzuteilen, dass eine Generalisierung geaddet oder removed wurde.
	 * @param gen
	 * @param spez
	 */
	public void generalisierungChanged(Konzept gen, Konzept spez){
	    Konzept[] konzepte = new Konzept[2];
	    konzepte[0] = gen;
	    konzepte[1] = spez;
	    this.changedObject = konzepte;
	    this.setChanged();
	    this.notifyObservers();
	}

}
