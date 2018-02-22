package jm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Tools;

public class JMButton extends JLabel {
	private static final long serialVersionUID = 5773449309163002302L;

	public final static int STYLE_TEXT = 0;
	public final static int STYLE_CLOSE_BUTTON = 1;

	/** Determines whether the button functions or not **/
	private Boolean enabled = true;

	/** Set to true if the mouse has been pressed; else, false. **/
	private Boolean pressed = false;

	/** The color of the button's background **/
	private Color background = JMColor.DEFAULT_BACKGROUND;

	/** The color of the button's border **/
	private Color border = JMColor.DEFAULT_BORDER_COLOR;

	/** Determines what is displayed on the button **/
	private int style = JMButton.STYLE_TEXT;


	public JMButton(String s) {
		super(s);

		setupJMButton();
	}

	/**
	 * @param style may be one of the following:
	 * <ul>
	 * 		<li> {@link #STYLE_CLOSE_BUTTON}
	 * <ul>
	 */
	public JMButton(int style) {
		this.style = style;

		setupJMButton();
	}

	public void setupJMButton() {
		setHorizontalAlignment(SwingConstants.CENTER);
		setFocusable(true);
		setOpaque(false);
		setBorder();

		addFocusListener(createFocusListener());
		addMouseListener(createMouseAdapter());
	}

	public void addActionListner(Consumer<InputEvent> listener) {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if((arg0.getKeyCode() == KeyEvent.VK_ENTER || arg0.getKeyCode() == KeyEvent.VK_SPACE) && enabled) {
					listener.accept(arg0);
					requestFocus(true);
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(contains(arg0.getPoint()) && enabled) {
					listener.accept(arg0);
					requestFocus(true);
				}
			}
		});
	}

	public FocusListener createFocusListener() {
		return new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(enabled) {
					border = JMColor.HOVER_BORDER_COLOR;
					background = JMColor.HOVER_COLOR;
				}
				
				repaint();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(enabled) {
					border = JMColor.DEFAULT_BORDER_COLOR;
					background = JMColor.DEFAULT_BACKGROUND;
				}

				repaint();
			}
		};
	}

	private MouseAdapter createMouseAdapter() {
		return new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(enabled) {
					pressed = false;

					if(!hasFocus()) {
						border     = JMColor.DEFAULT_BORDER_COLOR;
						background = JMColor.DEFAULT_BACKGROUND;
					} else {
						border = JMColor.HOVER_BORDER_COLOR;
						background = JMColor.HOVER_COLOR;
					}
				}

				repaint();
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if(enabled) {
					background = JMColor.PRESS_COLOR;
					pressed = true;
				}

				repaint();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if(enabled) {
					if(pressed) {
						border = JMColor.HOVER_BORDER_COLOR;
						background = JMColor.HOVER_COLOR;
					} else if(!hasFocus()) {
						border = JMColor.DEFAULT_BORDER_COLOR;
						background = JMColor.DEFAULT_BACKGROUND;
					}
				}

				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(enabled) {
					if(!pressed) {
						border = JMColor.HOVER_BORDER_COLOR;
						background = JMColor.HOVER_COLOR;
					} else {
						border = JMColor.HOVER_BORDER_COLOR;
						background = JMColor.PRESS_COLOR;
					}
				}

				repaint();
			}
		};
	}


	public void setButtonEnabled(Boolean enable) {
		this.enabled = enable;

		if(this.enabled) {
			setForeground(JMColor.DEFAULT_FOREGROUND);

			border = JMColor.DEFAULT_BORDER_COLOR;
			background   = JMColor.DEFAULT_BACKGROUND;
		} else {
			setForeground(JMColor.DISABLED_FOREGROUND_COLOR);

			border = JMColor.DISABLED_BORDER_COLOR;
			background   = JMColor.DISABLED_BACKGROUND_COLOR;
		}

		setFocusable(this.enabled);
		repaint();
	}

	private void setBorder() {
		Tools.setBorder(this);
	}

	@Override
	public void paintComponent(Graphics g) {		
		Graphics2D gg = (Graphics2D) g;

		gg.setColor(background);
		gg.fillRect(0, 0, getWidth(), getHeight());

		gg.setColor(border);
		gg.drawRect(0, 0, getWidth()-1, getHeight()-1);

		if(style == JMButton.STYLE_CLOSE_BUTTON) {
			drawCloseButton(gg);
		} else {
			super.paintComponent(g);
		}
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