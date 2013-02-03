package thesaurusEditor.gui.graph;
/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.media.j3d.Shape3D;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import thesaurusEditor.gui.graph.EdgeClass;

import thesaurusEditor.Controller;
import thesaurusEditor.Konzept;
import thesaurusEditor.Sprache;
import thesaurusEditor.Thesaurus;
import thesaurusEditor.URI;
import thesaurusEditor.gui.Main;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.GraphMouse;
import edu.uci.ics.jung.visualization.annotations.AnnotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.AnimatedPickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.LabelEditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ShearingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.decorators.VertexIconShapeTransformer;
import edu.uci.ics.jung.visualization.layout.PersistentLayout;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;


/**
 * This demo shows how to use the vertex labels themselves as 
 * the vertex shapes. Additionally, it shows html labels
 * so they are multi-line, and gradient painting of the
 * vertex labels.
 * 
 * @author Tom Nelson
 * 
 */
public class MainGraph extends JPanel implements Observer {

    /**
	 * 
	 */
	
    Graph<Konzept,EdgeClass> graph;

    VisualizationViewer<Konzept,EdgeClass> vv;
    
    PersistentLayout<Konzept, EdgeClass> layout;
	private Controller ctrl;
	private List<Konzept> konzepte;
	private Main main;
	private final MyModalGraphMouse<Konzept,EdgeClass> graphMouse;
    /**
     * create an instance of a simple graph with basic controls
     */
    public MainGraph(List<Konzept> konzepte,Main main) {
        
        // create a simple graph for the demo
        this.konzepte = konzepte;
    	this.main = main;
    	
    	main.getController().getThesaurus().addObserver(this);
    	
    	graph = new DirectedSparseGraph<Konzept, EdgeClass>();
        
		for(Konzept k: konzepte) {
			graph.addVertex(k);
		}
		createEdges(konzepte);
        
        
        layout = new PersistentLayoutImpl<Konzept,EdgeClass>(new FRLayout<Konzept,EdgeClass>(graph));
        //layout = new FRLayout<Konzept,EdgeClass>(graph);
        Dimension preferredSize = new Dimension(1300,900);
        final VisualizationModel<Konzept,EdgeClass> visualizationModel = 
            new DefaultVisualizationModel<Konzept,EdgeClass>(layout, preferredSize);
        vv =  new VisualizationViewer<Konzept,EdgeClass>(visualizationModel, preferredSize);
        
        // this class will provide both label drawing and vertex shapes
        VertexLabelAsShapeRenderer<Konzept,EdgeClass> vlasr = new VertexLabelAsShapeRenderer<Konzept,EdgeClass>(vv.getRenderContext());
        
        // customize the render context
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<Konzept,String>() {
        	public String transform(Konzept k) {
        		return "";
        	}
        });
        		// this chains together Transformers so that the html tags
        		// are prepended to the toString method output
        		/*new ChainedTransformer<Konzept,String>(new Transformer[]{
        		new ToStringLabeller<Konzept>(),
        		new Transformer<Konzept,String>() {
					public String transform(Konzept input) {
						return input.toString();
					}}}));*/
        vv.getRenderContext().setVertexShapeTransformer(new Transformer<Konzept,Shape>() {
        	public Shape transform(Konzept k) {
        		return new Rectangle(-((k.toString().length()*8+10)/2),-(10),k.toString().length()*7+18,21);        	}
        });
        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.red));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new ConstantTransformer(Color.black));
        vv.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(2.5f)));
        
        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Konzept>(vv.getPickedVertexState(), Color.white,  Color.yellow));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<EdgeClass>(vv.getPickedEdgeState(), Color.black, Color.red));

        
        vv.getRenderContext().setVertexIconTransformer(new Transformer<Konzept,Icon>() {

        	/*
        	 * Implements the Icon interface to draw an Icon with background color and
        	 * a text label
        	 */
			public Icon transform(final Konzept v) {
				return new Icon() {
					private Konzept k;
					public int getIconHeight() {
						return 20;
					}

					public int getIconWidth() {
						if(k!=null) {
							return k.toString().length()*7+10;
						}
						return v.toString().length()*7+10;
					}

					public void paintIcon(Component c, Graphics g,
							int x, int y) {
						if(vv.getPickedVertexState().isPicked(v)) {
							g.setColor(Color.yellow);
						} else {
							g.setColor(Color.lightGray);
						}
						g.fillRect(x, y, v.toString().length()*8+10, 20);
						
						if(vv.getPickedVertexState().isPicked(v)) {
							g.setColor(Color.black);
						} else {
							g.setColor(Color.black);
						}
						g.drawRect(x, y, v.toString().length()*8+10, 20);
						if(vv.getPickedVertexState().isPicked(v)) {
							g.setColor(Color.black);
						} else {
							g.setColor(Color.black);
						}
						this.k=v;
						
						if(vv.getPickedVertexState().isPicked(v)) {
							Font font = new Font("DejaVu Sans Mono", Font.BOLD,  14);
							g.setFont(font);
							g.drawString(""+v, x+6, y+15);
						} else {
							Font font = new Font("DejaVu Sans Mono", Font.PLAIN,  14);
							g.setFont(font);
							g.drawString(""+v, x+6, y+15);
						}
						this.k=v;
						
					}};
			}});
        
        
        // customize the renderer
        /*GradientVertexRenderer<Konzept,EdgeClass> vertex = new GradientVertexRenderer<Konzept,EdgeClass>(Color.white, Color.white, true);
        vv.getRenderer().setVertexRenderer(vertex);
        vv.getRenderer().setVertexLabelRenderer(vlasr); */

        vv.setBackground(Color.white);
        
        // add a listener for ToolTips
        KonzeptParser<Konzept> parser = new KonzeptParser<Konzept>();
        vv.setVertexToolTipTransformer(parser);
        
        /*final DefaultModalGraphMouse<Konzept,Edge> graphMouse = 
            new DefaultModalGraphMouse<Konzept,Edge>();
		*/
       
        
        
        
        
        Factory<Konzept> vertexFactory = new VertexFactory();
        Factory<EdgeClass> edgeFactory = new EdgeFactory();
        
        graphMouse = 
        	new MyModalGraphMouse<Konzept,EdgeClass>(vv.getRenderContext(), vertexFactory, edgeFactory, main, vv);
        
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        // the EditingGraphMouse will pass mouse event coordinates to the
        // vertexLocations function to set the locations of the vertices as
        // they are created
//        graphMouse.setVertexLocations(vertexLocations);
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
        
        
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        //graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        
        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton();
        plus.setIcon(new ImageIcon(getClass().getResource("/thesaurusEditor/img/zoom_in.png")));
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton();
        minus.setIcon(new ImageIcon(getClass().getResource("/thesaurusEditor/img/zoom_out.png")));
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });
        
        JPanel controls = new JPanel();
        JPanel zoomControls = new JPanel(new GridLayout(1,2));
        //zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
        zoomControls.add(plus);
        zoomControls.add(minus);
        controls.add(zoomControls);
        //controls.add(modeBox);
        /*
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(gzsp);
        gzsp.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );


        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(controls);
        controls.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 164, Short.MAX_VALUE)
        );*/

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gzsp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(controls, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gzsp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    
    }
    
    public MyModalGraphMouse getGraphMouse() {
    	return graphMouse;
    }
    
    public void saveLayout(String s) throws IOException{
    	layout.persist(s);
    }
    
    public VisualizationViewer<Konzept, EdgeClass> getVV() {
    	return vv;
    }
    
    public void loadLayout(String s) throws IOException{
    	try {
    		layout.restore(s);
    		if(false) throw new ClassNotFoundException();
    		vv.repaint();
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    }
    
    protected void processMouseEvent(MouseEvent e)
	{
    	if(e.getID() == MouseEvent.MOUSE_ENTERED){
    		this.requestFocus();	
    	}
	}

    
    public void keyEventHappened(KeyEvent e) {
    	if(e.getID()==KeyEvent.KEY_PRESSED) {
    		graphMouse.getModeKeyListener().keyPressed(e);
    	}
    	if(e.getID()==KeyEvent.KEY_RELEASED) {
    		graphMouse.getModeKeyListener().keyReleased(e);;
    	}
    }
    
    public void setCursor(Cursor c) {
    }
    
    
    class VertexFactory implements Factory<Konzept> {

    	int i=0;

		public Konzept create() {
			int anzahl = main.getController().getThesaurus().getKonzepte().size();
			main.fuehreAus("Main_createKonzept");
			if(anzahl==main.getController().getThesaurus().getKonzepte().size()) {
				return null;	
			}
			return main.getController().getThesaurus().getKonzepte().get(main.getController().getThesaurus().getKonzepte().size()-1);
			
		}
		
		public void delete(Konzept vertex) {
			main.getController().getThesaurus().removeKonzept(vertex);
		}
		public void edit(Konzept k) {
			main.getController().setAktuellesKonzept(k);
			main.fuehreAus("Suchen_KonzeptÖffnen");
		}
    }
    
    class EdgeFactory implements Factory<EdgeClass> {

    	int i=0;

        @Override
		public EdgeClass create() {
			return new EdgeClass();
		}
        
        public void addToModell(Konzept k1, Konzept k2) {
        	if (! k1.equals(k2)) {
	        	Konzept ka = main.getController().getAktuellesKonzept();
	        	main.getController().setAktuellesKonzept(k1);
	        	main.getController().addSpezialisierung(k2);
	        	main.getController().setAktuellesKonzept(ka);
	        	main.fuehreAus("Changed");
        	}
        }
        
        public void delete(Konzept k1, Konzept k2) {
        	Konzept ka = main.getController().getAktuellesKonzept();
        	main.getController().setAktuellesKonzept(k1);
        	main.getController().removeSpezialisierung(k2);
        	main.getController().setAktuellesKonzept(ka);
        	main.fuehreAus("Changed");
        }
    }
    
    public void update(Observable a, Object o){
    	Object x = ((Thesaurus)a).getChange();
    	if(x instanceof Konzept){
    		if(konzepte.contains(x)) {
    			graph.addVertex((Konzept)x);
    		} else {
    			graph.removeVertex((Konzept)x);
    		}
    		vv.repaint();
    		this.requestFocus();
    	}
    	if(x instanceof Konzept[] && ((Konzept[])x).length==2) {
    		boolean contains= false;
    		EdgeClass zuLoeschen=null;
    		for(EdgeClass edge: graph.getEdges()) {
    			if(((Konzept[])x)[0]==edge.getOberKonzept() && ((Konzept[])x)[1]==edge.getUnterKonzept()) {
    				contains = true;
    				zuLoeschen = edge;
    			}
    		}
    		if(contains) {
    			graph.removeEdge(zuLoeschen);
    		} else {
    			graph.addEdge(new EdgeClass(((Konzept[])x)[0],((Konzept[])x)[1]), ((Konzept[])x)[0], ((Konzept[])x)[1], EdgeType.DIRECTED);
    		}
    		
    	}
    	this.requestFocus();
    }
    
	public void setMain(Main main) {
		this.main = main;
	}
	
	
    public class KonzeptParser<Konzept> implements Transformer {
    	public String transform(Object o) {
    		return ((thesaurusEditor.Konzept)o).getToolTipText();
    	}
    }
    
    private void createEdges(List<Konzept> konzepte) {
    	for(Konzept konzept: konzepte) {
    		for(Konzept konzspez: konzept.getSpezialisierungen()) {
    			if(konzepte.contains(konzspez))
    			{
    				graph.addEdge(new EdgeClass(konzept,konzspez), konzept, konzspez, EdgeType.DIRECTED);
    			}
    		}
    		//graph.
    	}

    }
    
    public void zoomToVertex(Konzept k) {
    	Layout<Konzept,EdgeClass> layout = vv.getGraphLayout();
        Point2D newCenter = layout.transform(k);
        Point2D lvc = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(vv.getCenter());
        final double dx = (lvc.getX() - newCenter.getX())/ 100;
        final double dy = (lvc.getY() - newCenter.getY())/ 100;
        vv.getPickedVertexState().pick(k, true);
        Runnable animator = new Runnable() {

                public void run() {
                        for (int i = 0; i < 100; i++) {
                                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
                                try {
                                        Thread.sleep(5);
                                } catch (InterruptedException ex) {
                                }
                        }
                }
         };
         Thread thread = new Thread(animator);
         thread.start();
    }
    
    /**
     * a driver for this demo
     */
    /*public static void main(String[] args) {
        JFrame f = new JFrame();
        Controller ctrl = new Controller();
		Thesaurus thesaurus = ctrl.createThesaurus("Deutsch","de");
		ctrl.addKonzept("Tier");
		thesaurus.getKonzepte().get(0).getSprachrepraesentationen().get(0).setDefinition("Ein Lebewesen");
		thesaurus.getKonzepte().get(0).getSprachrepraesentationen().get(0).setBeschreibung("Tiere gibt es in vielen Formen");
		ctrl.addKonzept("Pflanze");
		ctrl.addKonzept("Katze");
		ctrl.addKonzept("Hund");
		ctrl.addKonzept("Blume");
		ctrl.addKonzept("Baum");
		ctrl.setAktuellesKonzept(thesaurus.getKonzepte().get(0));
		ctrl.addSpezialisierung(thesaurus.getKonzepte().get(2));
		ctrl.addSpezialisierung(thesaurus.getKonzepte().get(3));
		ctrl.setAktuellesKonzept(thesaurus.getKonzepte().get(1));
		ctrl.addSpezialisierung(thesaurus.getKonzepte().get(4));
		ctrl.addSpezialisierung(thesaurus.getKonzepte().get(5));
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new MainGraph(thesaurus.getKonzepte(),null));
        f.pack();
        f.setVisible(true);
    }*/
}


