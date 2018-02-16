package jm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Tools;

public class JMButton extends JLabel {
	private static final long serialVersionUID = 5773449309163002302L;

	public final static int STYLE_TEXT = 0;
	public final static int STYLE_CLOSE_BUTTON = 1;

	/** Set to true if the mouse has been pressed; else, false. **/
	private Boolean mousePressed = false;

	/** Used when user hovers over log **/
	private Color originalColor;

	Boolean mouseEntered = false;

	Color   drawColor    = getBackground();

	private int style = JMButton.STYLE_TEXT;

	public JMButton(String s) {
		super(s);

		setHorizontalAlignment(SwingConstants.CENTER);
		addMouseListener();
	}

	/**
	 * 
	 * @param style may be one of the following:
	 * <ul>
	 * 		<li> {@link #STYLE_CLOSE_BUTTON}
	 * <ul>
	 */
	public JMButton(int style) {
		super("");
		
		this.style = style;

		setHorizontalAlignment(SwingConstants.CENTER);
		addMouseListener();
	}

	/** Makes this function like a button **/
	public void addMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				mousePressed = false;

				if(contains(e.getPoint())) {
					drawColor = Tools.HOVER_COLOR;
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {				
				drawColor = originalColor;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(mousePressed) {
					drawColor = Tools.CLICK_COLOR;
				} else {
					originalColor = getBackground();
					drawColor = Tools.HOVER_COLOR;
				}

				repaint();
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {		
		Graphics2D gg = (Graphics2D) g;

		gg.setColor(drawColor);
		gg.fillRect(0, 0, getWidth(), getHeight());

		if(style == JMButton.STYLE_CLOSE_BUTTON) {
			drawCloseButton(gg);
		}

		super.paintComponent(g);
	}
	
	/**
	 * Draws the close button if the current style is set to {@link #STYLE_CLOSE_BUTTON}.
	 */
	private void drawCloseButton(Graphics2D gg) {
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gg.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		gg.setColor(Color.RED);

		int border = getHeight()/4;
		
		gg.drawLine(border, border, getWidth()-border, getHeight()-border);
		gg.drawLine(border, getWidth()-border, getHeight()-border, border);
	}
}