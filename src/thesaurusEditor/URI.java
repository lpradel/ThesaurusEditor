package thesaurusEditor;

import java.io.Serializable;
import java.util.regex.*;

/**
 * URI (Uniform Resource Identifier(Uniformer Ressourcen-Identifizierer)) der einzelnen Konzepte und Schemata.
 * Ermöglicht die gezielte Navigation zu den Eintraegen des Thesaurus.
 * Im SoPra bestehen URIs nur aus Kleinbuchstaben, Ziffern, Unter-, Binde- und Schraegstrichen, exakt einer Raute (#) und dem Praefix http://. 
 *  
 * @author sopr053 
 * @author sopr058
 *
 */
public class URI implements Serializable{
	/**
	 * @param uri der String, der die URI realisiert.
	 */
	private String uri;
	private final static String REGEX = "http://[a-z0-9[\\-_/.]]*#[a-z0-9[\\-_/]]*";
	
    /**
     * Liefert eine Standard-URI fuer ein Schema mit dem angegebenen Bezeichner.
     * 
     * @param s Der String, der fuer die Schema-URI vorgesehen ist.
     * @return  Das Objekt der Form URI.
     */
	public static URI genSchemaURI(String domain, String s) {
		URI uri = new URI();
		
		String ident = s.toLowerCase();
		ident = ident.replaceAll("[^a-z0-9[\\-_/.]]", "");
		
		uri.setUri(domain.toLowerCase() + "schemes#" + ident);
		
		return uri;
	}
	/**
	 * Liefert eine Standard-URI fuer ein Konzept mit dem angegebenen Bezeichner.
	 * 
     * @param s Der String, der fuer die Konzept-URI vorgesehen ist.
	 * @return  Das Objekt der Form URI.
	 */
	public static URI genKonzeptURI(String domain, String s) {
		URI uri = new URI();
		
		String ident = s.toLowerCase();
		ident = ident.replaceAll("[^a-z0-9[\\-_/.]]", "");
		
		uri.setUri(domain.toLowerCase() + "concepts#" + ident);
		
		return uri;
	}
	/**
	 * Es wird ueberprueft ob die URI den SoPra-Konventionen entspricht.
	 * 
	 * @param uri Die zu ueberpruefende URI. 
	 * @return Eine Ausgabe des Typs boolean.
	 */
	public static boolean istZulaessig(URI uri) {
		return istZulaessig(uri.getUri());
	}
	/**
	 * Es wird ueberprueft, ob der vorgesehene String den SoPra-Konventionen entspricht.
	 * 
	 * @param uri Der zu ueberpruefende String.
	 * @return Eine Ausgabe des Typs boolean.
	 */
	public static boolean istZulaessig(String uri) {
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(uri);
		
		return m.matches();
	}
	
	/**
	 * Es wird ueberprueft, ob die angegebene Domain gemaess Aufgabenstellung zulaessig ist.
	 * 
	 * @param domain Zu pruefender Domain-String.
	 * @return true, wenn zulaessig, sonst false.
	 */
	public static boolean checkDomain(String domain) {
		Pattern p = Pattern.compile("http://[a-z0-9[\\-_/.]]*");
		Matcher m = p.matcher(domain);
		
		return m.matches();
	}
	
	/**
	 * Methode, um den String einer URI zu veraendern.
	 * 
	 * @param uri Der gewuensche String.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	/**
	 * Die URI wird zurueckgegeben.
	 * 
	 * @return die URI
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * Methode, um einen String mit dieser URI zu vergleichen.
	 * @param o Der String, der mit dem String dieser URI verglichen werden soll.
	 * @return true, bei Uebereinstimmung, sonst false.
	 */
	public boolean equals(Object o)
	{
		if (this.getClass() != o.getClass())
		{
			if (o instanceof String)
			{
				return ((String)o).equals(this.getUri());
			}
			else
			{
				return false;
			}
		}
		
		return ((URI)o).getUri().equals(this.getUri());
	}
	
	/**
	 * Methode, die ueberprueft, ob zwei Objekte vom Typ URI uebereinstimmen.
	 * @param u Die externe URI, die mit dieser URI verglichen werden soll.
	 * @return true, bei Uebereinstimmung, sonst false.
	 */
	public boolean equals (URI u)
	{	
		if(u==null||u.getUri()==null) return false;
		return (u.getUri().equals(this.getUri()));
	}
	
}
