package thesaurusEditor.gui.graph;

import thesaurusEditor.Konzept;
import thesaurusEditor.gui.Main;
import thesaurusEditor.gui.graph.EdgeClass;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.plaf.basic.BasicIconFactory;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.annotations.AnnotatingGraphMousePlugin;
//import edu.uci.ics.jung.visualization.control.AnimatedPickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import thesaurusEditor.gui.graph.EditingGraphMousePlugin;
import thesaurusEditor.gui.graph.EditingModalGraphMouse.ModeKeyAdapter;
//import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.LabelEditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
//import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ShearingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class MyModalGraphMouse<V,E> extends thesaurusEditor.gui.graph.EditingModalGraphMouse {

	//protected MyPopupGraphMousePlugin<Konzept,Edge> popupEditingPlugin;
	EditingGraphMousePlugin<V,E> editingPlugin;
	protected KeyListener modeKeyListener;
	protected VisualizationViewer vv;
	//MyModeKeyAdapter modeKeyAdapter;
	public MyModalGraphMouse(RenderContext rc, Factory vertexFactory,
			Factory edgeFactory, float in, float out, Main main, VisualizationViewer<Konzept, EdgeClass> vv) {
		super(rc, vertexFactory, edgeFactory, in, out,main);
		this.modeKeyListener=new MyModeKeyAdapter(this, vv);
		// TODO Auto-generated constructor stub
	}
	public MyModalGraphMouse(RenderContext rc, Factory vertexFactory,
			Factory edgeFactory, Main main, VisualizationViewer<Konzept, EdgeClass> vv) {
		super(rc, vertexFactory, edgeFactory, main);
		this.modeKeyListener=new MyModeKeyAdapter(this, vv);
		this.vv = vv;
		// TODO Auto-generated constructor stub
	}
	
	protected void loadPlugins() {
		pickingPlugin = new PickingGraphMousePlugin<Konzept,EdgeClass>(main);
		animatedPickingPlugin = new AnimatedPickingGraphMousePlugin<Konzept,EdgeClass>();
		translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
		scalingPlugin = new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, in, out);
		rotatingPlugin = new RotatingGraphMousePlugin();
		shearingPlugin = new ShearingGraphMousePlugin();
		editingPlugin = (EditingGraphMousePlugin<V, E>) new EditingGraphMousePlugin<Konzept,EdgeClass>(vertexFactory, edgeFactory);
		labelEditingPlugin = new LabelEditingGraphMousePlugin<Konzept,EdgeClass>();
		annotatingPlugin = new AnnotatingGraphMousePlugin<Konzept,EdgeClass>(rc);
		popupEditingPlugin = new MyPopupGraphMousePlugin<Konzept,EdgeClass>(vertexFactory, edgeFactory, this);
		add(scalingPlugin);
		setMode(Mode.EDITING);
	}
	public JComboBox getModeComboBox() {
		if(modeBox == null) {
			modeBox = new JComboBox(new Mode[]{Mode.TRANSFORMING, Mode.PICKING, Mode.EDITING});
			modeBox.addItemListener(new ModeListener());
		}
		modeBox.setSelectedItem(mode);
		return modeBox;
	}
	
	public GraphMousePlugin getPickingPlugin() {
		return pickingPlugin;
	}
	
	public GraphMousePlugin getAnimatedPlugin() {
		return animatedPickingPlugin;
	}
	
	public void setMode(Mode mode) {
		if(this.mode != mode) {
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
					this.mode, ItemEvent.DESELECTED));
			this.mode = mode;
			if(mode == Mode.TRANSFORMING) {
				setTransformingMode();
			} else if(mode == Mode.PICKING) {
				setPickingMode();
			} else if(mode == Mode.EDITING) {
				setEditingMode();
			} else if(mode == Mode.ANNOTATING) {
				setAnnotatingMode();
			}
			if(modeBox != null) {
				modeBox.setSelectedItem(mode);
			}
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, mode, ItemEvent.SELECTED));
		}
	}
	class ModeListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            setMode((Mode) e.getItem());
        }
    }
	@Override
	protected void setPickingMode() {
		remove(translatingPlugin);
		remove(rotatingPlugin);
		remove(shearingPlugin);
		remove(editingPlugin);
		remove(annotatingPlugin);
		add(pickingPlugin);
		add(animatedPickingPlugin);
		add(labelEditingPlugin);
		add(popupEditingPlugin);
	}

	@Override
    protected void setTransformingMode() {
		remove(pickingPlugin);
		remove(animatedPickingPlugin);
		remove(editingPlugin);
		remove(annotatingPlugin);
		add(translatingPlugin);
		add(rotatingPlugin);
		add(shearingPlugin);
		add(labelEditingPlugin);
		add(popupEditingPlugin);
	}
	@Override
	protected void setEditingMode() {
		remove(pickingPlugin);
		remove(animatedPickingPlugin);
		remove(translatingPlugin);
		remove(rotatingPlugin);
		remove(shearingPlugin);
		remove(labelEditingPlugin);
		remove(annotatingPlugin);
		add(editingPlugin);
		add(popupEditingPlugin);
	}
	
	@Override
	public KeyListener getModeKeyListener() {
		return modeKeyListener;
	}
	
	 public JMenu getModeMenu() {
			if(modeMenu == null) {
				modeMenu = new JMenu();// {
				Icon icon = BasicIconFactory.getMenuArrowIcon();
				modeMenu.setIcon(BasicIconFactory.getMenuArrowIcon());
				modeMenu.setPreferredSize(new Dimension(icon.getIconWidth()+10, 
						icon.getIconHeight()+10));

				final JRadioButtonMenuItem transformingButton = 
					new JRadioButtonMenuItem(Mode.TRANSFORMING.toString());
				transformingButton.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED) {
							setMode(Mode.TRANSFORMING);
						}
					}});

				final JRadioButtonMenuItem pickingButton =
					new JRadioButtonMenuItem(Mode.PICKING.toString());
				pickingButton.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED) {
							setMode(Mode.PICKING);
						}
					}});

				final JRadioButtonMenuItem editingButton =
					new JRadioButtonMenuItem(Mode.EDITING.toString());
				editingButton.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED) {
							setMode(Mode.EDITING);
						}
					}});

				ButtonGroup radio = new ButtonGroup();
				radio.add(transformingButton);
				radio.add(pickingButton);
				radio.add(editingButton);
				transformingButton.setSelected(true);
				modeMenu.add(transformingButton);
				modeMenu.add(pickingButton);
				modeMenu.add(editingButton);
				modeMenu.setToolTipText("Menu for setting Mouse Mode");
				addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED) {
							if(e.getItem() == Mode.TRANSFORMING) {
								transformingButton.setSelected(true);
							} else if(e.getItem() == Mode.PICKING) {
								pickingButton.setSelected(true);
							} else if(e.getItem() == Mode.EDITING) {
								editingButton.setSelected(true);
							}
						}
					}});
			}
			return modeMenu;
		}
	    public static class MyModeKeyAdapter extends ModeKeyAdapter {
	    	VisualizationViewer<Konzept, EdgeClass> vv;
	    	public MyModeKeyAdapter(ModalGraphMouse graphMouse, VisualizationViewer<Konzept, EdgeClass> vv) {
				super(graphMouse);
				this.vv=vv;
	    	}

			public MyModeKeyAdapter(char t, char p, char e, char a, ModalGraphMouse graphMouse) {
				super(t,p,e,a,graphMouse);
			}
			
			@Override
	        public void keyTyped(KeyEvent event) {
				/*char keyChar = event.getKeyChar();
				if(keyChar == 't') {
					((Component)event.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					graphMouse.setMode(Mode.TRANSFORMING);
				} else if(keyChar == 'p') {
					((Component)event.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					graphMouse.setMode(Mode.PICKING);
				} else if(keyChar == 'e') {
					((Component)event.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					graphMouse.setMode(Mode.EDITING);
				} */
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_CIRCUMFLEX) {
					if(graphMouse !=null) {
						vv.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						graphMouse.setMode(Mode.PICKING);
					}
				} else if(event.getKeyCode()==KeyEvent.VK_SPACE) {
					if(graphMouse !=null) {
						vv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						graphMouse.setMode(Mode.TRANSFORMING);
					}
				} else if(event.getKeyCode()==17) {
					if(graphMouse !=null) {
						vv.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						graphMouse.setMode(Mode.PICKING);
					}
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent event) {
				if(event.getKeyCode()==KeyEvent.VK_CIRCUMFLEX || event.getKeyCode()==KeyEvent.VK_SPACE || event.getKeyCode()==17) {
					if(graphMouse !=null) {
						vv.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
						graphMouse.setMode(Mode.EDITING);
					}
				}
			}
	    }

		/**
		 * @return the annotatingPlugin
		 */
		public AnnotatingGraphMousePlugin<V,E> getAnnotatingPlugin() {
			return annotatingPlugin;
		}

		/**
		 * @return the editingPlugin
		 */
		public EditingGraphMousePlugin<V,E> getEditingPlugin() {
			return editingPlugin;
		}

		/**
		 * @return the labelEditingPlugin
		 */
		public LabelEditingGraphMousePlugin<V,E> getLabelEditingPlugin() {
			return labelEditingPlugin;
		}

		/**
		 * @return the popupEditingPlugin
		 */
		public EditingPopupGraphMousePlugin<V,E> getPopupEditingPlugin() {
			return popupEditingPlugin;
		}
}