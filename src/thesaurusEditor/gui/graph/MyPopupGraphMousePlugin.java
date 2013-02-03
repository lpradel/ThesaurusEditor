package thesaurusEditor.gui.graph;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;

import thesaurusEditor.Konzept;
import thesaurusEditor.gui.graph.EdgeClass;
import thesaurusEditor.gui.graph.MainGraph.EdgeFactory;
import thesaurusEditor.gui.graph.MainGraph.VertexFactory;
import thesaurusEditor.gui.OptionPane;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class MyPopupGraphMousePlugin<K,E> extends EditingPopupGraphMousePlugin<Konzept, EdgeClass> {
		private VertexFactory myVertexFactory;
		private EdgeFactory myEdgeFactory;
		private MyModalGraphMouse graphMouse;
		public MyPopupGraphMousePlugin(Factory<Konzept> vertexFactory,
				Factory<EdgeClass> edgeFactory, MyModalGraphMouse graphMouse) {
			super(vertexFactory, edgeFactory);
			this.graphMouse = graphMouse;
			myVertexFactory = (VertexFactory) vertexFactory;
			myEdgeFactory = (EdgeFactory) edgeFactory;


			// TODO Auto-generated constructor stub
		}

		protected void handlePopup(MouseEvent e) {
	        final VisualizationViewer<Konzept,EdgeClass> vv =
	            (VisualizationViewer<Konzept,EdgeClass>)e.getSource();
	        final Layout<Konzept,EdgeClass> layout = vv.getGraphLayout();
	        final Graph<Konzept,EdgeClass> graph = layout.getGraph();
	        final Point2D p = e.getPoint();
	        final Point2D ivp = p;
	        vv.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			graphMouse.setMode(Mode.EDITING);
	        
	        GraphElementAccessor<Konzept,EdgeClass> pickSupport = vv.getPickSupport();
	        if(pickSupport != null) {

	            final Konzept vertex = pickSupport.getVertex(layout, ivp.getX(), ivp.getY());
	            final EdgeClass edge = pickSupport.getEdge(layout, ivp.getX(), ivp.getY());
	            final PickedState<Konzept> pickedVertexState = vv.getPickedVertexState();
	            final PickedState<EdgeClass> pickedEdgeState = vv.getPickedEdgeState();
	            JPopupMenu popup = new JPopupMenu();
	            JMenuItem mi;
	            if(vertex != null) {
	            	Set<Konzept> picked = pickedVertexState.getPicked();
	            	if(picked.size() > 0) {
	            		if(graph instanceof UndirectedGraph == false) {
	            			JMenu directedMenu = new JMenu("Spezialisierung erstellen");
	            			directedMenu.setIcon(new ImageIcon(getClass().getResource("/thesaurusEditor/img/add.png")));
	            			popup.add(directedMenu);
	            			for(final Konzept other : picked) {
	            		        mi = new javax.swing.JMenuItem("Verbindung von "+other+" zu "+vertex,new ImageIcon(getClass().getResource("/thesaurusEditor/img/add.png")));
	        	                mi.addActionListener(new ActionListener() {
	        	                	public void actionPerformed(ActionEvent e) {
	            						EdgeClass edge = edgeFactory.create();
	            						edge.setKonzepte(other, vertex);
	            						myEdgeFactory.addToModell(other, vertex);
	            						graph.addEdge(edge,
	            								other, vertex, EdgeType.DIRECTED);
	            						vv.repaint();
	            					}
	            				});
	            				directedMenu.add(mi);
	            			}
	            		}
	                }
	            	mi = new javax.swing.JMenuItem("Konzept bearbeiten",new ImageIcon(getClass().getResource("/thesaurusEditor/img/edit.png")));
	                mi.addActionListener(new ActionListener() {
	                	public void actionPerformed(ActionEvent e) {
	                		 pickedVertexState.pick(vertex, false);
	                		 myVertexFactory.edit((thesaurusEditor.Konzept) vertex);
	                		 vv.repaint();
	                	}
	                });
	                popup.add(mi);
	            	mi = new javax.swing.JMenuItem("Konzept löschen",new ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png")));
	                mi.addActionListener(new ActionListener() {
	                	public void actionPerformed(ActionEvent e) {
                                    OptionPane optionPane = new OptionPane(vv, "Wollen Sie dieses Konzept wirklich löschen?", "Konzept löschen", new String[]{"Ja"});
                                    String gewaehlt = (String)optionPane.pane.getValue();
                                    if(gewaehlt.equals("Ja"))
                                    {
                                        myVertexFactory.delete((thesaurusEditor.Konzept) vertex);
                                        graph.removeVertex(vertex);
                                        vv.repaint();
                                    }
                            }
	                });
	                popup.add(mi);

	            } else if(edge != null) {
	                mi = new javax.swing.JMenuItem("Verbindung löschen",new ImageIcon(getClass().getResource("/thesaurusEditor/img/delete.png")));
	                mi.addActionListener(new ActionListener() {
	                	public void actionPerformed(ActionEvent e) {
                        	pickedEdgeState.pick(edge, false);
                        	Konzept k1 = edge.getOberKonzept();
                        	Konzept k2 = edge.getUnterKonzept();
                        	myEdgeFactory.delete(k1, k2);
                        	graph.removeEdge(edge);
                        	vv.repaint();
                    }});

	            	popup.add(mi);
	            } else {
	            	mi = new javax.swing.JMenuItem("Konzept anlegen",new ImageIcon(getClass().getResource("/thesaurusEditor/img/filenew.png")));
	                mi.addActionListener(new ActionListener() {
	                	public void actionPerformed(ActionEvent e) {
	                        Konzept newVertex = vertexFactory.create();
	                        if(newVertex==null) return;
	                        graph.addVertex(newVertex);
	                        layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p));
	                        vv.repaint();
	                    }
	                });
	                popup.add(mi);
	            }
	            if(popup.getComponentCount() > 0) {
	                popup.show(vv, e.getX(), e.getY());


	            }
	        }
	    }
}