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
import static org.junit.Assert.*;

/**
 *
 * @author sopr051
 */
public class KonzeptTest {

    public KonzeptTest() {
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
     * Test of hatBevBezeichner method, of class Konzept.
     */
    @Test
    public void testHatBevBezeichner() {
        System.out.println("hatBevBezeichner");
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s, new URI());
        boolean expResult = true;
        boolean result = instance.hatBevBezeichnung(s);
        if(expResult != result){
        	fail("Die hatBevBezeichner-Methode ist fehlgeschlagen!");
        }
    }

    /**
     * Test of addSprachrepraesentation, getSprachrepraesentation(en) methods, of class Konzept.
     */
    @Test
    public void testGetAddSprachrepraesentation() {
        System.out.println("addSprachrepraesentation");
        Sprache s = new Sprache("Fr", "Francais");
        Konzept instance = new Konzept("Pomme de terre",s, new URI());
        instance.addSprachrepraesentation(s);
        List <Sprachrepraesentation> result = instance.getSprachrepraesentationen();
        Sprachrepraesentation expResult = new Sprachrepraesentation(s,"Pomme de terre",BezeichnungsTyp.bevorzugt);
        if(result.isEmpty()|| result.size()>1 || !expResult.equals(result.get(0))){
        	fail("Die addSprachrepraesentation-Methode bzw getSprachrepraesentationen-Methode ist fehlgeschlagen!");
        }
        Sprachrepraesentation otherResult = instance.getSprachrepraesentation(s);
        if(otherResult == null || !otherResult.equals(expResult))
        {
        	fail("Die addSprachrepraesentation-Methode bzw getSprachrepraesentation-Methode ist fehlgeschlagen!");
        }
    }

    /**
     * Test of enthaeltWort method, of class Konzept.
     */
    @Test
    public void testEnthaeltWort() {
        System.out.println("enthaeltWort");
        Sprache s = new Sprache("Fr","Francais");
        String wort = "Pomme de terre";
        Konzept instance = new Konzept(wort,s,new URI());
        BezeichnungsTyp expResult = BezeichnungsTyp.bevorzugt;
        BezeichnungsTyp result = instance.enthaeltWort(s, wort);
        if(!result.equals(expResult)){
        	fail("Die testEnthaeltWort-Methode ist fehlgeschlagen!");
        }
    }

    
    /**
     * Test of getURI and setURI methods, of class Konzept.
     */
    @Test
    public void testSetGetURI() {
        System.out.println("set/getURI");
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s,URI.genKonzeptURI("test", "kartoffel"));
        URI expResult = URI.genKonzeptURI("test", "kartoffel");
        URI result = instance.getURI();
        if(!expResult.equals(result)){
        	fail("Die getURI-Methode ist fehlgeschlagen!");
        }
        instance.setURI(expResult);
        result = instance.getURI();
        if(!expResult.equals(result)){
        	fail("Die setURI-Methode ist fehlgeschlagen!");
        }
    }
   


    /**
     * Test of addSchema and getSchema methods, of class Konzept.
     */
    @Test
    public void testAddGetSchema() {
        System.out.println("add/getSchema");
        Schema schema1 = new Schema("schema1",URI.genKonzeptURI("domain", "schema1"));
        Schema schema2 = new Schema("schema2",URI.genKonzeptURI("domain", "schema2"));
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s,new URI());
        instance.addSchema(schema1);
        instance.addSchema(schema2);
        List <Schema> result = instance.getSchemata();
        if(!(result.contains(schema1) && result.contains(schema2))){
        	fail("Die addSchema-Methode ist fehlgeschlagen!");
        }
    }

    /**
     * Test of removeSchema method, of class Konzept.
     */
    @Test
    public void testRemoveSchema() {
        System.out.println("removeSchema");
        Schema schema = new Schema("schema",URI.genSchemaURI("domain", "schema"));
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s,URI.genKonzeptURI("domain", "konzept"));
        instance.addSchema(schema);
        instance.removeSchema(schema);
        List <Schema> result = instance.getSchemata();
        if(!result.isEmpty()){
        	fail("Die removeSchema-Methode ist fehlgeschlagen!");
        }
    }

    /**
     * Test of getGeneralisierungen, removeGeneralisierung and addGeneralisierung methods of class Konzept.
     */
    @Test
    public void testAddGetRemoveGeneralisierungen() {
        System.out.println("getGeneralisierungen/addGeneralisierung/removeGeneralisierung");
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s,URI.genKonzeptURI("domain", "schema1"));
        Konzept generalisierung = new Konzept("Legumes",s,URI.genKonzeptURI("domain", "schema2"));
        if(!instance.getGeneralisierungen().isEmpty()){
        	fail("Generalisierungs-Liste sollte leer sein");
        }
        instance.addGeneralisierung(generalisierung);
        if(!(instance.getGeneralisierungen().size()==1 && instance.getGeneralisierungen().get(0).equals(generalisierung)))
        {
        	fail("AddGeneralisierung schlug fehl!");
        }
        instance.removeGeneralisierung(generalisierung);
        if(!instance.getGeneralisierungen().isEmpty())
        {
        	fail("Generalisierung wurde nicht richtig geloescht!");
        }
    }

    /**
     * Test of getSpezialisierungen and addSpezialisierung methods, of class Konzept.
     */
    @Test
    public void testAddGetRemoveSpezialisierungen() {
        System.out.println("getSpezialisierungen/addSpezialisierung");
        Sprache s = new Sprache("Fr","Francais");
        Konzept instance = new Konzept("Pomme de terre",s,URI.genKonzeptURI("domain", "schema1"));
        Konzept Spezialisierung = new Konzept("Legumes",s,URI.genKonzeptURI("domain", "schema2"));
        if(!instance.getSpezialisierungen().isEmpty()){
        	fail("Spezialisierungs-Liste sollte leer sein");
        }
        instance.addSpezialisierung(Spezialisierung);
        if(!(instance.getSpezialisierungen().size()==1 && instance.getSpezialisierungen().get(0).equals(Spezialisierung)))
        {
        	fail("AddSpezialisierung schlug fehl!");
        }
        instance.removeSpezialisierung(Spezialisierung);
        if(!instance.getSpezialisierungen().isEmpty())
        {
        	fail("Spezialisierung wurde nicht richtig geloescht!");
        }
    }
}