/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thesaurusEditor.Konzept;
import thesaurusEditor.Schema;
import thesaurusEditor.Sprache;
import thesaurusEditor.URI;
import static org.junit.Assert.*;

/**
 *
 * @author sopr051
 */
public class SchemaTest {

    public SchemaTest() {
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
     * Test of setURI method, of class Schema.
     */
    @Test
    public void testSetURI() {
        System.out.println("setURI");
        
        try {
        	URI lnkURI = new URI();
        	lnkURI.genKonzeptURI("haribo", "http://example.org");
        	
        	Schema instance = new Schema("Haustier", lnkURI);
            
            if ( instance.getURI().getUri() != lnkURI.getUri() )
            {
            	fail("setURI wird falsch ausgefuehrt.");
            }
        } catch (Exception e) {
        	fail("Exception in setURI");
        }
    }

    /**
     * Test of getURI method, of class Schema.
     */
    @Test
    public void testGetURI() {
        System.out.println("getURI");
        
        try {
        	URI lnkURI = new URI();
        	lnkURI.genKonzeptURI("haribo", "http://example.org");
        	Schema instance = new Schema("Haustier", lnkURI);
        	
        	if ( instance.getURI() != lnkURI )
        	{
        		fail("getURI oder setURI wird falsch ausgefuehrt.");
        	}
        } catch (Exception e) {
        	fail("Exception in getURI");
        }
    }

    /**
     * Test of addTopKonzept method, of class Schema.
     */
    @Test
    public void testAddTopKonzept() {
        System.out.println("addTopKonzept");
        
        try {
        	URI lnkURI = new URI();
        	lnkURI = URI.genKonzeptURI("haribo", "http://example.org");
            Konzept topKonzept = new Konzept("Tolles Konzept", new Sprache("DE", "Deutsch"), lnkURI);
            Schema instance = new Schema("haribo", lnkURI);
            instance.addTopKonzept(topKonzept);
            
            List<Konzept> alleKonzepte = instance.getTopKonzepte();
            boolean gefunden = false;
            for (int i = 0; i < alleKonzepte.size(); i++)
            {
            	if ( gefunden == false && alleKonzepte.get(i) == topKonzept )
            	{
            		gefunden = true;
            	}
            }
            
            if ( ! gefunden )
            {
            	fail("addTopKonzept wird falsch ausgefuehrt.");
            }
            
        } catch (Exception e) {
        	fail("Exception in addTopKonzept.");
        }
    }

    /**
     * Test of removeTopKonzept method, of class Schema.
     */
    @Test
    public void testRemoveTopKonzept() {
        System.out.println("removeTopKonzept");
        
        try {
        	URI lnkURI = new URI();
        	lnkURI = URI.genKonzeptURI("haribo", "http://example.org");
        	Konzept topKonzept = new Konzept("Tolles Konzept", new Sprache("DE", "Deutsch"), lnkURI);
        	
            Schema instance = new Schema("haribo", lnkURI);
            instance.addTopKonzept(topKonzept);
            instance.addTopKonzept(topKonzept);
            instance.removeTopKonzept(topKonzept);
            
            List<Konzept> alleKonzepte = instance.getTopKonzepte();
            int zaehler = 0;
            for (int i = 0; i < alleKonzepte.size(); i++)
            {
            	if ( alleKonzepte.get(i) == topKonzept )
            	{
            		zaehler++;
            	}
            }
            
            if ( zaehler == 2 )
            {
            	fail("removeTopKonzept funktioniert gar nicht!");
            } else if ( zaehler == 1 )
            {
            	fail("removeTopKonzept funktioniert ein bisschen!");
            } else if ( zaehler != 0 )
            {
            	fail("removeTopKonzept funktioniert anscheinend nicht.");
            }
        } catch (Exception e) {
        	fail("Exception in removeTopKonzept.");
        }
    }
    
}