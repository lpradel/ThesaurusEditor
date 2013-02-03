/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesaurusEditor.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.ListDataEvent;
import thesaurusEditor.Konzept;

/**
 *
 * @author sopr051
 */
public class KonzeptModel implements javax.swing.ListModel {

        private List<Konzept> konzepte;
        private List<javax.swing.event.ListDataListener> listener;

        public KonzeptModel(List<Konzept> konzepte) {
            this.konzepte = konzepte;
            this.listener = new ArrayList<javax.swing.event.ListDataListener>();
            Collections.sort(this.konzepte, new KonzeptKomperator());
        }

        public void addKonzept(Konzept k) {
            this.konzepte.add(k);
            Collections.sort(this.konzepte, new KonzeptKomperator());
            this.update();
        }

        public void removeKonzept(Konzept k) {
            this.konzepte.remove(k);
            this.update();
        }

        @Override
        public int getSize() {
            return this.konzepte.size();
        }

        @Override
        public Object getElementAt(int index) {
            return this.konzepte.get(index);
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
                l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.konzepte.size() - 1));
            }
        }

        private class KonzeptKomperator implements Comparator<Konzept> {

            @Override
            public int compare(Konzept o1, Konzept o2) {
                return o1.getURI().getUri().compareTo(o2.getURI().getUri());
            }
        }
    }
