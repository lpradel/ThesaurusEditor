/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * KonzeptAnsicht.java
 *
 * Created on Mar 21, 2011, 3:04:03 PM
 */
package thesaurusEditor.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListDataEvent;
import thesaurusEditor.BezeichnungsTyp;
import thesaurusEditor.Controller;
import thesaurusEditor.Konzept;
import thesaurusEditor.Schema;
import thesaurusEditor.Sprache;
import thesaurusEditor.Sprachrepraesentation;

/**
 * Generiert und verwaltet das Fenster eines Konzeptes
 * 
 * @author David+Korbi
 */
public class KonzeptAnsicht extends javax.swing.JPanel {

    private Konzept aktuellesKonzept;
    private List<Konzept> genAdded, genRemoved, spezAdded, spezRemoved;
    private List<Schema> schemaAdded, schemaRemoved;
    private Main main;
    private Controller ctrl;
    private boolean changed;
    private JList lstAddTo;
    private String alterName;
    // Suchen:
    private Date date = null;
    private boolean geklickt = false;
    private List<Konzept>[] suchErgebnisse;

    // Suchen ENDE
    /**
     * Generiert das Fenster und laed in die Klasse alle benoetigten Sachen wie
     * den Controller oder die Main. Darueber hinaus wird der Tab "Deutsch" den
     * wir drin gelassen haben entfernt (ueber diesen ist eine einfache
     * Moeglichkeit des Änderns der Tabs gegeben).
     *
     * @param main
     * @param ctrl
     * @param k
     */
    public KonzeptAnsicht(Main main, Controller ctrl, Konzept k) {
	this.main = main;
	this.ctrl = ctrl;
	this.changed = false;
	this.initComponents();

	// Wir haben beschlossen die alte Ansicht erstmal drin zu lassen,
	// damit diese im Zweifelsfall bearbeitet werden kann. Deshalb diese
	// hier löschen.
	this.tabSprache.remove(this.tabSprache.getComponent(1));

	this.genAdded = new ArrayList<Konzept>();
	this.genRemoved = new ArrayList<Konzept>();
	this.spezAdded = new ArrayList<Konzept>();
	this.spezRemoved = new ArrayList<Konzept>();
	this.schemaAdded = new ArrayList<Schema>();
	this.schemaRemoved = new ArrayList<Schema>();
	this.ladeKonzept(k);
	this.tabSprache.setSelectedIndex(1);

	// Suchen
	List<Sprache> sprachen = this.ctrl.getThesaurus().getSprachen();
	sprachen.add(HauptAnsicht.alleSprachen);
	cbSprache.setModel(new javax.swing.DefaultComboBoxModel(sprachen.toArray()));
	// Suchen ENDE
    }

    /**
     * Generiert aus einer Sprachrepraesentation einen Tab in der Mitte der
     * Konzeptansicht
     *
     * @param s
     */
    private void addTab(Sprachrepraesentation s) {
	this.tabSprache.addTab(s.getSprache().getSprache(), new SprachTab(s));
    }

    /**
     * Hier wird eigentlich erst das Konzept in die Ansicht geladen (es wird
     * durch die Sprachen des Thesaurus durchiteriert und dann mit den
     * vorhandenden Daten der Sprachrepraesentationen gefuellt).
     *
     * @param konzept
     */
    private void ladeKonzept(Konzept konzept) {
	this.aktuellesKonzept = konzept;

	for (Sprache s : ctrl.getThesaurus().getSprachen()) {
	    boolean found = false;
	    for (Sprachrepraesentation sprachrep : konzept.getSprachrepraesentationen()) {
		if (s.equals(sprachrep.getSprache())) {
		    this.addTab(sprachrep);
		    found = true;
		}
	    }
	    if (!found) {
		konzept.addSprachrepraesentation(s);
		this.addTab(konzept.getSprachrepraesentation(s));
	    }
	}
	this.alterName = konzept.toString();

	this.lstExistentSchemata.setModel(new SchemaModel(this.ctrl.getThesaurus().getSchemata()));
	this.lstRelatedSchemata.setModel(new SchemaModel(this.aktuellesKonzept.getSchemata()));

	//Start: Tooltip einfügen
	this.lstGeneralisierungen = new javax.swing.JList() {
	    // This method is called as the cursor moves within the list.

	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		// Get item index
		int index = locationToIndex(evt.getPoint());

		// Get item
		Object item = getModel().getElementAt(index);

		// Return the tool tip text
		return ((Konzept) item).getToolTipText();
	    }
	};
	//GUI neu setzen
	lstGeneralisierungen.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGeneralisierungenValueChanged(evt);
            }
        });
        scrGeneralisierungen.setViewportView(lstGeneralisierungen);
	// End ToolTipEinfügen
	this.lstGeneralisierungen.setModel(new KonzeptModel(
		this.aktuellesKonzept.getGeneralisierungen()));

	//Start: Tooltip einfügen
	this.lstSpezialisierungen = new javax.swing.JList() {
	    // This method is called as the cursor moves within the list.

	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		// Get item index
		int index = locationToIndex(evt.getPoint());

		// Get item
		Object item = getModel().getElementAt(index);

		// Return the tool tip text
		return ((Konzept) item).getToolTipText();
	    }
	};
	//GUI neu setzen
	 lstSpezialisierungen.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSpezialisierungenValueChanged(evt);
            }
        });
        scrSpezialisierungen.setViewportView(lstSpezialisierungen);
	// End ToolTipEinfügen
	this.lstSpezialisierungen.setModel(new KonzeptModel(
		this.aktuellesKonzept.getSpezialisierungen()));
	this.lblKonzept.setText(konzept.toString());

    }

    /**
     * Wenn OK oder Anwenden betaetigt wird, dann werden die entsprechenden
     * Generalisierungen und Spezialisierungen (aus den Listen mit den
     * Aenderungen) und saemtliche Inhalte aus den Sprachtabs ins geoeffnete
     * Konzept zurueckgeschrieben.
     */
    private void speicherKonzept() {
	if (this.changed) {
	    this.ctrl.setAktuellesKonzept(this.aktuellesKonzept);
	    this.ctrl.removeBezeichnungen();

	    // Alle Sprachtabs speichern
	    for (Object o : this.tabSprache.getComponents()) {
		if (!o.equals(this.scrSchemata)) {
		    SprachTab sp = (SprachTab) o;
		    this.ctrl.addBezeichnung(sp.getTxtBevBezText(), sp.getSprachrep().getSprache(),
			    BezeichnungsTyp.bevorzugt);
		    this.ctrl.addBezeichnung(sp.getTxtaBeispielText(), sp.getSprachrep().getSprache(),
			    BezeichnungsTyp.beispiel);
		    this.ctrl.addBezeichnung(sp.getTxtaBeschreibungText(), sp.getSprachrep().getSprache(),
			    BezeichnungsTyp.beschreibung);
		    this.ctrl.addBezeichnung(sp.getTxtaDefinitionText(), sp.getSprachrep().getSprache(),
			    BezeichnungsTyp.definition);

		    String[] altBez = sp.getTxtaAltBezText().split("\n");
		    String[] verBez = sp.getTxtaVerBezText().split("\n");

		    for (String s : altBez) {
			if (!s.trim().equals("")) {
			    this.ctrl.addBezeichnung(s.trim(), sp.getSprachrep().getSprache(),
				    BezeichnungsTyp.alternativ);
			}
		    }

		    for (String s : verBez) {
			if (!s.trim().equals("")) {
			    this.ctrl.addBezeichnung(s.trim(), sp.getSprachrep().getSprache(),
				    BezeichnungsTyp.versteckt);
			}
		    }
		}
	    }

	    // Spezialisierungen & Generalisierungen ins Konzept schreiben
	    for (Konzept k : this.genAdded) {
		this.ctrl.addGeneralisierung(k);
	    }
	    for (Konzept k : this.spezAdded) {
		this.ctrl.addSpezialisierung(k);
	    }
	    for (Konzept k : this.genRemoved) {
		this.ctrl.removeGeneralisierung(k);
	    }
	    for (Konzept k : this.spezRemoved) {
		this.ctrl.removeSpezialisierung(k);
	    }
	    for (Schema s : this.schemaAdded) {
		this.ctrl.addSchemaToKonzept(s);
	    }
	    for (Schema s : this.schemaRemoved) {
		this.ctrl.removeSchemaFromKonzept(s);
	    }
	    this.genAdded.clear();
	    this.genRemoved.clear();
	    this.spezAdded.clear();
	    this.spezRemoved.clear();
	    this.schemaAdded.clear();
	    this.schemaRemoved.clear();

	    if (!this.alterName.equals(((SprachTab) this.tabSprache.getComponent(1)).getTxtBevBezText())) {
		this.ctrl.setURI(((SprachTab) this.tabSprache.getComponent(1)).getTxtBevBezText());
	    }
	    this.main.fuehreAus("Changed");
	}
    }

    /**
     * Setzt, dass das Konzept veraendert wurde. Wird nur fuer den "Anwenden"
     * Button gebraucht und fuer ein einfacheres Speichern.
     */
    private void setChanged() {
	this.changed = true;
	this.btnAnwenden.setEnabled(true);
    }

    // <editor-fold defaultstate="collapsed" desc="SprachTab">
    /**
     * Kapselung um die Sprachtabs mehrmals auf einfache Art einfuegen und
     * verwalten zu koennen.
     *
     * @author David+Korbi
     */
    private class SprachTab extends javax.swing.JScrollPane {

	private Sprachrepraesentation sprachrep;
	private javax.swing.JLabel lblBevBez;
	private javax.swing.JLabel lblAltBez;
	private javax.swing.JLabel lblBeispiel;
	private javax.swing.JLabel lblBeschreibung;
	private javax.swing.JLabel lblDefinition;
	private javax.swing.JLabel lblVerBez;
	private javax.swing.JPanel pnl;
	private javax.swing.JScrollPane scrAltBez;
	private javax.swing.JScrollPane scrBeispiel;
	private javax.swing.JScrollPane scrBeschreibung;
	private javax.swing.JScrollPane scrDefinition;
	private javax.swing.JScrollPane scrVerBez;
	private javax.swing.JTextField txtBevBez;
	private javax.swing.JTextArea txtaAltBez;
	private javax.swing.JTextArea txtaBeispiel;
	private javax.swing.JTextArea txtaBeschreibung;
	private javax.swing.JTextArea txtaDefinition;
	private javax.swing.JTextArea txtaVerBez;

	/**
	 * Ueber eine Sprachrepraesentation wird der Sprachtab hier
	 * initialisiert. Tabs werden unterbunden und es werden die Listener für
	 * Änderungen hinzugefuegt.
	 *
	 * @param sprachrep
	 */
	public SprachTab(Sprachrepraesentation sprachrep) {
	    this.sprachrep = sprachrep;
	    this.initComponents();
	    this.denyTabs();
	    this.ladeSprachrep(sprachrep);
	    this.changeTabs();
	}

	/**
	 * Initialisierung der eigentlichen Swing-Bauteile, größtenteils
	 * "modified autogenerated-content based on the well known Deutsch-Tab"
	 * ;-)
	 */
	private void initComponents() {
        pnl = new javax.swing.JPanel();
        lblBevBez = new javax.swing.JLabel();
        txtBevBez = new javax.swing.JTextField();
        lblAltBez = new javax.swing.JLabel();
        scrAltBez = new javax.swing.JScrollPane();
        txtaAltBez = new javax.swing.JTextArea();
        lblVerBez = new javax.swing.JLabel();
        scrVerBez = new javax.swing.JScrollPane();
        txtaVerBez = new javax.swing.JTextArea();
        lblBeschreibung = new javax.swing.JLabel();
        scrBeschreibung = new javax.swing.JScrollPane();
        txtaBeschreibung = new javax.swing.JTextArea();
        scrDefinition = new javax.swing.JScrollPane();
        txtaDefinition = new javax.swing.JTextArea();
        lblDefinition = new javax.swing.JLabel();
        scrBeispiel = new javax.swing.JScrollPane();
        txtaBeispiel = new javax.swing.JTextArea();
        lblBeispiel = new javax.swing.JLabel();

        lblBevBez.setText("Bevorzugte Bezeichnung");

        txtBevBez.addFocusListener(new java.awt.event.FocusAdapter() {
		@Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBevBezFocusGained(evt);
            }
        });

        lblAltBez.setText("Alternative Bezeichnungen");

        txtaAltBez.setColumns(10);
        txtaAltBez.setRows(2);
        scrAltBez.setViewportView(txtaAltBez);

        lblVerBez.setText("Versteckte Bezeichnungen");

        txtaVerBez.setColumns(10);
        txtaVerBez.setRows(2);
        scrVerBez.setViewportView(txtaVerBez);

        lblBeschreibung.setText("Beschreibung");

        txtaBeschreibung.setColumns(20);
        txtaBeschreibung.setRows(2);
        scrBeschreibung.setViewportView(txtaBeschreibung);

        txtaDefinition.setColumns(20);
        txtaDefinition.setRows(2);
        scrDefinition.setViewportView(txtaDefinition);

        lblDefinition.setText("Definition");

        txtaBeispiel.setColumns(20);
        txtaBeispiel.setRows(2);
        scrBeispiel.setViewportView(txtaBeispiel);

        lblBeispiel.setText("Beispiel");

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrDefinition, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(scrBeispiel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(scrBeschreibung, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(txtBevBez, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(lblBevBez, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLayout.createSequentialGroup()
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrAltBez, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(lblAltBez))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVerBez)
                            .addComponent(scrVerBez, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)))
                    .addComponent(lblBeschreibung, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDefinition, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBeispiel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        pnlLayout.setVerticalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBevBez)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBevBez, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAltBez)
                    .addComponent(lblVerBez))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addComponent(scrAltBez, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblBeschreibung))
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addComponent(scrVerBez, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addGap(23, 23, 23)))
                .addGap(0, 0, 0)
                .addComponent(scrBeschreibung, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDefinition)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrDefinition, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBeispiel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrBeispiel, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );

        this.setViewportView(pnl);

        tabSprache.addTab("", this);
	}

	/**
	 * Gibt die Sprachrepraesentation der Kapsel wieder
	 *
	 * @return
	 */
	public Sprachrepraesentation getSprachrep() {
	    return this.sprachrep;
	}

	/**
	 * Hier werden die Textfelder mit Inhalten aus der Sprachrepraesentation
	 * geladen
	 *
	 * @param sprachre
	 * @todo Ich wollte hier noch die letzte leere Zeile entfernen
	 */
	private void ladeSprachrep(Sprachrepraesentation sprachrep) {
	    this.txtBevBez.setText(sprachrep.getBevorzugteBezeichnung());
	    this.txtaBeschreibung.setText(sprachrep.getBeschreibung());
	    this.txtaBeispiel.setText(sprachrep.getBeispiel());
	    this.txtaDefinition.setText(sprachrep.getDefinition());

	    String AltBez = "";
	    for (String s : sprachrep.getAltBezeichnungen()) {
		AltBez += s + "\n";
	    }
	    // AltBez = AltBez.substring(0, AltBez.length()-3);
	    this.txtaAltBez.setText(AltBez);

	    String VerBez = "";
	    for (String s : sprachrep.getVersteckteBezeichnungen()) {
		VerBez += s + "\n";
	    }
	    // VerBez = VerBez.substring(0, VerBez.length()-3);
	    this.txtaVerBez.setText(VerBez);
	}

	private void txtBevBezFocusGained(java.awt.event.FocusEvent evt) {
	    if(this.txtBevBez.getText().equals("neues Konzept")){
		this.txtBevBez.setText("");
	    }
	}

	/**
	 * Tabs werden hiermit auf dem gewuenschtem Textfeld unterbunden und der
	 * Focus direkt auf das naechste Element gesetzt
	 *
	 * @param txtA
	 */
	private void denyTab(JTextArea txtA) {
	    txtA.getActionMap().put("focusNextComponent",
		    new javax.swing.AbstractAction("focusNextComponent") {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
			    java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		    });

	    txtA.getInputMap().put(
		    javax.swing.KeyStroke.getKeyStroke(
		    java.awt.event.KeyEvent.VK_TAB, 0),
		    "focusNextComponent");
	}

	/**
	 * Ruft denyTab() fuer alle vorhandenen Textfelder auf
	 */
	private void denyTabs() {
	    this.denyTab(txtaAltBez);
	    this.denyTab(txtaBeispiel);
	    this.denyTab(txtaBeschreibung);
	    this.denyTab(txtaDefinition);
	    this.denyTab(txtaVerBez);
	}

	/**
	 * Fügt einem einer TextComponent einen Listener für Änderungen hinzu
	 *
	 * @param comp
	 */
	private void changeTab(javax.swing.text.JTextComponent comp) {
	    comp.getDocument().addDocumentListener(
		    new javax.swing.event.DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
			    setChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			    setChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			    return;
			}
		    });
	}

	/**
	 * Ruft changeTab() fuer alle vorhandenen Textfelder auf
	 */
	private void changeTabs() {
	    this.changeTab(txtaAltBez);
	    this.changeTab(txtaBeispiel);
	    this.changeTab(txtaBeschreibung);
	    this.changeTab(txtaDefinition);
	    this.changeTab(txtaVerBez);
	    this.changeTab(txtBevBez);
	}

	private String getTxtaAltBezText() {
	    return txtaAltBez.getText();
	}

	private String getTxtaBeispielText() {
	    return txtaBeispiel.getText();
	}

	private String getTxtaBeschreibungText() {
	    return txtaBeschreibung.getText();
	}

	private String getTxtaDefinitionText() {
	    return txtaDefinition.getText();
	}

	private String getTxtaVerBezText() {
	    return txtaVerBez.getText();
	}

	private String getTxtBevBezText() {
	    return txtBevBez.getText();
	}
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SchemaModel">
    /**
     * Kapselung um die Listen in der GUI richtig zu pflegen
     */
    private class SchemaModel implements javax.swing.ListModel {

	private List<Schema> schemata;
	private List<javax.swing.event.ListDataListener> listener;

	public SchemaModel(List<Schema> schemata) {
	    this.schemata = schemata;
	    this.listener = new ArrayList<javax.swing.event.ListDataListener>();
	    // Default Schema darf nicht angezeigt werden:
	    if (this.schemata.contains(main.getController().getThesaurus().getDefaultSchema())) {
		this.schemata.remove(main.getController().getThesaurus().getDefaultSchema());
	    }
	    Collections.sort(this.schemata, new SchemaKomperator());
	}

	public void addSchema(Schema k) {
	    if (!k.equals(main.getController().getThesaurus().getDefaultSchema())) {
		this.schemata.add(k);
		Collections.sort(this.schemata, new SchemaKomperator());
		this.update();
	    }
	}

	public void removeSchema(Schema k) {
	    this.schemata.remove(k);
	    this.update();
	}

	@Override
	public int getSize() {
	    return this.schemata.size();
	}

	@Override
	public Object getElementAt(int index) {
	    return this.schemata.get(index);
	}

	@Override
	public void addListDataListener(javax.swing.event.ListDataListener l) {
	    this.listener.add(l);
	}

	@Override
	public void removeListDataListener(javax.swing.event.ListDataListener l) {
	    this.listener.remove(l);
	}

	public void update() {
	    for (javax.swing.event.ListDataListener l : this.listener) {
		l.contentsChanged(new ListDataEvent(this,
			ListDataEvent.CONTENTS_CHANGED, 0,
			this.schemata.size() - 1));
	    }
	}

	private boolean contains(Schema s) {
	    return this.schemata.contains(s);
	}

	private class SchemaKomperator implements Comparator<Schema> {

	    @Override
	    public int compare(Schema o1, Schema o2) {
		return o1.getURI().getUri().compareTo(o2.getURI().getUri());
	    }
	}
    }// </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        diaSuchen = new javax.swing.JDialog();
        pnlSuchen = new javax.swing.JPanel();
        txtSuchen = new javax.swing.JTextField();
        cbSprache = new javax.swing.JComboBox();
        btnSuchen = new javax.swing.JButton();
        scrSuchergebnisse = new javax.swing.JScrollPane();
        tblSuchergebnisse = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnAnwenden = new javax.swing.JButton();
        btnAbbrechen = new javax.swing.JButton();
        seperator = new javax.swing.JSeparator();
        pnlGeneralisierungen = new javax.swing.JPanel();
        lblGeneralisierungen = new javax.swing.JLabel();
        scrGeneralisierungen = new javax.swing.JScrollPane();
        lstGeneralisierungen = new javax.swing.JList();
        btnAddGen = new javax.swing.JButton();
        btnRemGen = new javax.swing.JButton();
        pnlKonzept = new javax.swing.JPanel();
        lblKonzept = new javax.swing.JLabel();
        tabSprache = new javax.swing.JTabbedPane();
        scrSchemata = new javax.swing.JScrollPane();
        pnlSchemata = new javax.swing.JPanel();
        scrExistentSchemata = new javax.swing.JScrollPane();
        lstExistentSchemata = new javax.swing.JList();
        scrRelatedSchemata = new javax.swing.JScrollPane();
        lstRelatedSchemata = new javax.swing.JList();
        btnRemSchema = new javax.swing.JButton();
        btnAddSchema = new javax.swing.JButton();
        lblExistentSchemata = new javax.swing.JLabel();
        lblRelatedSchemata = new javax.swing.JLabel();
        scrDeutsch = new javax.swing.JScrollPane();
        pnlDeutsch = new javax.swing.JPanel();
        lblDeutschBevBez = new javax.swing.JLabel();
        txtDeutschBevBez = new javax.swing.JTextField();
        lblDeutschAltBez = new javax.swing.JLabel();
        scrDeutschAltBez = new javax.swing.JScrollPane();
        txtaDeutschAltBez = new javax.swing.JTextArea();
        lblDeutschVerBez = new javax.swing.JLabel();
        scrDeutschVerBez = new javax.swing.JScrollPane();
        txtaDeutschVerBez = new javax.swing.JTextArea();
        lblDeutschBeschreibung = new javax.swing.JLabel();
        scrDeutschBeschreibung = new javax.swing.JScrollPane();
        txtaDeutschBeschreibung = new javax.swing.JTextArea();
        scrDeutschDefinition = new javax.swing.JScrollPane();
        txtaDeutschDefinition = new javax.swing.JTextArea();
        lblDeutschDefinition = new javax.swing.JLabel();
        scrDeutschBeispiel = new javax.swing.JScrollPane();
        txtaDeutschBeispiel = new javax.swing.JTextArea();
        lblDeutschBeispiel = new javax.swing.JLabel();
        pnlSpezialisierungen = new javax.swing.JPanel();
        lblSpezialisierungen = new javax.swing.JLabel();
        scrSpezialisierungen = new javax.swing.JScrollPane();
        lstSpezialisierungen = new javax.swing.JList();
        btnRemSpez = new javax.swing.JButton();
        btnAddSpez = new javax.swing.JButton();

        diaSuchen.setMinimumSize(new java.awt.Dimension(544, 299));

        pnlSuchen.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtSuchen.setText("Hier Suchwort eingeben");
        txtSuchen.setMaximumSize(new java.awt.Dimension(160, 25));
        txtSuchen.setMinimumSize(new java.awt.Dimension(160, 25));
        txtSuchen.setPreferredSize(new java.awt.Dimension(160, 25));
        txtSuchen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuchenActionPerformed(evt);
            }
        });
        txtSuchen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSuchenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuchenFocusLost(evt);
            }
        });

        cbSprache.setMaximumSize(new java.awt.Dimension(160, 25));
        cbSprache.setMinimumSize(new java.awt.Dimension(160, 25));
        cbSprache.setPreferredSize(new java.awt.Dimension(160, 25));

        btnSuchen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/find.png"))); // NOI18N
        btnSuchen.setText("Suchen");
        btnSuchen.setMaximumSize(new java.awt.Dimension(160, 25));
        btnSuchen.setMinimumSize(new java.awt.Dimension(160, 25));
        btnSuchen.setPreferredSize(new java.awt.Dimension(160, 25));
        btnSuchen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuchenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSuchenLayout = new javax.swing.GroupLayout(pnlSuchen);
        pnlSuchen.setLayout(pnlSuchenLayout);
        pnlSuchenLayout.setHorizontalGroup(
            pnlSuchenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSuchenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSuchen, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbSprache, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuchen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlSuchenLayout.setVerticalGroup(
            pnlSuchenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuchenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSuchenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSuchen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuchen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbSprache, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        scrSuchergebnisse.setMinimumSize(new java.awt.Dimension(0, 0));

        tblSuchergebnisse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bevorzugte Bezeichnung", "Alternative Bezeichnungen", "Bemerkung"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSuchergebnisse.setUpdateSelectionOnSort(false);
        tblSuchergebnisse.setVerifyInputWhenFocusTarget(false);
        tblSuchergebnisse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSuchergebnisseMouseClicked(evt);
            }
        });
        scrSuchergebnisse.setViewportView(tblSuchergebnisse);

        javax.swing.GroupLayout diaSuchenLayout = new javax.swing.GroupLayout(diaSuchen.getContentPane());
        diaSuchen.getContentPane().setLayout(diaSuchenLayout);
        diaSuchenLayout.setHorizontalGroup(
            diaSuchenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSuchen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrSuchergebnisse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
        );
        diaSuchenLayout.setVerticalGroup(
            diaSuchenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diaSuchenLayout.createSequentialGroup()
                .addComponent(pnlSuchen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrSuchergebnisse, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
        );

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_ok.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnAnwenden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_ok_apply.png"))); // NOI18N
        btnAnwenden.setText("Anwenden");
        btnAnwenden.setEnabled(false);
        btnAnwenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnwendenActionPerformed(evt);
            }
        });

        btnAbbrechen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_cancel.png"))); // NOI18N
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbbrechenActionPerformed(evt);
            }
        });

        pnlGeneralisierungen.setBorder(null);
        pnlGeneralisierungen.setPreferredSize(new java.awt.Dimension(208, 494));

        lblGeneralisierungen.setText("Generalisierungen");

        lstGeneralisierungen.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstGeneralisierungen.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGeneralisierungenValueChanged(evt);
            }
        });
        scrGeneralisierungen.setViewportView(lstGeneralisierungen);

        btnAddGen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/add.png"))); // NOI18N
        btnAddGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGenActionPerformed(evt);
            }
        });

        btnRemGen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png"))); // NOI18N
        btnRemGen.setEnabled(false);
        btnRemGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemGenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlGeneralisierungenLayout = new javax.swing.GroupLayout(pnlGeneralisierungen);
        pnlGeneralisierungen.setLayout(pnlGeneralisierungenLayout);
        pnlGeneralisierungenLayout.setHorizontalGroup(
            pnlGeneralisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralisierungenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGeneralisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrGeneralisierungen, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGeneralisierungen)
                    .addGroup(pnlGeneralisierungenLayout.createSequentialGroup()
                        .addComponent(btnAddGen, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemGen, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlGeneralisierungenLayout.setVerticalGroup(
            pnlGeneralisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGeneralisierungenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGeneralisierungen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrGeneralisierungen, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGeneralisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAddGen)
                    .addComponent(btnRemGen))
                .addContainerGap())
        );

        pnlKonzept.setBorder(null);

        lblKonzept.setText("<Name>");

        tabSprache.setFocusCycleRoot(true);

        lstExistentSchemata.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstExistentSchemata.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstExistentSchemataValueChanged(evt);
            }
        });
        scrExistentSchemata.setViewportView(lstExistentSchemata);

        lstRelatedSchemata.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstRelatedSchemata.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstRelatedSchemataValueChanged(evt);
            }
        });
        scrRelatedSchemata.setViewportView(lstRelatedSchemata);

        btnRemSchema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/arrow_right.png"))); // NOI18N
        btnRemSchema.setEnabled(false);
        btnRemSchema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemSchemaActionPerformed(evt);
            }
        });

        btnAddSchema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/arrow_left.png"))); // NOI18N
        btnAddSchema.setEnabled(false);
        btnAddSchema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSchemaActionPerformed(evt);
            }
        });

        lblExistentSchemata.setText("Vorhandene Schemata");

        lblRelatedSchemata.setText("Ausgewählte Schemata");

        javax.swing.GroupLayout pnlSchemataLayout = new javax.swing.GroupLayout(pnlSchemata);
        pnlSchemata.setLayout(pnlSchemataLayout);
        pnlSchemataLayout.setHorizontalGroup(
            pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSchemataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSchemataLayout.createSequentialGroup()
                        .addComponent(scrRelatedSchemata, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddSchema)
                            .addComponent(btnRemSchema)))
                    .addComponent(lblRelatedSchemata))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblExistentSchemata)
                    .addComponent(scrExistentSchemata, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlSchemataLayout.setVerticalGroup(
            pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSchemataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExistentSchemata)
                    .addComponent(lblRelatedSchemata))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSchemataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(scrExistentSchemata, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                    .addGroup(pnlSchemataLayout.createSequentialGroup()
                        .addComponent(btnAddSchema)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemSchema))
                    .addComponent(scrRelatedSchemata, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE))
                .addContainerGap())
        );

        scrSchemata.setViewportView(pnlSchemata);

        tabSprache.addTab("Schemata", scrSchemata);

        lblDeutschBevBez.setText("Bevorzugte Bezeichnung");

        txtDeutschBevBez.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDeutschBevBezFocusGained(evt);
            }
        });

        lblDeutschAltBez.setText("Alternative Bezeichnungen");

        txtaDeutschAltBez.setColumns(10);
        txtaDeutschAltBez.setRows(2);
        scrDeutschAltBez.setViewportView(txtaDeutschAltBez);

        lblDeutschVerBez.setText("Versteckte Bezeichnungen");

        txtaDeutschVerBez.setColumns(10);
        txtaDeutschVerBez.setRows(2);
        scrDeutschVerBez.setViewportView(txtaDeutschVerBez);

        lblDeutschBeschreibung.setText("Beschreibung");

        txtaDeutschBeschreibung.setColumns(20);
        txtaDeutschBeschreibung.setRows(2);
        scrDeutschBeschreibung.setViewportView(txtaDeutschBeschreibung);

        txtaDeutschDefinition.setColumns(20);
        txtaDeutschDefinition.setRows(2);
        scrDeutschDefinition.setViewportView(txtaDeutschDefinition);

        lblDeutschDefinition.setText("Definition");

        txtaDeutschBeispiel.setColumns(20);
        txtaDeutschBeispiel.setRows(2);
        scrDeutschBeispiel.setViewportView(txtaDeutschBeispiel);

        lblDeutschBeispiel.setText("Beispiel");

        javax.swing.GroupLayout pnlDeutschLayout = new javax.swing.GroupLayout(pnlDeutsch);
        pnlDeutsch.setLayout(pnlDeutschLayout);
        pnlDeutschLayout.setHorizontalGroup(
            pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDeutschLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrDeutschDefinition, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(scrDeutschBeispiel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(scrDeutschBeschreibung, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(txtDeutschBevBez, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(lblDeutschBevBez, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlDeutschLayout.createSequentialGroup()
                        .addGroup(pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrDeutschAltBez, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(lblDeutschAltBez))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDeutschVerBez)
                            .addComponent(scrDeutschVerBez, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)))
                    .addComponent(lblDeutschBeschreibung, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDeutschDefinition, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDeutschBeispiel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        pnlDeutschLayout.setVerticalGroup(
            pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDeutschLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDeutschBevBez)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDeutschBevBez, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDeutschAltBez)
                    .addComponent(lblDeutschVerBez))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDeutschLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDeutschLayout.createSequentialGroup()
                        .addComponent(scrDeutschAltBez, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDeutschBeschreibung))
                    .addGroup(pnlDeutschLayout.createSequentialGroup()
                        .addComponent(scrDeutschVerBez, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addGap(23, 23, 23)))
                .addGap(0, 0, 0)
                .addComponent(scrDeutschBeschreibung, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDeutschDefinition)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrDeutschDefinition, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDeutschBeispiel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrDeutschBeispiel, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );

        scrDeutsch.setViewportView(pnlDeutsch);

        tabSprache.addTab("Deutsch", scrDeutsch);

        javax.swing.GroupLayout pnlKonzeptLayout = new javax.swing.GroupLayout(pnlKonzept);
        pnlKonzept.setLayout(pnlKonzeptLayout);
        pnlKonzeptLayout.setHorizontalGroup(
            pnlKonzeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKonzeptLayout.createSequentialGroup()
                .addComponent(lblKonzept)
                .addContainerGap(413, Short.MAX_VALUE))
            .addComponent(tabSprache, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        );
        pnlKonzeptLayout.setVerticalGroup(
            pnlKonzeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKonzeptLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKonzept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabSprache, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlSpezialisierungen.setBorder(null);
        pnlSpezialisierungen.setPreferredSize(new java.awt.Dimension(208, 494));

        lblSpezialisierungen.setText("Spezialisierungen");

        lstSpezialisierungen.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstSpezialisierungen.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstSpezialisierungenValueChanged(evt);
            }
        });
        scrSpezialisierungen.setViewportView(lstSpezialisierungen);

        btnRemSpez.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png"))); // NOI18N
        btnRemSpez.setEnabled(false);
        btnRemSpez.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemSpezActionPerformed(evt);
            }
        });

        btnAddSpez.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/add.png"))); // NOI18N
        btnAddSpez.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSpezActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSpezialisierungenLayout = new javax.swing.GroupLayout(pnlSpezialisierungen);
        pnlSpezialisierungen.setLayout(pnlSpezialisierungenLayout);
        pnlSpezialisierungenLayout.setHorizontalGroup(
            pnlSpezialisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpezialisierungenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSpezialisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrSpezialisierungen, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSpezialisierungen)
                    .addGroup(pnlSpezialisierungenLayout.createSequentialGroup()
                        .addComponent(btnAddSpez, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemSpez, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSpezialisierungenLayout.setVerticalGroup(
            pnlSpezialisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSpezialisierungenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSpezialisierungen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrSpezialisierungen, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSpezialisierungenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAddSpez)
                    .addComponent(btnRemSpez))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seperator, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnwenden)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbbrechen))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlGeneralisierungen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlKonzept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSpezialisierungen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnwenden, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbbrechen, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seperator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlKonzept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSpezialisierungen, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addComponent(pnlGeneralisierungen, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void txtDeutschBevBezFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeutschBevBezFocusGained
	    //Drin aus Kompatibilitätsgründen
	}//GEN-LAST:event_txtDeutschBevBezFocusGained

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOkActionPerformed
	this.speicherKonzept();
	this.main.fuehreAus("KonzeptAnsicht_Ok");
    }// GEN-LAST:event_btnOkActionPerformed

    private void btnAnwendenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnwendenActionPerformed
	this.speicherKonzept();
	this.changed = false;
	this.btnAnwenden.setEnabled(false);
    }// GEN-LAST:event_btnAnwendenActionPerformed

    private void btnAbbrechenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAbbrechenActionPerformed
	this.ctrl.setAktuellesKonzept(this.aktuellesKonzept);
	this.setVisible(false);
	this.main.fuehreAus("KonzeptAnsicht_Abbruch");
    }// GEN-LAST:event_btnAbbrechenActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Beziehungen">
    private void lstGeneralisierungenValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstGeneralisierungenValueChanged
	this.btnRemGen.setEnabled(this.lstGeneralisierungen.getSelectedIndices().length > 0);
    }// GEN-LAST:event_lstGeneralisierungenValueChanged

    private void lstSpezialisierungenValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstSpezialisierungenValueChanged
	this.btnRemSpez.setEnabled(this.lstSpezialisierungen.getSelectedIndices().length > 0);
    }// GEN-LAST:event_lstSpezialisierungenValueChanged

    private void btnAddGenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddGenActionPerformed
	this.lstAddTo = this.lstGeneralisierungen;
	this.diaSuchen.setVisible(true);
	this.diaSuchen.setBounds(20, 20, 550, 300);
    }// GEN-LAST:event_btnAddGenActionPerformed

    private void btnRemGenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemGenActionPerformed
	List<Konzept> rem = new ArrayList<Konzept>();
	for (Object o : lstGeneralisierungen.getSelectedValues()) {
	    Konzept k = (Konzept) o;
	    rem.add(k);
	    if (!this.genAdded.contains(k)) {
		this.genRemoved.add(k);
	    } else {
		this.genAdded.remove(k);
	    }
	}
	for (Konzept k : rem) {
	    ((KonzeptModel) this.lstGeneralisierungen.getModel()).removeKonzept(k);
	}
	this.lstGeneralisierungen.clearSelection();
	this.setChanged();
    }// GEN-LAST:event_btnRemGenActionPerformed

    private void btnAddSpezActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddSpezActionPerformed
	this.lstAddTo = this.lstSpezialisierungen;
	this.diaSuchen.setVisible(true);
	this.diaSuchen.setBounds(20, 20, 550, 300);
    }// GEN-LAST:event_btnAddSpezActionPerformed

    private void btnRemSpezActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemSpezActionPerformed
	List<Konzept> rem = new ArrayList<Konzept>();
	for (Object o : lstSpezialisierungen.getSelectedValues()) {
	    Konzept k = (Konzept) o;
	    rem.add(k);
	    if (!this.spezAdded.contains(k)) {
		this.spezRemoved.add(k);
	    } else {
		this.spezAdded.remove(k);
	    }
	}
	for (Konzept k : rem) {
	    ((KonzeptModel) this.lstSpezialisierungen.getModel()).removeKonzept(k);
	}
	this.lstSpezialisierungen.clearSelection();
	this.setChanged();
    }// GEN-LAST:event_btnRemSpezActionPerformed

    private void addBeziehung(Konzept k) {
	this.diaSuchen.setVisible(false);
	if (this.lstAddTo.equals(this.lstGeneralisierungen)) {
	    if (!this.genRemoved.contains(k)) {
		this.genAdded.add(k);
	    } else {
		this.genRemoved.remove(k);
	    }
	} else {
	    if (!this.spezRemoved.contains(k)) {
		this.spezAdded.add(k);
	    } else {
		this.spezRemoved.remove(k);
	    }
	}
	((KonzeptModel) this.lstAddTo.getModel()).addKonzept(k);
	this.setChanged();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Suchen">
    // ##################### Suchen #############################################
    private void txtSuchenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSuchenActionPerformed
	this.btnSuchenActionPerformed(evt);
    }// GEN-LAST:event_txtSuchenActionPerformed

    private void txtSuchenFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtSuchenFocusGained
	if (this.txtSuchen.getText().equals("Geben Sie bitte ein Suchwort ein!")
		|| this.txtSuchen.getText().equals("Hier Suchwort eingeben")) {
	    this.txtSuchen.setText("");
	}
    }// GEN-LAST:event_txtSuchenFocusGained

    private void txtSuchenFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtSuchenFocusLost
	if (this.txtSuchen.getText().isEmpty()) {
	    this.txtSuchen.setText("Hier Suchwort eingeben");
	}
    }// GEN-LAST:event_txtSuchenFocusLost

    private void btnSuchenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuchenActionPerformed
	if (!txtSuchen.getText().isEmpty()
		&& !txtSuchen.getText().equals(
		"Geben Sie bitte ein Suchwort ein!")
		&& !txtSuchen.getText().equals("Hier Suchwort eingeben")) {
	    if (cbSprache.getSelectedItem().equals(HauptAnsicht.alleSprachen)) {
		suchErgebnisse = new ArrayList[3];
		suchErgebnisse[0] = new ArrayList<Konzept>();
		suchErgebnisse[1] = new ArrayList<Konzept>();
		suchErgebnisse[2] = new ArrayList<Konzept>();
		for (Sprache s : this.ctrl.getThesaurus().getSprachen()) {
		    List<Konzept>[] list = ctrl.suchen(txtSuchen.getText(), s);
		    suchErgebnisse[0].addAll(list[0]);
		    suchErgebnisse[1].addAll(list[1]);
		    suchErgebnisse[2].addAll(list[2]);
		}
	    } else {
		suchErgebnisse = ctrl.suchen(txtSuchen.getText(),
			((Sprache) cbSprache.getSelectedItem()));
	    }
	    viewSuchErgebnisse();
	    if (suchErgebnisse == null
		    || (suchErgebnisse[0].isEmpty()
		    && suchErgebnisse[1].isEmpty() && suchErgebnisse[2].isEmpty())) {
		JOptionPane.showMessageDialog(this,
			"Es wurden keine Konzepte gefunden!",
			"Suche erfolglos", JOptionPane.INFORMATION_MESSAGE);
	    }
	} else {
	    txtSuchen.setText("Geben Sie bitte ein Suchwort ein!");
	}
    }// GEN-LAST:event_btnSuchenActionPerformed

    private void tblSuchergebnisseMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblSuchergebnisseMouseClicked
	Calendar calendar = Calendar.getInstance();
	Date now = calendar.getTime();
	thesaurusEditor.Konzept aktuelles = this.getKonzept(tblSuchergebnisse.getSelectedRow());
	if (date != null && (now.getTime() - date.getTime()) < 500) {
	    if (!geklickt) {
		System.out.println("Single clicked");
		geklickt = true;
	    } else {
		ctrl.setAktuellesKonzept(aktuelles);
		System.out.println(aktuelles.getURI());
		System.out.println("double clicked");
		// TODO fixen!
		geklickt = false;
		this.addBeziehung(aktuelles);
	    }
	} else {
	    System.out.println("Single clicked");
	    geklickt = true;
	    // TODO: Graphen-Vorschau des Konzepts anzeigen
	}
	date = now;
    }// GEN-LAST:event_tblSuchergebnisseMouseClicked

    private void viewSuchErgebnisse() {
	String[][] model = new String[suchErgebnisse[0].size()
		+ suchErgebnisse[1].size() + suchErgebnisse[2].size()][3];
	int i = 0;
	for (int sorte = 0; sorte <= 2; sorte++) {
	    for (int a = 0; a < suchErgebnisse[sorte].size(); a++) {
		if (cbSprache.getSelectedItem().equals(HauptAnsicht.alleSprachen)) {
		    for (Sprache s : this.ctrl.getThesaurus().getSprachen()) {
			Sprachrepraesentation sp = this.suchErgebnisse[sorte].get(a).getSprachrepraesentation(s);
			if (sp != null
				&& sp.enthaeltWort(this.txtSuchen.getText()) != BezeichnungsTyp.kein) {
			    model[i][0] = sp.getBevorzugteBezeichnung();
			    if (model[i][0].isEmpty()) {
				model[i][0] = this.suchErgebnisse[sorte].get(a).toString()
					+ "["
					+ this.suchErgebnisse[sorte].get(a).getSprachrepraesentationen().get(0).getSprache() + "]";
			    }
			    model[i][1] = sp.getAltBezeichnungen().toString();
			    model[i][2] = "Gefunden in der Sprache [" + s
				    + "] - " + sp.getBeschreibung();
			    break;
			}
		    }
		} else {
		    Sprachrepraesentation s = suchErgebnisse[sorte].get(a).getSprachrepraesentation(
			    (Sprache) cbSprache.getSelectedItem());

		    model[i][0] = s.getBevorzugteBezeichnung();
		    model[i][1] = s.getAltBezeichnungen().toString();
		    model[i][2] = s.getBeschreibung();
		}
		i++;
	    }
	}
	tblSuchergebnisse.setModel(new javax.swing.table.DefaultTableModel(
		model, new String[]{"Bevorzugte Bezeichnung",
		    "Alternative Bezeichnung", "Bemerkung"}) {

	    @Override
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	    }

	    public boolean isEditable() {
		return false;
	    }
	});
    }

    private Konzept getKonzept(int selectedRow) {
	if (selectedRow < suchErgebnisse[0].size()) {
	    return suchErgebnisse[0].get(selectedRow);
	}
	selectedRow -= suchErgebnisse[0].size();
	if (selectedRow < suchErgebnisse[1].size()) {
	    return suchErgebnisse[1].get(selectedRow);
	}
	selectedRow -= suchErgebnisse[1].size();
	if (selectedRow < suchErgebnisse[2].size()) {
	    return suchErgebnisse[2].get(selectedRow);
	}
	return null;
    }

    // ##################### Suchen ENDE ########################################
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Schemata">
    private void lstRelatedSchemataValueChanged(
	    javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstRelatedSchemataValueChanged
	this.btnRemSchema.setEnabled(this.lstRelatedSchemata.getSelectedIndices().length > 0);
    }// GEN-LAST:event_lstRelatedSchemataValueChanged

    private void lstExistentSchemataValueChanged(
	    javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstExistentSchemataValueChanged
	int selectedCount = this.lstExistentSchemata.getSelectedIndices().length;
	for (Object o : this.lstExistentSchemata.getSelectedValues()) {
	    Schema s = (Schema) o;
	    if (((SchemaModel) this.lstRelatedSchemata.getModel()).contains(s)) {
		selectedCount--;
	    }
	}
	this.btnAddSchema.setEnabled(selectedCount > 0);
    }// GEN-LAST:event_lstExistentSchemataValueChanged

    private void btnRemSchemaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemSchemaActionPerformed
	for (Object o : this.lstRelatedSchemata.getSelectedValues()) {
	    Schema s = (Schema) o;
	    if (this.schemaAdded.contains(s)) {
		this.schemaAdded.remove(s);
	    } else {
		this.schemaRemoved.add(s);
	    }
	    ((SchemaModel) this.lstRelatedSchemata.getModel()).removeSchema(s);
	}
	this.lstRelatedSchemata.clearSelection();
	this.setChanged();
    }// GEN-LAST:event_btnRemSchemaActionPerformed

    private void btnAddSchemaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddSchemaActionPerformed
	for (Object o : this.lstExistentSchemata.getSelectedValues()) {
	    Schema s = (Schema) o;
	    if (!((SchemaModel) this.lstRelatedSchemata.getModel()).contains(s)) {
		if (this.schemaRemoved.contains(s)) {
		    this.schemaRemoved.remove(s);
		} else {
		    this.schemaAdded.add(s);
		}
		((SchemaModel) this.lstRelatedSchemata.getModel()).addSchema(s);
	    }
	}
	this.lstExistentSchemata.clearSelection();
	this.setChanged();
    }// GEN-LAST:event_btnAddSchemaActionPerformed
    // </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbbrechen;
    private javax.swing.JButton btnAddGen;
    private javax.swing.JButton btnAddSchema;
    private javax.swing.JButton btnAddSpez;
    private javax.swing.JButton btnAnwenden;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRemGen;
    private javax.swing.JButton btnRemSchema;
    private javax.swing.JButton btnRemSpez;
    private javax.swing.JButton btnSuchen;
    private javax.swing.JComboBox cbSprache;
    private javax.swing.JDialog diaSuchen;
    private javax.swing.JLabel lblDeutschAltBez;
    private javax.swing.JLabel lblDeutschBeispiel;
    private javax.swing.JLabel lblDeutschBeschreibung;
    private javax.swing.JLabel lblDeutschBevBez;
    private javax.swing.JLabel lblDeutschDefinition;
    private javax.swing.JLabel lblDeutschVerBez;
    private javax.swing.JLabel lblExistentSchemata;
    private javax.swing.JLabel lblGeneralisierungen;
    private javax.swing.JLabel lblKonzept;
    private javax.swing.JLabel lblRelatedSchemata;
    private javax.swing.JLabel lblSpezialisierungen;
    private javax.swing.JList lstExistentSchemata;
    private javax.swing.JList lstGeneralisierungen;
    private javax.swing.JList lstRelatedSchemata;
    private javax.swing.JList lstSpezialisierungen;
    private javax.swing.JPanel pnlDeutsch;
    private javax.swing.JPanel pnlGeneralisierungen;
    private javax.swing.JPanel pnlKonzept;
    private javax.swing.JPanel pnlSchemata;
    private javax.swing.JPanel pnlSpezialisierungen;
    private javax.swing.JPanel pnlSuchen;
    private javax.swing.JScrollPane scrDeutsch;
    private javax.swing.JScrollPane scrDeutschAltBez;
    private javax.swing.JScrollPane scrDeutschBeispiel;
    private javax.swing.JScrollPane scrDeutschBeschreibung;
    private javax.swing.JScrollPane scrDeutschDefinition;
    private javax.swing.JScrollPane scrDeutschVerBez;
    private javax.swing.JScrollPane scrExistentSchemata;
    private javax.swing.JScrollPane scrGeneralisierungen;
    private javax.swing.JScrollPane scrRelatedSchemata;
    private javax.swing.JScrollPane scrSchemata;
    private javax.swing.JScrollPane scrSpezialisierungen;
    private javax.swing.JScrollPane scrSuchergebnisse;
    private javax.swing.JSeparator seperator;
    private javax.swing.JTabbedPane tabSprache;
    private javax.swing.JTable tblSuchergebnisse;
    private javax.swing.JTextField txtDeutschBevBez;
    private javax.swing.JTextField txtSuchen;
    private javax.swing.JTextArea txtaDeutschAltBez;
    private javax.swing.JTextArea txtaDeutschBeispiel;
    private javax.swing.JTextArea txtaDeutschBeschreibung;
    private javax.swing.JTextArea txtaDeutschDefinition;
    private javax.swing.JTextArea txtaDeutschVerBez;
    // End of variables declaration//GEN-END:variables
}
