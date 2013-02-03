//package thesaurusEditor;



package thesaurusEditor;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thesaurusEditor.BezeichnungsTyp;
import thesaurusEditor.Sprache;
import thesaurusEditor.Sprachrepraesentation;
import static org.junit.Assert.*;

/**
 *
 * @author sopr051
 */
public class SprachrepraesentationTest {

    public SprachrepraesentationTest() {
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
     * Test of enthaeltWort method, of class Sprachrepraesentation.
     */
    @Test
    public void testEnthaeltWort() {
    	// Test ob enthalten, dabei wurde das Wort als jeden Typ einmal eingefügt. Test ob nicht enthalten wenn nicht eingefügt
    	
    	// Test falls nur ein Element enthalten
        System.out.println("enthaeltWort");
        String wort = "Hund";
        Sprachrepraesentation instance = new Sprachrepraesentation(new Sprache("DE","Deutsch"),wort, BezeichnungsTyp.bevorzugt);
        BezeichnungsTyp expResult = BezeichnungsTyp.bevorzugt;
        BezeichnungsTyp result = instance.enthaeltWort(wort);
        assertEquals(expResult, result);
        
        // Test für alternatvien Bezeichner
        wort = "Hund";
        instance = new Sprachrepraesentation(new thesaurusEditor.Sprache("DE","Deutsch"),wort, BezeichnungsTyp.alternativ);
        expResult = BezeichnungsTyp.alternativ;
        result = instance.enthaeltWort(wort);
        assertEquals(expResult, result);
        
        // Test als versteckter Bezeichner 
        instance = new Sprachrepraesentation(new Sprache("DE","Deutsch"),wort, BezeichnungsTyp.versteckt);
        expResult = BezeichnungsTyp.versteckt;
        result = instance.enthaeltWort(wort);
        assertEquals(expResult, result);
        
        // Test wenn nicht enthalten
        expResult = BezeichnungsTyp.kein;
        result = instance.enthaeltWort("Dog");
        assertEquals(expResult, result);
        
        // Test wenn mehr Elemente enthalten
        instance = new Sprachrepraesentation(new Sprache("DE","Deutsch"),wort, BezeichnungsTyp.bevorzugt);
        instance.addAltBezeichnung("WauWau");
        instance.addAltBezeichnung("Köter");
        instance.addAltBezeichnung("Töle");
        instance.addVersteckteBezeichnung("Huund");
        instance.addVersteckteBezeichnung("Hunnd");
        instance.addVersteckteBezeichnung("Hunddd");
        instance.addVersteckteBezeichnung("Hunde");
        
        expResult = BezeichnungsTyp.bevorzugt;
        result = instance.enthaeltWort("Hund"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.alternativ;
        result = instance.enthaeltWort("Töle"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.alternativ;
        result = instance.enthaeltWort("Köter"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.alternativ;
        result = instance.enthaeltWort("WauWau"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.versteckt;
        result = instance.enthaeltWort("Hunnd"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.versteckt;
        result = instance.enthaeltWort("Huund"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.versteckt;
        result = instance.enthaeltWort("Hunddd"); 
        assertEquals(expResult, result);
        
        expResult = BezeichnungsTyp.kein;
        result = instance.enthaeltWort("Dog"); 
        assertEquals(expResult, result);
    }



    @Test
    public void testSetBevorzugteBezeichnung() {
        System.out.println("setBevorzugteBezeichnung");
        String bevorzugt = "Hund";
        Sprachrepraesentation instance = new Sprachrepraesentation(new Sprache("DE", "Deutsch"));
        instance.setBevorzugteBezeichnung(bevorzugt);
        String result = instance.getBevorzugteBezeichnung();
        String expResult = "Hund";
        assertEquals(expResult, result);
        
        instance.setBevorzugteBezeichnung("Köter");
        result = instance.getBevorzugteBezeichnung();
        expResult = "Köter";
        assertEquals(expResult, result);
        
        boolean resultB = instance.getAltBezeichnungen().contains("Hund");
        assertTrue(resultB);
    }

    /**
     * Test of getAltBezeichnungen method, of class Sprachrepraesentation.
     */
    @Test
    public void testGetAltBezeichnungen() {
        System.out.println("getAltBezeichnungen");
        String wort = "Hund";
        Sprachrepraesentation instance = new Sprachrepraesentation(new Sprache("DE","Deutsch"),wort, BezeichnungsTyp.alternativ);
        instance.addAltBezeichnung("Köter");
        instance.addAltBezeichnung("WauWau");
        instance.addAltBezeichnung("Töle");
        List<String> expResult = new ArrayList<String>();
        expResult.add("Hund");
        expResult.add("Köter");
        expResult.add("WauWau");
        expResult.add("Töle");
        List<String> result = instance.getAltBezeichnungen();
        boolean b = result.containsAll(expResult);
        assertTrue(b);
        
        expResult = instance.getAltBezeichnungen();
        expResult.add("Dog");
        result = instance.getAltBezeichnungen();
        b = result.containsAll(expResult);
        assertFalse(b);
    }

    /**
     * Test of addAltBezeichnung method, of class Sprachrepraesentation.
     */
    @Test
    public void testAddAltBezeichnung() {
    	System.out.println("AddAltBezeichnungen");
    	String wort = "Hund";
        System.out.println("addAltBezeichnung");
        Sprachrepraesentation instance = new Sprachrepraesentation(new Sprache("DE","Deutsch"),wort, BezeichnungsTyp.alternativ);
        instance.addAltBezeichnung("Köter");
        instance.addAltBezeichnung("WauWau");
        instance.addAltBezeichnung("Töle");
        List<String> expResult = new ArrayList<String>();
        expResult.add("Hund");
        expResult.add("Köter");
        expResult.add("WauWau");
        expResult.add("Töle");
        List<String> result = instance.getAltBezeichnungen();
        boolean b = result.containsAll(expResult);
        assertTrue(b);
        
        instance.addAltBezeichnung("Hund");
        result = instance.getAltBezeichnungen();
        b = result.containsAll(expResult); 
        assertTrue(b);
    }


   // Test von AltBezeichnung zu Versteckte Bezeichnung äquivalent, auch auf CopyPaste Fehler im Code geprüft

}