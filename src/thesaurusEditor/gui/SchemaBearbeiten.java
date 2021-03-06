/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SchemaBearbeiten.java
 *
 * Created on Mar 22, 2011, 10:01:20 AM
 */

package thesaurusEditor.gui;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import thesaurusEditor.Konzept;
import thesaurusEditor.Schema;
import thesaurusEditor.Controller;
import thesaurusEditor.Sprachrepraesentation;
import thesaurusEditor.Thesaurus;
import thesaurusEditor.gui.OptionPane;

/**
 *
 * @author sopr058
 */
public class SchemaBearbeiten extends javax.swing.JPanel
{
	private Schema aktuell;
	private int aendern;
	private String name;
	private List<Konzept> gesamt, added, removed, konezpteImSchema, andereKonzepte;
	private Main main;
	private Controller ctrl;
	private Thesaurus thesaurus;
	
    /** Creates new form SchemaBearbeiten */
    public SchemaBearbeiten(Main main, Schema s, Controller ctrl) 
    {   
    	this.main = main;
    	this.ctrl = ctrl;
    	
    	this.thesaurus = ctrl.getThesaurus();
    	this.aktuell = s;
    	this.initComponents();
    	initGUI();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAbbrechen = new javax.swing.JButton();
        btnAnwenden = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        pnlKonzeptansicht = new javax.swing.JPanel();
        scrKonzepteImSchema = new javax.swing.JScrollPane();
        lstKonzepteimSchema = new javax.swing.JList();
        scrAndereKonzepte = new javax.swing.JScrollPane();
        lstAndereKonzepte = new javax.swing.JList();
        btnNachRechts = new javax.swing.JButton();
        btnNachLinks = new javax.swing.JButton();
        lblKonzepteImSchema = new javax.swing.JLabel();
        lblAndereKonzepte = new javax.swing.JLabel();
        pnlTopkonzepte = new javax.swing.JPanel();
        lblTopkonzepte = new javax.swing.JLabel();
        scrTopkonzepte = new javax.swing.JScrollPane();
        lstTopkonzepte = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        pnlSchema = new javax.swing.JPanel();
        btnUmbenennen = new javax.swing.JButton();
        btnEntfernen = new javax.swing.JButton();
        lblSchema = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(914, 653));

        btnAbbrechen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_cancel.png"))); // NOI18N
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbbrechenActionPerformed(evt);
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

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/dialog_ok.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(32, 33));
        btnOk.setMinimumSize(new java.awt.Dimension(32, 33));
        btnOk.setPreferredSize(new java.awt.Dimension(32, 33));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        pnlKonzeptansicht.setBorder(null);

        lstKonzepteimSchema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lstKonzepteimSchemaFocusGained(evt);
            }
        });
        scrKonzepteImSchema.setViewportView(lstKonzepteimSchema);

        lstAndereKonzepte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lstAndereKonzepteFocusGained(evt);
            }
        });
        scrAndereKonzepte.setViewportView(lstAndereKonzepte);

        btnNachRechts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/arrow_right.png"))); // NOI18N
        btnNachRechts.setActionCommand("AusSchemaEntfernen");
        btnNachRechts.setEnabled(false);
        btnNachRechts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNachRechtsActionPerformed(evt);
            }
        });

        btnNachLinks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/arrow_left.png"))); // NOI18N
        btnNachLinks.setActionCommand("ZuSchemaHinzufuegen");
        btnNachLinks.setEnabled(false);
        btnNachLinks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNachLinksActionPerformed(evt);
            }
        });

        lblKonzepteImSchema.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        lblKonzepteImSchema.setText("Konzepte im Schema");

        lblAndereKonzepte.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
        lblAndereKonzepte.setText("Andere Konzepte");

        javax.swing.GroupLayout pnlKonzeptansichtLayout = new javax.swing.GroupLayout(pnlKonzeptansicht);
        pnlKonzeptansicht.setLayout(pnlKonzeptansichtLayout);
        pnlKonzeptansichtLayout.setHorizontalGroup(
            pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKonzeptansichtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrKonzepteImSchema, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(lblKonzepteImSchema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNachLinks)
                    .addComponent(btnNachRechts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrAndereKonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(lblAndereKonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlKonzeptansichtLayout.setVerticalGroup(
            pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKonzeptansichtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKonzepteImSchema)
                    .addComponent(lblAndereKonzepte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKonzeptansichtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(pnlKonzeptansichtLayout.createSequentialGroup()
                        .addComponent(btnNachLinks)
                        .addGap(5, 5, 5)
                        .addComponent(btnNachRechts))
                    .addComponent(scrAndereKonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addComponent(scrKonzepteImSchema, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnlTopkonzepte.setBorder(null);

        lblTopkonzepte.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        lblTopkonzepte.setText("Topkonzepte");

        lstTopkonzepte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lstTopkonzepte.setRequestFocusEnabled(false);
        lstTopkonzepte.setSelectionBackground(new java.awt.Color(255, 255, 255));
        lstTopkonzepte.setVerifyInputWhenFocusTarget(false);
        scrTopkonzepte.setViewportView(lstTopkonzepte);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        jLabel1.setText("(Wird nach dem Speichern aktualisiert)");

        javax.swing.GroupLayout pnlTopkonzepteLayout = new javax.swing.GroupLayout(pnlTopkonzepte);
        pnlTopkonzepte.setLayout(pnlTopkonzepteLayout);
        pnlTopkonzepteLayout.setHorizontalGroup(
            pnlTopkonzepteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrTopkonzepte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTopkonzepteLayout.createSequentialGroup()
                .addComponent(lblTopkonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1))
        );
        pnlTopkonzepteLayout.setVerticalGroup(
            pnlTopkonzepteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopkonzepteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTopkonzepteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTopkonzepte)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrTopkonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlSchema.setBorder(null);

        btnUmbenennen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/draw_text.png"))); // NOI18N
        btnUmbenennen.setText("Umbennen");
        btnUmbenennen.setMaximumSize(new java.awt.Dimension(100, 30));
        btnUmbenennen.setMinimumSize(new java.awt.Dimension(100, 30));
        btnUmbenennen.setPreferredSize(new java.awt.Dimension(100, 30));
        btnUmbenennen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUmbenennenActionPerformed(evt);
            }
        });

        btnEntfernen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png"))); // NOI18N
        btnEntfernen.setText("Entfernen");
        btnEntfernen.setMaximumSize(new java.awt.Dimension(100, 30));
        btnEntfernen.setMinimumSize(new java.awt.Dimension(100, 30));
        btnEntfernen.setPreferredSize(new java.awt.Dimension(100, 30));
        btnEntfernen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntfernenActionPerformed(evt);
            }
        });

        lblSchema.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N
        lblSchema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSchema.setText("<Schema>");

        javax.swing.GroupLayout pnlSchemaLayout = new javax.swing.GroupLayout(pnlSchema);
        pnlSchema.setLayout(pnlSchemaLayout);
        pnlSchemaLayout.setHorizontalGroup(
            pnlSchemaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSchema, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSchemaLayout.createSequentialGroup()
                .addComponent(btnUmbenennen, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntfernen, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSchemaLayout.setVerticalGroup(
            pnlSchemaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSchemaLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(lblSchema, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSchemaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUmbenennen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntfernen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnwenden)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbbrechen))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlSchema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlTopkonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlKonzeptansicht, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnwenden, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbbrechen, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlSchema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlTopkonzepte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlKonzeptansicht, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
   

    private void initGUI(){
        this.name = aktuell.getName();
    	this.aendern = 0;
    	this.added = new ArrayList<Konzept>();
    	this.removed = new ArrayList<Konzept>();
    	this.gesamt = thesaurus.getKonzepte();
    	List<Konzept>[] konzeptUnterteilung = konzepteImSchema();
    	this.konezpteImSchema = konzeptUnterteilung[0];
    	this.andereKonzepte = konzeptUnterteilung[1];
	//ToolTips für TopKonzepte anbinden
	this.lstTopkonzepte = new javax.swing.JList(new KonzeptModel(this.andereKonzepte)) {
	    // This method is called as the cursor moves within the list.

	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		// Get item index
		if (getCellBounds(0, getLastVisibleIndex()).contains(evt.getPoint())) {
		    int index = locationToIndex(evt.getPoint());

		    // Get item
		    Object item = getModel().getElementAt(index);

		    // Return the tool tip text
		    return ((Konzept) item).getToolTipText();
		}
		return "Liste der Topkonzepte";
	    }
	};
	//GUI muss leider nochmal gesetzt werden
        this.lstTopkonzepte.setModel(new KonzeptModel(aktuell.getTopKonzepte()));
	 lstTopkonzepte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lstTopkonzepte.setRequestFocusEnabled(false);
        lstTopkonzepte.setSelectionBackground(new java.awt.Color(255, 255, 255));
        lstTopkonzepte.setVerifyInputWhenFocusTarget(false);
        scrTopkonzepte.setViewportView(lstTopkonzepte);
        //Liste für Konzepte im Schema mit ToolTipTexts versehen
	this.lstKonzepteimSchema = new javax.swing.JList(new KonzeptModel(this.andereKonzepte)) {
	    // This method is called as the cursor moves within the list.

	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		if (getCellBounds(0, getLastVisibleIndex()).contains(evt.getPoint())) {
		// Get item index
		int index = locationToIndex(evt.getPoint());

		// Get item
		Object item = getModel().getElementAt(index);

		// Return the tool tip text
		return ((Konzept) item).getToolTipText();}
		return "Liste der Konzepte innerhalb des Schemas";
	    }
	};
	//GUI muss leider nochmal gesetzt werden
	 lstKonzepteimSchema.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lstKonzepteimSchemaFocusGained(evt);
            }
        });
        scrKonzepteImSchema.setViewportView(lstKonzepteimSchema);

	this.lstKonzepteimSchema.setModel(new KonzeptModel(this.konezpteImSchema));
       //
	this.lstAndereKonzepte = new javax.swing.JList(new KonzeptModel(this.andereKonzepte)) {
	    // This method is called as the cursor moves within the list.

	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		if (getCellBounds(0, getLastVisibleIndex()).contains(evt.getPoint())) {
		// Get item index
		int index = locationToIndex(evt.getPoint());

		// Get item
		Object item = getModel().getElementAt(index);

		// Return the tool tip text
		return ((Konzept) item).getToolTipText();
		}
		return "Liste der Konzepte, welche sich nicht im Schema befinden";
	    }
	};
	//GUI muss leider nochmal gesetzt werden
	lstAndereKonzepte.addFocusListener(new java.awt.event.FocusAdapter() {

	    public void focusGained(java.awt.event.FocusEvent evt) {
		lstAndereKonzepteFocusGained(evt);
	    }
	});
        scrAndereKonzepte.setViewportView(lstAndereKonzepte);

	
	this.lstAndereKonzepte.setModel(new KonzeptModel(this.andereKonzepte));
	/*{
     // This method is called as the cursor moves within the list.
	    @Override
	    public String getToolTipText(java.awt.event.MouseEvent evt) {
		// Get item index
		int index = locationToIndex(evt.getPoint());

		// Get item
		Object item = getModel().getElementAt(index);

		// Return the tool tip text
		return "tool tip for " + item;
	    }
	};*/
	this.lblSchema.setText(name);
	this.btnAnwenden.setEnabled(false);
    }

    private List<Konzept>[] konzepteImSchema()
    {
    	List<Konzept>[] konzeptUnterteilung = new ArrayList[2];
    	konzeptUnterteilung[0] = new ArrayList<Konzept>();
    	konzeptUnterteilung[1] = new ArrayList<Konzept>();
    	for (Konzept k: gesamt){
    		boolean gefunden = false;
    		for (Schema s: k.getSchemata()){
    			if (s.equals(this.aktuell)){
    				gefunden = true;
    			}    			
    		}
    		if (gefunden){
    			konzeptUnterteilung[0].add(k);
    		}
    		else{
    			konzeptUnterteilung[1].add(k);
    		}
    	}
    	return konzeptUnterteilung;
    }     
    
    private void aenderungenAnwenden()
    {
    	aktuell.setName(name);
        if (!this.added.isEmpty()){
            for(Konzept k : added){
		this.ctrl.setAktuellesKonzept(k);
		this.ctrl.addSchemaToKonzept(aktuell);
	    }
        }
        if (!this.removed.isEmpty()){
            for(Konzept k : removed){
		this.ctrl.setAktuellesKonzept(k);
		this.ctrl.removeSchemaFromKonzept(aktuell);
	    }
        }
	this.added.clear();
	this.removed.clear();
        this.initGUI();
	this.main.fuehreAus("Changed");
        //System.out.println("Änderungen vorgenommen");
    }
    private void btnNachRechtsActionPerformed(java.awt.event.ActionEvent evt)
    {
	List<Konzept> rem = new ArrayList<Konzept>();
	for (Object o : this.lstKonzepteimSchema.getSelectedValues()) {
            Konzept k = (Konzept) o;
            rem.add(k);
            if (!this.added.contains(k)) {
                this.removed.add(k);
            } else {
                this.added.remove(k);
            }
        }
        for (Konzept k : rem) {
            ((KonzeptModel) this.lstKonzepteimSchema.getModel()).removeKonzept(k);
            ((KonzeptModel) this.lstAndereKonzepte.getModel()).addKonzept(k);
        }
        this.lstKonzepteimSchema.clearSelection();

        if (!this.added.isEmpty() || !this.removed.isEmpty() || !this.name.equals(aktuell.getName())){
	    this.btnAnwenden.setEnabled(true);
	} else{
            this.btnAnwenden.setEnabled(false);
        }
	this.updateTopKonzepte();
    }

    private void btnNachLinksActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_btnNachLinksActionPerformed
	List<Konzept> add = new ArrayList<Konzept>();
	for (Object o : this.lstAndereKonzepte.getSelectedValues()) {
            Konzept k = (Konzept) o;
            add.add(k);
            if (!this.removed.contains(k)) {
                this.added.add(k);
            } else {
                this.removed.remove(k);
            }
        }
        for (Konzept k : add) {
            ((KonzeptModel) this.lstAndereKonzepte.getModel()).removeKonzept(k);
            ((KonzeptModel) this.lstKonzepteimSchema.getModel()).addKonzept(k);
        }
        this.lstAndereKonzepte.clearSelection();

        if (!this.added.isEmpty() || !this.removed.isEmpty() || !this.name.equals(aktuell.getName())){
	    this.btnAnwenden.setEnabled(true);
	} else{
            this.btnAnwenden.setEnabled(false);
        }
	this.updateTopKonzepte();
    }//GEN-LAST:event_btnNachLinksActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_btnOkActionPerformed
    	this.aenderungenAnwenden();
    	this.main.fuehreAus("SchemaBearbeiten_OK");
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnAnwendenActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_btnAnwendenActionPerformed
    	/*for (Konzept k: this.Added){
            System.out.println(k);
        }
        System.out.println("");
        for (Konzept k: this.Removed){
            System.out.println(k);
        }*/
        this.aenderungenAnwenden();
    }//GEN-LAST:event_btnAnwendenActionPerformed

    private void btnEntfernenActionPerformed(java.awt.event.ActionEvent evt)
    {//GEN-FIRST:event_btnEntfernenActionPerformed
        OptionPane pane = new OptionPane(this, "Wollen Sie dieses Schema wirklich löschen?", "Löschen bestätigen", new String[]{"Ja", "Nein"});
	//aendern = JOptionPane.showConfirmDialog(this, "Wollen Sie dieses Schema wirklich löschen?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);
	String gewaehlt = (String)pane.pane.getValue();
	if (gewaehlt.equals("Ja")) {
            ctrl.removeSchema(aktuell);
            main.fuehreAus("SchemaBearbeiten_Abbruch");
	    this.main.fuehreAus("Changed");
        }

    }//GEN-LAST:event_btnEntfernenActionPerformed

    private void btnAbbrechenActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_btnAbbrechenActionPerformed
        main.fuehreAus("SchemaBearbeiten_Abbruch");
    }//GEN-LAST:event_btnAbbrechenActionPerformed

    private void lstKonzepteimSchemaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lstKonzepteimSchemaFocusGained
    	this.btnNachLinks.setEnabled(false);
       this.lstAndereKonzepte.removeSelectionInterval(0, lstAndereKonzepte.getModel().getSize());
        if (lstKonzepteimSchema.getSelectedIndex()!=-1){
            this.btnNachRechts.setEnabled(true);
        }
    }//GEN-LAST:event_lstKonzepteimSchemaFocusGained

    private void lstAndereKonzepteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lstAndereKonzepteFocusGained
        this.btnNachRechts.setEnabled(false);
        this.lstKonzepteimSchema.removeSelectionInterval(0, lstKonzepteimSchema.getModel().getSize());
        if (lstAndereKonzepte.getSelectedIndex()!=-1){
            this.btnNachLinks.setEnabled(true);
        }
    }//GEN-LAST:event_lstAndereKonzepteFocusGained

    private void btnUmbenennenActionPerformed(java.awt.event.ActionEvent evt)
    {                                               
    	name = JOptionPane.showInputDialog(this, "Geben Sie bitte den neuen Namen ein", aktuell.getName()).trim();
    	lblSchema.setText(name);
        if (!this.added.isEmpty() || !this.removed.isEmpty() || !this.name.equals(aktuell.getName())) this.btnAnwenden.setEnabled(true);
        else{
            this.btnAnwenden.setEnabled(false);
        }
    }                                             


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbbrechen;
    private javax.swing.JButton btnAnwenden;
    private javax.swing.JButton btnEntfernen;
    private javax.swing.JButton btnNachLinks;
    private javax.swing.JButton btnNachRechts;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnUmbenennen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAndereKonzepte;
    private javax.swing.JLabel lblKonzepteImSchema;
    private javax.swing.JLabel lblSchema;
    private javax.swing.JLabel lblTopkonzepte;
    private javax.swing.JList lstAndereKonzepte;
    private javax.swing.JList lstKonzepteimSchema;
    private javax.swing.JList lstTopkonzepte;
    private javax.swing.JPanel pnlKonzeptansicht;
    private javax.swing.JPanel pnlSchema;
    private javax.swing.JPanel pnlTopkonzepte;
    private javax.swing.JScrollPane scrAndereKonzepte;
    private javax.swing.JScrollPane scrKonzepteImSchema;
    private javax.swing.JScrollPane scrTopkonzepte;
    // End of variables declaration//GEN-END:variables

    
    public static void main(String args []){
    	javax.swing.JFrame frame = new javax.swing.JFrame();
    	thesaurusEditor.Controller ctrl = new thesaurusEditor.Controller();
    	ThesaurusTestfall.genTestThesaurus();
    	ctrl.laden("testThesaurus");
       	frame.add(new SchemaBearbeiten(new Main(ctrl), ctrl.getThesaurus().getSchemata().get(1), ctrl));
    	frame.setBounds(50, 50, 800, 600);
        frame.setVisible(true);
    }

     private DefaultListModel getListModel(List<Konzept> konzept)
    {
    	DefaultListModel model = new DefaultListModel();
        for(int i=0;i<konzept.size();i++)
        {
            
            model.addElement(konzept.get(i));
            
        }
        return model;
    }

    private void updateTopKonzepte() {
	this.lstTopkonzepte.setModel(new KonzeptModel(this.aktuell.getTopKonzepte()));
    }

   
}
