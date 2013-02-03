/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor.gui;

import thesaurusEditor.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import thesaurusEditor.BezeichnungsTyp;

import thesaurusEditor.Konzept;
import thesaurusEditor.Konzept;
import thesaurusEditor.Schema;
import thesaurusEditor.Sprache;
import thesaurusEditor.Sprache;
import thesaurusEditor.Thesaurus;
import thesaurusEditor.Thesaurus;
import thesaurusEditor.URI;
import thesaurusEditor.URI;


/**
 *
 * @author Jan Eric Lenssen
 */
public class ThesaurusTestfall {

    public ThesaurusTestfall() {
    }
    
    public static Thesaurus genTestThesaurus() {
    	Thesaurus t;
    	
    	Sprache hauptsprache = new Sprache("de", "Deutsch");
    	Sprache englisch = new Sprache("en", "Englisch");
    	String domain = "http://www.wer-das-liest-ist-doof.de/";
    	
    	/* Thesaurus-Objekt anlegen */
    	t = Thesaurus.createThesaurus(hauptsprache, domain);
    	t.closeThesaurus();
    	t = Thesaurus.createThesaurus(hauptsprache, domain);
    	
    	/* Sprachen */
    	t.addSprache(englisch);
    	
    	/* Konzepte */
    	Konzept hund = new Konzept("Hund", hauptsprache, URI.genKonzeptURI(domain, "Hund"));
    	Konzept katze = new Konzept("Katze", hauptsprache, URI.genKonzeptURI(domain, "Katze"));
    	Konzept maus = new Konzept("Maus", hauptsprache, URI.genKonzeptURI(domain, "Maus"));
    	Konzept perserkatze = new Konzept("Perserkatze", hauptsprache, URI.genKonzeptURI(domain, "Perserkatze"));
    	Konzept saeugetier = new Konzept("Säugetier", hauptsprache, URI.genKonzeptURI(domain, "Säugetier"));
    	Konzept tier = new Konzept("Tier", hauptsprache, URI.genKonzeptURI(domain, "Tier"));
    	
    	/* Bezeichnungen */
    	hund.addBezeichnung("Köter", hauptsprache, BezeichnungsTyp.alternativ);
    	hund.addBezeichnung("Töle", hauptsprache, BezeichnungsTyp.alternativ);
    	hund.addSprachrepraesentation(englisch);
    	hund.addBezeichnung("Dog", englisch, BezeichnungsTyp.bevorzugt);
    	
    	katze.addBezeichnung("Kätzchen", hauptsprache, BezeichnungsTyp.alternativ);
    	katze.addBezeichnung("Kaetzchen", hauptsprache, BezeichnungsTyp.versteckt);
    	
    	/* Schemata */
    	Schema haustiere = new Schema("Haustier", URI.genSchemaURI(domain, "Haustier"));
    	hund.addSchema(haustiere);
        katze.addSchema(haustiere);
        saeugetier.addSchema(haustiere);
        perserkatze.addSchema(haustiere);

    	/* Beziehungen */
    	saeugetier.addSpezialisierung(hund);
    	saeugetier.addSpezialisierung(katze);
    	saeugetier.addSpezialisierung(maus);
    	katze.addSpezialisierung(perserkatze);
    	tier.addSpezialisierung(saeugetier);
    	perserkatze.addGeneralisierung(katze);
    	hund.addGeneralisierung(saeugetier);
    	katze.addGeneralisierung(saeugetier);
    	maus.addGeneralisierung(saeugetier);
    	saeugetier.addGeneralisierung(tier);
    	
    	
    	/* Schemata-Beziehungen */
    	haustiere.addTopKonzept(saeugetier);
    	
    	
    	/* Anlegen */
    	t.addKonzept(hund);
    	t.addKonzept(katze);
    	t.addKonzept(maus);
    	t.addKonzept(perserkatze);
    	t.addKonzept(saeugetier);
    	t.addKonzept(tier);
    	t.addSchema(haustiere);
    	
    	
    		FileOutputStream fout = null;
    		ObjectOutputStream out = null;
    		try {
    		    fout = new FileOutputStream("testThesaurus");
    		    out = new ObjectOutputStream(fout);
    		    out.writeObject(t);
    		} catch (IOException e) {
    		    e.printStackTrace();
    		} finally {
    		    try {
    			out.close();
    			fout.close();
    		    } catch (Exception e) {
    		    }
    		}
    	
    	return t;
    }

}