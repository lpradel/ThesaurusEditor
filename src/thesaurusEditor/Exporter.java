package thesaurusEditor;

/* Imports */
import thesaurusEditor.ExportException;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;

/* XML-Imports */
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 * Exportierer-Schnittstelle fuer den Thesaurus. Kapselt das RDF/XML-Exportieren
 * in diese Klasse ab. Bietet die statische Schnittstelle export, um ein
 * Thesaurus-Objekt an den angegebenen Pfad in eine RDF/XML-Datei zu
 * exportieren.
 * 
 * @author sopr053
 *
 */
public class Exporter implements Serializable {
	
	/* XML-Konstanten */
	private final static String XML_DOCTYPE_SYSTEM = "";
	private final static String XML_DOCTYPE_PUBLIC = "";
	
	/* RDF/XML-Konstanten */
	private final static String RDF_ROOT = "rdf:RDF";
	private final static String RDF_LINK_ATTR = "xmlns:rdf";
	private final static String RDF_LINK_VAL = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private final static String RDF_URI = "rdf:resource";
	private final static String RDF_LANG = "xml:lang";
	private final static String SKOS_LINK_ATTR = "xmlns:skos";
	private final static String SKOS_LINK_VAL = "http://purl.org/dc/elements/1.1/";
	private final static String SKOS_CONCEPT = "skos:Concept";
	private final static String SKOS_SCHEME = "skos:ConceptScheme";
	private final static String SKOS_GENERALISIERUNG = "skos:broaderTransitive";
	private final static String SKOS_SPEZIALISIERUNG = "skos:narrowerTransitive";
	private final static String SKOS_PREF_LABEL = "skos:prefLabel";
	private final static String SKOS_ALT_LABEL = "skos:altLabel";
	private final static String SKOS_HIDDEN_LABEL = "skos:hiddenLabel";
	private final static String SKOS_DEFINITION = "skos:definition";
	private final static String SKOS_DESCRIPTION = "skos:scopeNote";
	private final static String SKOS_EXAMPLE = "skos:example";
	
	
	/**
	 * Exportiert das Thesaurus-Objekt t in eine RDF/XML-Format-Datei
	 * am angegebenen Pfad.
	 * 
	 * @param pfad Der Pfad zur Datei, in die exportiert werden soll.
	 * @param t Das Thesaurus-Objekt, welches exportiert werden soll.
	 * 
	 * @author sopr053
	 */
	public static void export(String pfad, Thesaurus t) throws ExportException {
		/**
		 * 
		 * Inits und Variablen
		 * 
		 */
		
		/* Variablen fuer XML-Output */
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		
		/* Erzeugen des Dokuments */
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		}
		catch (Exception e)
		{
			throw new ExportException("Fehler beim Erzeugen des XML-Files!");
		}
		
		/**
		 * 
		 * Erzeugen der XML-Struktur
		 * 
		 */
		
		/* XML-Version */
		document.setXmlVersion("1.0");
		
		/*
         * Wurzel-Element: rdf
         * */
        Element rdf = document.createElement(RDF_ROOT);
        rdf.setAttribute(RDF_LINK_ATTR, RDF_LINK_VAL);
        rdf.setAttribute(SKOS_LINK_ATTR, SKOS_LINK_VAL);
        
        document.appendChild(rdf);
        
        /*
         * Konzepte
         * */
        List<Konzept> konzepte = t.getKonzepte();
        
        /* Ueber alle Konzepte iterieren */
        for (Konzept k : konzepte)
        {
        	Element konzept = document.createElement(SKOS_CONCEPT);
        	
        	/* URI */
        	konzept.setAttribute("rdf:about", k.getURI().getUri());
        	
        	/*
        	 * zugeordnete Schemata
        	 * */
        	List<Schema> schemataZuKonzept = k.getSchemata();
        	for (Schema s : schemataZuKonzept)
        	{
        		Element schemaZuKonzept = document.createElement("skos:inScheme");
        		schemaZuKonzept.setAttribute(RDF_URI, s.getURI().getUri());
        		
        		/* Uebernehmen */
        		konzept.appendChild(schemaZuKonzept);
        	}
        	
        	/*
        	 * Generalisierungen, broader
        	 * */
        	List<Konzept> generalisierungen = k.getGeneralisierungen();
        	for (Konzept g : generalisierungen)
        	{
        		Element generalisierung = document.createElement(SKOS_GENERALISIERUNG);
        		generalisierung.setAttribute(RDF_URI, g.getURI().getUri());	/* URI */
        		
        		/* Uebernehmen */
        		konzept.appendChild(generalisierung);
        	}
        	
        	/*
        	 * Spezialisierungen, narrower
        	 * */
        	List<Konzept> spezialisierungen = k.getSpezialisierungen();
        	for (Konzept sp : spezialisierungen)
        	{
        		Element spezialisierung = document.createElement(SKOS_SPEZIALISIERUNG);
        		spezialisierung.setAttribute(RDF_URI, sp.getURI().getUri());
        		
        		/* Uebernehmen */
        		konzept.appendChild(spezialisierung);
        	}
        	
        	/*
        	 * Bezeichner
        	 * */
        	List<Sprachrepraesentation> sprachreps = k.getSprachrepraesentationen();
        	for (Sprachrepraesentation rep : sprachreps)
        	{
        		Sprache rep_sprache = rep.getSprache();	/* fuer alle gleich */
        		
        		/* Bevorzugte Bezeichnung */
        		if (!rep.getBevorzugteBezeichnung().isEmpty())
        		{
        			Element bevorzugterBezeichner = document.createElement(SKOS_PREF_LABEL);
        			bevorzugterBezeichner.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* Bezeichner-Wert setzen */
        			Text bevBezText = document.createTextNode(rep.getBevorzugteBezeichnung());

        			/* Uebernehmen */
        			bevorzugterBezeichner.appendChild(bevBezText);
        			konzept.appendChild(bevorzugterBezeichner);
        		}
        		
        		/* Alternative Bezeichnungen */
        		List<String> altBezeichner = rep.getAltBezeichnungen();
        		for (String altBez : altBezeichner)
        		{
        			Element alternativerBezeichner = document.createElement(SKOS_ALT_LABEL);
        			alternativerBezeichner.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* altBez-Wert setzen */
        			Text altBezText = document.createTextNode(altBez);
        			
        			/* Uebernehmen */
        			alternativerBezeichner.appendChild(altBezText);
        			konzept.appendChild(alternativerBezeichner);
        		}
        		
        		/* Versteckte Bezeichnungen */
        		List<String> hiddenBezeichner = rep.getVersteckteBezeichnungen();
        		for (String hiddenBez : hiddenBezeichner)
        		{
        			Element versteckerBezeichner = document.createElement(SKOS_HIDDEN_LABEL);
        			versteckerBezeichner.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* hidBez-Wert setzen */
        			Text hidBezText = document.createTextNode(hiddenBez);
        			
        			/* Uebernehmen */
        			versteckerBezeichner.appendChild(hidBezText);
        			konzept.appendChild(versteckerBezeichner);
        		}
        		
        		/* Definition */
        		if (!rep.getDefinition().isEmpty())
        		{
        			Element definition = document.createElement(SKOS_DEFINITION);
        			definition.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* Definition-Wert setzen */
        			Text defText = document.createTextNode(rep.getDefinition());

        			/* Uebernehmen */
        			definition.appendChild(defText);
        			konzept.appendChild(definition);
        		}
        		
        		/* Beschreibung */
        		if (!rep.getBeschreibung().isEmpty())
        		{
        			Element beschreibung = document.createElement(SKOS_DESCRIPTION);
        			beschreibung.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* Beschreibungs-Wert setzen */
        			Text beschrText = document.createTextNode(rep.getBeschreibung());
        			
        			/* Uebernehmen */
        			beschreibung.appendChild(beschrText);
        			konzept.appendChild(beschreibung);
        		}
        		
        		/* Beispiel */
        		if (!rep.getBeispiel().isEmpty())
        		{
        			Element beispiel = document.createElement(SKOS_EXAMPLE);
        			beispiel.setAttribute(RDF_LANG, rep_sprache.getKuerzel());
        			/* Beispiel-Wert setzen */
        			Text bsplText = document.createTextNode(rep.getBeispiel());
        			
        			/* Uebernehmen */
        			beispiel.appendChild(bsplText);
        			konzept.appendChild(beispiel);
        		}
        		
        		
        	}
        	
        	/* Uebernehmen */
        	rdf.appendChild(konzept);
        }
        /*/ ------------- Konzepte ------------- /*/
        
        
        /*
         * Schemata
         * */
        List<Schema> schemata = t.getSchemata();
        
        /* Default-Schema */
        schemata.add(0, t.getDefaultSchema());       
        
        /* Ueber alle Schemata iterieren */
        for (Schema s : schemata)
        {
        	Element schema = document.createElement(SKOS_SCHEME);
        	
        	/* URI */
        	schema.setAttribute("rdf:about", s.getURI().getUri());
        	
        	/* Topkonzepte */
        	List<Konzept> topkonzepte = s.getTopKonzepte();
        	for (Konzept tk : topkonzepte)
        	{
        		Element topkonzept = document.createElement("skos:hasTopConcept");
        		topkonzept.setAttribute(RDF_URI, tk.getURI().getUri());
        		
        		/* Uebernehmen */
        		schema.appendChild(topkonzept);
        	}
        	
        	/* Name / Beschreibung */
        	Element beschreibung = document.createElement(SKOS_DESCRIPTION);
        	/* TODO: Sprache der Beschreibung! */
        	Text beschrText = document.createTextNode(s.getName());
        	beschreibung.appendChild(beschrText);
        	schema.appendChild(beschreibung);
        	
        	/* Uebernehmen */
        	rdf.appendChild(schema);
        }
		
        /*/ ------------- Schemata ------------- /*/
        
		/**
		 * 
		 * XML-Output in Datei
		 * 
		 */
		try {
        	/* Transformer erzeugen */
        	TransformerFactory transfac = TransformerFactory.newInstance();
        	transfac.setAttribute("indent-number", 4);
        	Transformer trans = transfac.newTransformer();
        	//trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, XML_DOCTYPE_SYSTEM);
        	//trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, XML_DOCTYPE_PUBLIC);
        	trans.setOutputProperty(OutputKeys.INDENT, "yes");
        	trans.setOutputProperty(OutputKeys.METHOD, "xml");
        	trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        	/* XML-file in String speichern */
        	StringWriter sw = new StringWriter();
        	StreamResult result = new StreamResult(sw);
        	DOMSource source = new DOMSource(document);
        	trans.transform(source, result);
        	String xmlString = sw.toString();

        	/* Ausgabe */
        	// quick-n-dirty fix fuer newline
        	xmlString = xmlString.replace("<rdf:RDF", "\n<rdf:RDF");
        	// TODO:rm
        	//System.out.println("XML-Output:\n\n" + xmlString);
        	
        	
        	/* Datei-Ausgabe */
        	FileWriter fstream = new FileWriter(pfad);
        	BufferedWriter out = new BufferedWriter(fstream);
        	out.write(xmlString);
        	/* Output-Stream schliessen */
        	out.close();
        }
        catch (Exception e)
        {
        	throw new ExportException("Fehler beim Speichern in Datei!");
        }
	}
}
