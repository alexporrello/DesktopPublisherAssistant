package jm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * Responsible for resizing the application.
 * @author Alexander Porrello
 */
public class JMExpandablePanel extends JMPanel {
	private static final long serialVersionUID = 149674684797815832L;

	/** The position of this component **/
	private Anchor anchor;
	
	/** Determines whether this component is in the state required for movement. **/
	private boolean move = false;
	
	/** The panel's content **/
	private JComponent content = new JMPanel();
	
	/** The component that is clicked to resize this panel. **/
	private JMPanel moveClick = new JMPanel();
	
	public JMExpandablePanel(JComponent c, Anchor anchor) {
		this.anchor = anchor;
		this.content = c;

		setUpPanel();
	}

	public JMExpandablePanel(Anchor anchor) {
		this.anchor = anchor;

		setUpPanel();
	}

	public void setUpPanel() {

		super.setLayout(new BorderLayout());
		super.add(moveClick, anchor.anchor);
		super.add(content, BorderLayout.CENTER);

		if(anchor == Anchor.NORTH || anchor == Anchor.SOUTH) {
			moveClick.setPreferredSize(new Dimension(getWidth(), 5));
		} else {
			moveClick.setPreferredSize(new Dimension(5, getHeight()));
		}

		moveClick.setBackground(Color.WHITE);
		content.setBackground(Color.WHITE);
		setBackground(Color.WHITE);

		moveClick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				move = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				move = true;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		moveClick.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(move) {
					reSize(e.getPoint());
					setCursor();

					revalidate();
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if(conditions(e.getPoint())) {
					setCursor();
				} else {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}

	private void reSize(Point p) {
		if(anchor == Anchor.NORTH) {
			setPreferredSize(new Dimension(getWidth(), p.y));
		} else if(anchor == Anchor.EAST) {
			setPreferredSize(new Dimension(getWidth()-p.x, getHeight()));
		} else if(anchor == Anchor.SOUTH) {
			setPreferredSize(new Dimension(getWidth(), getHeight()-p.y));
		} else if(anchor == Anchor.WEST) {
			setPreferredSize(new Dimension(p.x, getHeight()));
		}
	}

	private Boolean conditions(Point p) {
		if(anchor == Anchor.NORTH) {
			return p.y > getHeight()-6 && p.y < getHeight()+3;
		} else if(anchor == Anchor.EAST) {
			return p.x > -3 && p.x < 6;
		} else if(anchor == Anchor.SOUTH) {
			return p.y > -3 && p.y < 6;
		} else if(anchor == Anchor.WEST) {
			return p.x < getWidth()+3 && p.x > getWidth()-6;
		} else {
			return false;
		}
	}

	/**
	 * Sets the cursor that displays when user hovers over resize portion of the window.
	 */
	private void setCursor() {
		if(anchor == Anchor.NORTH) {
			setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		} else if(anchor == Anchor.EAST) {
			setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		} else if(anchor == Anchor.SOUTH) {
			setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		} else if(anchor == Anchor.WEST) {
			setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		} 
	}

	@Override
	public Component add(Component comp) {
		return content.add(comp);
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		if(content != null) {
			content.setLayout(mgr);
		}
	}

	@Override
	public void removeAll() {
		content.removeAll();
	}


	public enum Anchor {
		NORTH(BorderLayout.SOUTH), SOUTH(BorderLayout.NORTH), 
		EAST(BorderLayout.WEST), WEST(BorderLayout.EAST);

		String anchor;

		Anchor(String anchor) {
			this.anchor = anchor;
		}

	}
}
