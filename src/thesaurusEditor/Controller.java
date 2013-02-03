package thesaurusEditor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Der Controller macht alles ^^
 * @author sopr051
 *
 */
public class Controller {

    /**
     * Das aktuelle Konzept, welches gerade "geöffnet" ist.
     * Wird bei einer Suche/Auswahl gesetzt.
     */
    private Konzept aktuellesKonzept;
    /**
     * Der Thesaurus, welcher diesem Controller zugrunde liegt.
     * Wird beim Laden/Erstellen gesetzt.
     */
    private Thesaurus lnkThesaurus;

    /**
     * Erstellt einen neuen Thesaurus und setzt dessen Hauptsprache
     * @param hauptsprache Hauptsprache des neuen Thesaurus.
     */
    public Thesaurus createThesaurus(String hauptsprache, String hauptspracheKuerzel) {
	this.lnkThesaurus = Thesaurus.createThesaurus(new Sprache(hauptspracheKuerzel, hauptsprache),"");
    return lnkThesaurus;
    }
    
    /**
     * Gibt den Thesaurus zurueck.
     */
    public Thesaurus getThesaurus()
    {
    	return lnkThesaurus;
    }

    /**
     * Lädt den Thesaurus (De-Serialize).
     */
    public void laden(String pfad) {
	FileInputStream fis = null;
	ObjectInputStream in = null;
	try {
	    fis = new FileInputStream(pfad);
	    in = new ObjectInputStream(fis);
	    this.lnkThesaurus = (Thesaurus) in.readObject();
	} catch (Exception e) {
	    /*
	    objekt = new Statistik();
	    throw new StatistikException("Statistik wurde neu angelegt.");
	     */
	} finally {
	    try {
		in.close();
		fis.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Speichert den Thesaurus (Serialize).
     */
    public void speichern(String pfad) {
	FileOutputStream fout = null;
	ObjectOutputStream out = null;
	try {
	    fout = new FileOutputStream(pfad);
	    out = new ObjectOutputStream(fout);
	    out.writeObject(this.lnkThesaurus);
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		out.close();
		fout.close();
	    } catch (Exception e) {
	    }
	}
    }

    /**
     * Fügt ein Schema zum aktuellen Konzept (this.aktuellesKonzept) hinzu.
     * @param s Das Schema, welches hinzugefügt werden soll.
     */
    public void addSchemaToKonzept(Schema s) {
	this.aktuellesKonzept.addSchema(s);
    }

    /**
     * Entfernt ein Schema vom aktuellen Konzept (this.aktuellesKonzept).
     * @param s Das zu entfernende Schema.
     */
    public void removeSchemaFromKonzept(Schema s) {
	this.aktuellesKonzept.removeSchema(s);
    }

    /**
     * Fügt ein neues Schema zum Thesaurus hinzu.
     * @param schema Name des Schemas.
     */
    public void addSchema(String schema) {
	this.lnkThesaurus.addSchema(new Schema(schema, this.genSchemaURI(schema)));
    }

    /**
     * Entfernt ein Schema aus dem Thesaurus.
     * @param s Das zu entfernende Schema.
     */
    public void removeSchema(Schema s) {
	this.lnkThesaurus.removeSchema(s);
    }

    /**
     * Fügt eine Sprache zum Thesaurus hinzu.
     * @param sprache ausgeschriebene Bezeichnung der Sprache (z.B. "Deutsch").
     * @param kuerzel einheitliches Kürzel der Sprache (z.B. "de").
     */
    public void addSprache(String sprache, String kuerzel) {
	this.lnkThesaurus.addSprache(new Sprache(kuerzel, sprache));
    }

    /**
     * Entfernt eine Sprache aus dem Thesaurus.
     * @param s Die zu entfernende Sprache.
     */
    public void removeSprache(Sprache s) {
	this.lnkThesaurus.removeSprache(s);
    }

    /**
     * Fügt eine Bezeichnung zum aktuellen Konzept (this.aktuellesKonzept) hinzu.
     * @param bez Bezeichnung die hinzugefügt werden soll.
     * @param typ Typ der Bezeichnung.
     */
    public void addBezeichnung(String bez, Sprache sprache, BezeichnungsTyp typ) {
	this.aktuellesKonzept.addBezeichnung(bez, sprache, typ);
    }

    /**
     * Entfernt eine Bezeichnung vom aktuellen Konzept.
     * @param bez Bezeichnung, die entfernt werden soll.
     * @param typ Typ der Bezeichnung.
     */
    public void removeBezeichnung(String bez, Sprache sprache, BezeichnungsTyp typ) {
	this.aktuellesKonzept.removeBezeichnung(bez, sprache, typ);
    }

    public void removeBezeichnungen () {
	this.aktuellesKonzept.removeBezeichnungen();
    }

    /**
     * Fügt eine Spezialisierung zum aktuellen Konzept (this.aktuellesKonzept) hinzu.
     * @param k Das aktuelle Konzept (this.aktuellesKonzept) erhält als Spezialisierung k.
     */
    public void addSpezialisierung(Konzept k) {
	this.aktuellesKonzept.addSpezialisierung(k);
	/*
	if(k.getGeneralisierungen().isEmpty()){
	    k.getSchemata().get(0).removeTopKonzept(k);
	}
	* Auskommentiert, da Top-Konzept-L�schung schon bei addGeneralisierung im Konzept stattfindet.
	* Siehe auch besprochenes Sequenzdiagramm zu "Controller.addSpezialisierung"
	*/
	k.addGeneralisierung(this.aktuellesKonzept);
	this.getThesaurus().generalisierungChanged(this.aktuellesKonzept, k);
    }

    /**
     * Entfernt eine Spezialisierung vom aktuellen Konzept (this.aktuellesKonzept).
     * @param k Das zu entfernende Konzept.
     */
    public void removeSpezialisierung(Konzept k) {
	this.aktuellesKonzept.removeSpezialisierung(k);
	k.removeGeneralisierung(aktuellesKonzept);
	/*
	if(k.getGeneralisierungen().isEmpty()){
	    k.getSchemata().get(0).addTopKonzept(k);
	}
	* Auskommentiert, da Top-Konzept-L�schung schon bei addGeneralisierung im Konzept stattfindet.
	* Siehe auch besprochenes Sequenzdiagramm zu "Controller.addSpezialisierung"
	*/
	this.getThesaurus().generalisierungChanged(this.aktuellesKonzept, k);
    }

    public Konzept getAktuellesKonzept(){
        return this.aktuellesKonzept;
    }
    
    public void setAktuellesKonzept(Konzept k) {
    	aktuellesKonzept = k;
    }
    
    /**
     * Fügt eine Generalisierung zum aktuellen Konzept (this.aktuellesKonzept) hinzu.
     * @param k Das aktuelle Konzept (this.aktuellesKonzept) erhält als Generalisierung k.
     */
    public void addGeneralisierung(Konzept k) {
	/*
	 if (this.aktuellesKonzept.getGeneralisierungen().isEmpty()) {


	    this.aktuellesKonzept.getSchemata().get(0).removeTopKonzept(this.aktuellesKonzept);
	}
	* Auskommentiert, da die �berpr�fung auf Topkonzept-L�schung schon in "addGeneralisierung" des Konzepts erledigt wird.
	*/
	this.aktuellesKonzept.addGeneralisierung(k);
	k.addSpezialisierung(this.aktuellesKonzept);
	this.getThesaurus().generalisierungChanged(k, this.aktuellesKonzept);
    }

    /**
     * Entfernt eine Generalisierung vom aktuellen Konzept (this.aktuellesKonzept).
     * @param k Das zu entfernende Konzept.
     */
    public void removeGeneralisierung(Konzept k) {
	this.aktuellesKonzept.removeGeneralisierung(k);
	/*
	if(this.aktuellesKonzept.getGeneralisierungen().isEmpty()){
	    this.aktuellesKonzept.getSchemata().get(0).addTopKonzept(this.aktuellesKonzept);
	}
	* Auskommentiert, da die �berpr�fung auf Topkonzept L�schung schon in "removeGeneralisierung" des Konzepts erledigt wird.
	*/
	k.removeSpezialisierung(this.aktuellesKonzept);
	this.getThesaurus().generalisierungChanged(k, this.aktuellesKonzept);
    }

    /**
     * Fügt ein neues Konzept zum Thesaurus hinzu. Dieses wird zunächst dem Default-Schema zugeordnet und erhält weder Generalisierungen noch Spezialisierungen. Dies muss über weitere Methodenaufrufe geregelt werden (Wiederverwendbarkeit).
     * @param bezeichnung Bevorzugter Bezeichner in der Hauptsprache des Thesaurus.
     */
    public void addKonzept(String bezeichnung) {
	this.aktuellesKonzept = new Konzept(bezeichnung, lnkThesaurus.getHauptSprache(), this.genKonzeptURI(bezeichnung));
	this.lnkThesaurus.addKonzept(this.aktuellesKonzept);
	this.aktuellesKonzept.addSchema(this.lnkThesaurus.getDefaultSchema());
	this.lnkThesaurus.getDefaultSchema().addTopKonzept(this.aktuellesKonzept);
    }

    /**
     * Löscht das aktuelle Konzept (this.aktuellesKonzept) aus dem Thesaurus.
     */
    public void removeKonzept() {
	this.lnkThesaurus.removeKonzept(this.aktuellesKonzept);
	this.aktuellesKonzept = null;
    }

    /**
     * Setzt eine neue URI für das aktuelle Konzept (this.aktuellesKonzept).
     * @param uri die zu setzende URI. Falls diese existiert wird eine Zahl angehangen.
     */
    public void setURI(String uri){
	this.aktuellesKonzept.setURI(this.genKonzeptURI(uri));
    }

    public void setSchemaURI(Schema s, String uri){
	s.setURI(this.genSchemaURI(uri));
    }
    
    /**
     * Erzeugt eine URI für Konzepte aus der übergebenen Bezeichnung.
     * @param bezeichnung Bezeichnung aus der eine URI generiert werden soll.
     * @return Eine eindeutige, noch nicht existente URI zu bezeichnung.
     */
    private URI genKonzeptURI(String bezeichnung)
    {
    	URI waere = URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
        
    	for(Konzept k:this.lnkThesaurus.getKonzepte()){
    	    if(k.getURI().equals(waere)){
    		return this.genKonzeptURI(bezeichnung, 1);
    	    }
    	}
    	return URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
    }
    
    /**
     * @see genKonzeptURI
     */
    private URI genKonzeptURI(String bezeichnung, int anhang){
    URI waere = URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);	
    
	for(Konzept k : this.lnkThesaurus.getKonzepte()){
	    if(k.getURI().equals(waere)){
		return this.genKonzeptURI(bezeichnung, anhang + 1);
	    }
	}
	return URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);
    }
    
    /**
     * Erzeugt eine URI für Schemata aus der übergebenen Bezeichnung.
     * @param bezeichnung Bezeichnung aus der eine URI generiert werden soll.
     * @return Eine eindeutige, noch nicht existente URI zu bezeichnung.
     */
    private URI genSchemaURI(String bezeichnung)
    {
    	URI waere = URI.genSchemaURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
        
    	for(Konzept k:this.lnkThesaurus.getKonzepte()){
    	    if(k.getURI().equals(waere)){
    		return this.genSchemaURI(bezeichnung, 1);
    	    }
    	}
    	return URI.genSchemaURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
    }
    
    /**
     * @see genSchemaURI
     */
    private URI genSchemaURI(String bezeichnung, int anhang){
    URI waere = URI.genSchemaURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);	
    
	for(Konzept k : this.lnkThesaurus.getKonzepte()){
	    if(k.getURI().equals(waere)){
		return this.genSchemaURI(bezeichnung, anhang + 1);
	    }
	}
	return URI.genSchemaURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);
    }

    /**
     * Erzeugt eine URI aus der übergebenen Bezeichnung.
     * @param bezeichnung Bezeichnung aus der eine URI generiert werden soll.
     * @return Eine eindeutige, noch nicht existente URI zu bezeichnung.
     */
    /*
    private URI genURI(String bezeichnung) {
    URI waere = URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
    
	for(Konzept k:this.lnkThesaurus.getKonzepte()){
	    if(k.getURI().equals(waere)){
		return this.genURI(bezeichnung, 1);
	    }
	}
	return URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung);
    }
    */

    /**
     * @see genURI
     */
    /*
    private URI genURI(String bezeichnung, int anhang){
    URI waere = URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);	
    
	for(Konzept k : this.lnkThesaurus.getKonzepte()){
	    if(k.getURI().equals(waere)){
		return this.genURI(bezeichnung, anhang + 1);
	    }
	}
	return URI.genKonzeptURI(this.lnkThesaurus.getBaseDomain(), bezeichnung + anhang);
    }
    */

    public List<Konzept>[] suchen(String suchWort, Sprache sprache) {
        //System.out.println("Suchwort: "+suchWort+" Sprache: "+sprache);
        return this.lnkThesaurus.suche(suchWort, sprache);
    }
    
    /**
     * 
     * @param Export-Datei-Pfad.
     * @throws ExportException
     */
    public void exportieren(String pfad) throws ExportException
    {
    	Exporter.export(pfad, this.getThesaurus());
    }
}
