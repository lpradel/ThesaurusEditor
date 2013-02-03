package thesaurusEditor;

/**
 * Exception-Klasse fuer Fehler beim Exportieren eines Thesaurus in RDF/XML.
 * 
 * @author sopr053
 *
 */
public class ExportException extends Exception {
	public ExportException (String arg0)
	{
		super(arg0);
	}
}
