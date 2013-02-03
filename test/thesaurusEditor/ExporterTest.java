/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thesaurusEditor.*;
import static org.junit.Assert.*;

/**
 *
 * @author sopr051
 */
public class ExporterTest {
	
	static Thesaurus thesaurus;

    public ExporterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    	
    	/* paar Sprachen */
    	Sprache sp = new Sprache("de", "Deutsch");
    	Sprache sp2 = new Sprache("en", "English");
    	
    	/* paar Konzepte */
    	Konzept k = new Konzept("LOL", sp, URI.genKonzeptURI("http://test.de/", "LOL"));
    	k.addSprachrepraesentation(sp2);
    	k.addBezeichnung("Laughing out Loud!", sp2, BezeichnungsTyp.bevorzugt);
    	k.addBezeichnung("alternativer, deutscher Bezeichner", sp, BezeichnungsTyp.alternativ);
    	k.addBezeichnung("versteckter, englischer Bezeichner", sp2, BezeichnungsTyp.versteckt);
    	k.getSprachrepraesentation(sp).setBeispiel("deutsches Beispiel, ROFL!");
    	k.getSprachrepraesentation(sp).setBeschreibung("deutsche Beschreibung :D");
    	k.getSprachrepraesentation(sp2).setBeschreibung("english description :D");
    	k.getSprachrepraesentation(sp2).setDefinition("english definition :O");
    	
    	Konzept k2 = new Konzept("second Concept^^", sp2, URI.genKonzeptURI("http://test.de/", "second Concept^^"));
    	k2.addGeneralisierung(k);
    	
    	k.addSpezialisierung(k2);
    	
    	/* Test-Thesaurus anlegen */
    	thesaurus = Thesaurus.createThesaurus(sp, "http://test.de/");
    	thesaurus.addKonzept(k);
    	thesaurus.addKonzept(k2);
    	thesaurus.getDefaultSchema().addTopKonzept(k);
    	thesaurus.getDefaultSchema().addTopKonzept(k2);
    	
    	thesaurus.closeThesaurus();
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
     * Test of export method, of class Exporter.
     */
    @Test
    public void testExport() {
        System.out.println("export");
        
        String pfad = "/sopra/sopgr05/sopr053/workspace/ThesaurusEditor/out.xml";
        
        /* Export-Test 
         * 1. Schritt: Exportieren, sollte funktionieren */
        try {
        	Exporter.export(pfad, thesaurus);
        }
        catch (ExportException e)
        {
        	fail("Das Exportieren sollte funktionieren!");
        }
        
        /* 2. Schritt: Tatsächlichen Datei-Inhalt mit Soll vergleichen */
    }

}