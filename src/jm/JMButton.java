package jm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

	/** Used when user hovers over log **/
	private Color originalBackground;

	/** The color of the button's background **/
	private Color drawColor = JMColor.DEFAULT_BACKGROUND;

	/** Determines what is displayed on the button **/
	private int style = JMButton.STYLE_TEXT;

	
	public JMButton(String s, Consumer<KeyEvent> keyListener, Consumer<MouseEvent> mouseListener) {
		super(s);

		setupJMButton(keyListener, mouseListener);
	}

	/**
	 * @param style may be one of the following:
	 * <ul>
	 * 		<li> {@link #STYLE_CLOSE_BUTTON}
	 * <ul>
	 */
	public JMButton(int style, Consumer<KeyEvent> keyListener, Consumer<MouseEvent> mouseListener) {		
		this.style = style;

		setupJMButton(keyListener, mouseListener);
	}

	
	public void setupJMButton(Consumer<KeyEvent> keyListener, Consumer<MouseEvent> mouseListener) {
		setHorizontalAlignment(SwingConstants.CENTER);
		setFocusable(true);
		setOpaque(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER && enabled) {
					keyListener.accept(arg0);
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(contains(arg0.getPoint()) && enabled) {
					mouseListener.accept(arg0);
				}
			}
		});

		addFocusListener();
		addMouseListener(createMouseAdapter());
	}

	public void addFocusListener() {
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(enabled) {
					setBorder(JMColor.HOVER_BORDER_COLOR);
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if(enabled) {
					setBorder(JMColor.DEFAULT_BORDER_COLOR);
				}
			}
		});
	}

	private MouseAdapter createMouseAdapter() {
		return new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(enabled) {
					pressed = false;

					setBorder(JMColor.HOVER_BORDER_COLOR);
					setBackground(JMColor.HOVER_COLOR);		
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if(enabled) {
					setBackground(JMColor.PRESS_COLOR);
					pressed = true;
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if(enabled) {
					if(pressed) {
						setBorder(JMColor.HOVER_BORDER_COLOR);
						setBackground(JMColor.HOVER_COLOR);
					} else {
						setBorder(JMColor.DEFAULT_BORDER_COLOR);
						setBackground(originalBackground);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(enabled) {
					if(!pressed) {
						originalBackground = getBackground();
						setBackground(JMColor.HOVER_COLOR);
					} else {
						setBackground(JMColor.PRESS_COLOR);
					}

					setBorder(JMColor.HOVER_BORDER_COLOR);
				}
			}
		};
	}


	public void setButtonEnabled(Boolean enable) {
		this.enabled = enable;
		
		if(this.enabled) {
			setBorder(JMColor.DEFAULT_BORDER_COLOR);
			setForeground(JMColor.DEFAULT_FOREGROUND);
			setBackground(JMColor.DEFAULT_BACKGROUND);
		} else {
			setBorder(JMColor.DISABLED_BORDER_COLOR);
			setForeground(JMColor.DISABLED_FOREGROUND_COLOR);
			setBackground(JMColor.DISABLED_BACKGROUND_COLOR);
		}
		
		setFocusable(this.enabled);
	}
	
	private void setBorder(Color color) {
		Tools.setBorderColor(this, color);
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