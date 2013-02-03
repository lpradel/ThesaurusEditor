package thesaurusEditor;
/**
 * Enumeration fuer die verschiedenen Bezeichnungstypen der Konzepte.
 * @author sopr058     
 */

public enum BezeichnungsTyp {
	/**
	 * Die 'Ueberschrift' des Konzeptes
	 */
	bevorzugt, 
	/**
	 * Die Synonyme des Konzepts
	 */
	alternativ, 
	/**
	 * Im uebertragenem Sinne auch Synonyme, die aber nicht angezeigt werden und nur orthographische Fehler und Umlaute abfangen sollen
	 */
	versteckt,
	beispiel,
	definition,
	beschreibung,
	/**
	 * Ein Platzhalter
	 */
	kein;

}
