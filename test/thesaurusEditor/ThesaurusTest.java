/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thesaurusEditor.Konzept;
import thesaurusEditor.Sprache;
import thesaurusEditor.Thesaurus;
import thesaurusEditor.URI;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Eric Lenssen
 */
public class ThesaurusTest {

    public ThesaurusTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of hatKonzepteOhneBevBez method, of class Thesaurus.
     */
    @Test
    public void testHatKonzepteOhneBevBez() {
        System.out.println("hatKonzepteOhneBevBez");

        Thesaurus instance =  Thesaurus.createThesaurus(new Sprache("de","Deutsch"),"");
        instance.addSprache(new Sprache("en","English"));
        instance.addKonzept(new Konzept("test1",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        instance.getKonzepte().get(0).addSprachrepraesentation(instance.getSprachen().get(1));
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(1)).setBevorzugteBezeichnung("test1");
        if(instance.hatKonzepteOhneBevBez()==true) fail("Kein fehlender BevBezeichner, Methode sagt es fehlt einer.");
        instance.addKonzept(new Konzept("test2",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        if(instance.hatKonzepteOhneBevBez()==false) fail("Fehlender BevBezeichner, Methode erkennt diesen nicht.");
        instance.closeThesaurus();
        
    }
    /**
     * Test of sucheOhneBevBez method, of class Thesaurus.
     */
    @Test
    public void testSucheOhneBevBez() {
        System.out.println("sucheKonzepteOhneBevBez");
        Thesaurus instance =  Thesaurus.createThesaurus(new Sprache("de","Deutsch"),"");
        instance.addSprache(new Sprache("en","English"));
        instance.addKonzept(new Konzept("test1",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        instance.getKonzepte().get(0).addSprachrepraesentation(instance.getSprachen().get(1));
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(1)).setBevorzugteBezeichnung("test1");
        if(instance.sucheOhneBevBezeichnung().size()!=0) fail("Kein fehlender BevBezeichner, Methode sagt es fehlt einer.");
        instance.addKonzept(new Konzept("test2",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        if(instance.sucheOhneBevBezeichnung().size()!=1 || !instance.sucheOhneBevBezeichnung().get(0).equals(instance.getKonzepte().get(1))) {
        	System.out.println(instance.sucheOhneBevBezeichnung().size());
        	fail("Fehlender BevBezeichner, Methode erkennt diesen nicht richtig.");
        }
        instance.addKonzept(new Konzept("test3",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        if(instance.sucheOhneBevBezeichnung().size()!=2 ) fail("2 Fehlende BevBezeichner, Methode erkennt diese nicht richtig.");
        instance.closeThesaurus();
    }

    /**
     * Test of suche method, of class Thesaurus.
     */
    @Test
    public void testSuche() {
        System.out.println("suche");
        Thesaurus instance =  Thesaurus.createThesaurus(new Sprache("de","Deutsch"), "");
        instance.addSprache(new Sprache("en","English"));
        instance.addKonzept(new Konzept("test1",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        instance.getKonzepte().get(0).addSprachrepraesentation(instance.getSprachen().get(1));
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(1)).setBevorzugteBezeichnung("test1");
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(1)).addAltBezeichnung("testing1");
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(1)).addVersteckteBezeichnung("tet1");
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(0)).addAltBezeichnung("testen1");
        instance.getKonzepte().get(0).getSprachrepraesentation(instance.getSprachen().get(0)).addVersteckteBezeichnung("teten1");
        instance.addKonzept(new Konzept("testen1",instance.getHauptSprache(),URI.genKonzeptURI(instance.getBaseDomain(), "blub")));
        instance.getKonzepte().get(1).addSprachrepraesentation(instance.getSprachen().get(1));
        instance.getKonzepte().get(1).getSprachrepraesentation(instance.getSprachen().get(1)).setBevorzugteBezeichnung("testen2");
        instance.getKonzepte().get(1).getSprachrepraesentation(instance.getSprachen().get(1)).addAltBezeichnung("tet1");
        instance.getKonzepte().get(1).getSprachrepraesentation(instance.getSprachen().get(1)).addVersteckteBezeichnung("tet2");
        instance.getKonzepte().get(1).getSprachrepraesentation(instance.getSprachen().get(0)).addAltBezeichnung("testen2");
        instance.getKonzepte().get(1).getSprachrepraesentation(instance.getSprachen().get(0)).addVersteckteBezeichnung("teten2");
        List<Konzept>[] list = instance.suche("tet1", instance.getSprachen().get(1));
        if(list[0].size()!=0 ||list[1].size()!=1 || list[2].size()!=1 || !list[1].get(0).equals(instance.getKonzepte().get(1)) || !list[2].get(0).equals(instance.getKonzepte().get(0))) {
        	fail("Fehler1 (Zwei Konzepte einer Sprache nicht richtig erkannt");
        }
        list = instance.suche("testen1", instance.getSprachen().get(0));
        if(list[0].size()!=1 ||list[1].size()!=1 || list[2].size()!=0 || !list[0].get(0).equals(instance.getKonzepte().get(1)) || !list[1].get(0).equals(instance.getKonzepte().get(0))) {
        	fail("Fehler2 (Zwei Konzepte der Hauptsprache nicht richtig erkannt)");
        }
        list = instance.suche("blub", instance.getSprachen().get(0));
        if(list[0].size()!=0 ||list[1].size()!=0 || list[2].size()!=0 ) {
        	fail("Fehler3 (Nicht existierendes Wort gefunden)");
        }
        list = instance.suche("test1", instance.getSprachen().get(1));
        if(list[0].size()!=1 ||list[1].size()!=0 || list[2].size()!=0 || !list[0].get(0).equals(instance.getKonzepte().get(0))) {
        	System.out.println(list[0].size()+"|"+list[1].size()+"|"+ list[2].size());
        	fail("Fehler4 (Konzept einer NebenSprache nicht richtig erkannt)");
        }
        list = instance.suche("test1", instance.getSprachen().get(0));
        if(list[0].size()!=1 ||list[1].size()!=0 || list[2].size()!=0 || !list[0].get(0).equals(instance.getKonzepte().get(0))) {
        	fail("Fehler5 (Konzept der Hauptsprache nicht richtig erkannt)");
        }
        instance.closeThesaurus();
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