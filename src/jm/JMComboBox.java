package jm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Timer;

import com.itextpdf.awt.geom.Point;

public class JMComboBox<E> extends JMLabel {
	private static final long serialVersionUID = 6536069199661574663L;

	String[] elements;

	int i = 0;

	Color borderColor = JMColor.DEFAULT_BACKGROUND;

	Color fontColor = Color.BLACK;

	Timer fadeTimer;

	public JMComboBox(String[] elements) {
		super(elements[0]);

		this.elements = elements;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(new Rectangle(2, 2, getHeight()-4, getHeight()-4).contains(e.getPoint())) {
					selectLeftmostObject();
				} else {
					selectRightmostObject();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				new ColorFader().fadeColor(JMColor.HOVER_BORDER_COLOR, 1);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				new ColorFader().fadeColor(JMColor.DEFAULT_BACKGROUND, 1);
			}
		});	

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				Rectangle leftButton  = new Rectangle(2,                          2, getHeight()-4, getHeight()-4);
				Rectangle rightButton = new Rectangle((getWidth()-getHeight())+2, 2, getHeight()-4, getHeight()-4);

				if(leftButton.contains(arg0.getPoint()) || rightButton.contains(arg0.getPoint())) {
					new ColorFader().fadeColor(JMColor.PRESS_COLOR, 1);
				} else {
					new ColorFader().fadeColor(JMColor.DEFAULT_BORDER_COLOR, 1);
				}
			}
		});
	}

	public void setSelectedIndex(Integer i) {
		this.i = i;

		setText(elements[i]);
	}

	/** Selects the element to the left of the current element, 
	 * unless the currently selected element is zero
	 */
	private void selectLeftmostObject() {
		if(i > 0) {
			i-=1;
		} else {
			i = elements.length-1;
		}

		setSelectedIndex(i);
	}

	private void selectRightmostObject() {
		if(i < elements.length-1) {
			i+=1;
		} else {
			i = 0;
		}

		setSelectedIndex(i);
	}

	public int getSelectedIndex() {
		return i;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gg = (Graphics2D) g;

		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gg.setColor(borderColor);
		gg.fillRoundRect(2, 2, getHeight()-4, getHeight()-4, 4, 4);
		gg.fillRoundRect((getWidth()-getHeight())+2, 2, getHeight()-4, getHeight()-4, 4, 4);

		gg.setColor(Color.BLACK);
		gg.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

		Point[] left = drawArrow(new Rectangle(2, 2, getHeight()-4, getHeight()-4), 0);
		gg.drawLine((int)left[0].x, (int)left[0].y, (int)left[1].x, (int)left[1].y);
		gg.drawLine((int)left[0].x, (int)left[0].y, (int)left[2].x, (int)left[2].y);
		
		Point[] right = drawArrow(new Rectangle((getWidth()-getHeight())+2, 2, getHeight()-4, getHeight()-4), 1);
		gg.drawLine((int)right[0].x, (int)right[0].y, (int)right[1].x, (int)right[1].y);
		gg.drawLine((int)right[0].x, (int)right[0].y, (int)right[2].x, (int)right[2].y);
	}

	/**
	 * Draws an arrow inside of given rectangular bounds.
	 * @param r the bounds of the arrow.
	 * @param direction 
	 * @return
	 */
	private Point[] drawArrow(Rectangle r, int direction) {
		Point a = new Point(r.x,           r.y);
		Point b = new Point(r.x + r.width, r.y);
		Point c = new Point(r.x,           r.y + r.height);
		Point d = new Point(r.x + r.width, r.y + r.height);

		int x = 7;
		int y = 5;

		Point nose;
		Point top;
		Point bot;
		
		if(direction == 0) {
			nose = new Point(a.x + 5, ((double)(a.y + c.y))/2);
			top  = new Point(b.x-x, b.y+y);
			bot  = new Point(d.x-x, d.y-y);
		} else {
			nose = new Point(b.x - 6, ((double)(a.y + c.y))/2);
			top  = new Point(a.x+x-1, b.y+y);
			bot  = new Point(c.x+x-1, d.y-y);
		}

		return new Point[]{nose, top, bot};
	}

	/** Responsible for the color fading animation **/
	private class ColorFader {		
		private void fadeColor(Color fadeToButton, int fadeSpeed) {
			if(fadeTimer != null && fadeTimer.isRunning()) {
				fadeTimer.stop();
			}

			fadeTimer = new Timer(fadeSpeed, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					colorFader(borderColor, fadeToButton);
					repaint();
				}    
			});

			fadeTimer.start();
		}

		private void colorFader(Color buttonfadeFrom, Color buttonFadeTo) {		
			int[] buttonA = makeRGBArray(buttonfadeFrom);
			int[] buttonB = makeRGBArray(buttonFadeTo);

			buttonA = fader(buttonA, buttonB);

			borderColor = arrayToColor(buttonA);

			if((buttonA[0] + "" + buttonA[1] + "" + buttonA[2]).equals(buttonB[0] + "" + buttonB[1] + "" + buttonB[2])) {
				repaint();
				fadeTimer.stop();
			}
		}

		/**
		 * Adds or subtracts 1 so that values of a will equal values of b.
		 * @param a the array whose values are changed
		 * @param b the array whose values are static
		 * @return an updated
		 */
		private int[] fader(int[] a, int[] b) {
			for(int i = 0; i < 3; i++) {
				if(a[i] > b[i]) {
					a[i] = a[i]-1;
				} else if(a[i] < b[i]) {
					a[i] = a[i]+1;
				}
			}

			return a;
		}

		/**
		 * Receives a color and returns the color represented as an array of integers.
		 * @param color the color to be converted
		 * @return an array of size three where...
		 * <ul>
		 * 		<li>0: Red</li>
		 *      <li>1: Green</li>
		 *      <li>2: Blue</li>
		 * </ul>
		 */
		private int[] makeRGBArray(Color color) {
			return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
		}

		/**
		 * Receives an array representation of a color and returns a color.
		 * @param rgb accepts array of integers made in {@link #makeRGBArray(Color)}.
		 * @return the Color represented by rgb.
		 */
		private Color arrayToColor(int[] rgb) {
			return new Color(rgb[0], rgb[1], rgb[2]);
		}
	}
}
