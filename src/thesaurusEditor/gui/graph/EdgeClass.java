package thesaurusEditor.gui.graph;

import thesaurusEditor.Konzept;

public class EdgeClass {
	private Konzept k1;
	private Konzept k2;
	private static int counter=0;
	private int id;
	public EdgeClass() {
		id=counter;
		counter++;
	}
	public EdgeClass(Konzept k1, Konzept k2) {
		id=counter;
		counter++;
		this.k1 = k1;
		this.k2 = k2;
	}
	public void setKonzepte(Konzept k1, Konzept k2) {
		this.k1 = k1;
		this.k2 = k2;
	}
	public Konzept getOberKonzept() {
		return k1;
	}
	public Konzept getUnterKonzept() {
		return k2;
	}
}
