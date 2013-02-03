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
import static org.junit.Assert.*;

/**
 *
 * @author sopr051
 */
public class URITest {

    public URITest() {
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
     * Test of genSchemaURI method, of class URI.
     */
    @Test
    public void testGenSchemaURI() {
        System.out.println("genSchemaURI");
        
        String domain = "http://meinedomain.de/";
        String s1 = "SCHEMALOL";
        String s2 = "Hmm Sonderzeichen^~";
        
        if (!URI.istZulaessig(URI.genSchemaURI(domain, s1)))
        	fail("FAIL!");
        
        if (!URI.istZulaessig(URI.genSchemaURI(domain, s2)))
        	fail("FAIL!");
    }

    /**
     * Test of genKonzeptURI method, of class URI.
     */
    @Test
    public void testGenKonzeptURI() {
        System.out.println("genKonzeptURI");
        
        String domain = "http://roffel.com/";
        String s1 = "KONZEPTLOL";
        String s2 = "Hmm Sonderzeichen^~";
        
        if (!URI.istZulaessig(URI.genKonzeptURI(domain, s1)))
        	fail("FAIL!");
        
        if (!URI.istZulaessig(URI.genKonzeptURI(domain, s2)))
        	fail("FAIL!");
    }

    /**
     * Test of istZulaessig method, of class URI.
     */
    @Test
    public void testIstZulaessig_URI() {
        System.out.println("istZulaessig");
        
        /* Test-Strings */
        URI testuris[] = new URI[10];
        
        for (int i = 0; i < 10; i++)
        	testuris[i] = new URI();
        
        testuris[0].setUri("blaa");
        testuris[1].setUri("keinhttp");
        testuris[2].setUri("http://rautefehlt");
        testuris[3].setUri("http://Grossbuchstabe#");
        testuris[4].setUri("http://0912#^unzulaessigeszeichenlol");
        testuris[5].setUri("http://testez4hlen_hier/und-jetzt/#blub");
        
        /* Unzulaessige pruefen */
        for (int i = 0; i < 5; i++)
        {
        	if (URI.istZulaessig(testuris[i]))
        		fail("Testuri # " + i +" : Diese URI ist nicht zulaessig!");
        }
        
        
        /* Zulaessige pruefen */
        if (!URI.istZulaessig(testuris[5]))
        	fail ("Testuri # 6 sollte zulaessige sein!");
    }

    /**
     * Test of istZulaessig method, of class URI.
     */
    @Test
    public void testIstZulaessig_String() {
        System.out.println("istZulaessig");
        
        /* Test-Strings */
        String testuris[] = new String[10];
        
        for (int i = 0; i < 10; i++)
        	testuris[i] = new String();
        
        testuris[0]="blaa";
        testuris[1]="keinhttp";
        testuris[2]="http://rautefehlt";
        testuris[3]="http://Grossbuchstabe#";
        testuris[4]="http://0912#^unzulaessigeszeichenlol";
        testuris[5]="http://testez4hlen_hier/und-jetzt/#blub";
        
        /* Unzulaessige pruefen */
        for (int i = 0; i < 5; i++)
        {
        	if (URI.istZulaessig(testuris[i]))
        		fail("Testuri # " + i +" : Diese URI ist nicht zulaessig!");
        }
        
        
        /* Zulaessige pruefen */
        if (!URI.istZulaessig(testuris[5]))
        	fail ("Testuri # 6 sollte zulaessige sein!");
    }
    
    /**
     * Test of checkDomain method, of class URI.
     */
    @Test
    public void testCheckDomain() {
    	System.out.println("checkDomain");
    	
    	String dom1 = "asdahsdoi";
    	String dom2 = "http://unzulaessigessonderzeichen^~";
    	String dom3 = "http://das_ist-zulaessig.de/";
    	
    	if (URI.checkDomain(dom1))
    	{
    		fail("Die Domain ist unzulaessig!");
    	}
    	
    	if (URI.checkDomain(dom2))
    	{
    		fail("Die Domain ist unzulaessig!");
    	}
    	
    	if (!URI.checkDomain(dom3))
    	{
    		fail("Die Domain sollte OK sein!");
    	}
    }

    /**
     * Test of setUri method, of class URI.
     */
    @Test
    public void testSetUri() {
        System.out.println("setUri");
        
        URI uri = new URI();
        uri.setUri("blub");
        
        if (!uri.getUri().equals("blub"))
        	fail("FAIL!");
    }

    /**
     * Test of getUri method, of class URI.
     */
    @Test
    public void testGetUri() {
        System.out.println("getUri");
        
        URI uri = new URI();
        uri.setUri("blub");
        
        if (!uri.getUri().equals("blub"))
        	fail("FAIL!");
    }

}