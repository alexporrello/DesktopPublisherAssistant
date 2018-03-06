package jm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.Timer;

import ui.Tools;

public class JMTextField extends JTextField {
	private static final long serialVersionUID = -2346021547935315452L;

	Boolean replaceSlashWithDash = false;
	
	Color borderColor = JMColor.DEFAULT_BORDER_COLOR;

	Timer fadeTimer;
	
	public JMTextField() {
		setupTextField();
	}

	public JMTextField(String s) {
		super(s);
		setupTextField();
	}
	
	public JMTextField(String s, Boolean replaceSlashWithDash) {
		super(s);
		
		this.replaceSlashWithDash = replaceSlashWithDash;
		
		setupTextField();
	}

	private void setupTextField() {
		copyContentsOnDoubleClick();
		selectAllWhenFocused();
		trimPastedStrings();
		setBackground(JMColor.DEFAULT_BACKGROUND);
		setForeground(JMColor.DEFAULT_FONT_COLOR);
		
		createBorder(Color.LIGHT_GRAY);
		
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				fadeColor(JMColor.HOVER_BORDER_COLOR, 0);
			}
			
			@Override
			public void focusLost(FocusEvent arg0) {
				fadeColor(JMColor.DEFAULT_BORDER_COLOR, 0);
			}
		});
	}

	/**
	 * Sets the border for this JTextField.
	 * @param borderColor is the color of the border.
	 */
	private void createBorder(Color borderColor) {
		this.borderColor = borderColor;
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		repaint();
	}

	/**
	 * Copies the entered text when user double-clicks.
	 */
	private void copyContentsOnDoubleClick() {
		addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
							new StringSelection(getText()), null);
					select(0, 0);
				}
			}
		});
	}

	/**
	 * Trims the whitespace off of strings that are pasted into the program.
	 */
	private void trimPastedStrings() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_V) {					
					arg0.consume();
					setText(Tools.getCopiedText().trim());
					
					if(replaceSlashWithDash) {
						setText(Tools.getCopiedText().trim().replace("/", "-"));
					}
				}
			}
		});
	}

	/**
	 * Selects all text when the text field receives focus.
	 */
	private void selectAllWhenFocused() {
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				selectAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				select(0, 0);
				removeEndSpace();
			}
		});
	}

	/**
	 * If a given JTextField ends with a space, remove it.
	 * @param toRemove the JTextField to be changed.
	 */
	private void removeEndSpace() {
		String text = this.getText();
		int carePosition = this.getCaretPosition();

		if(text.endsWith(" ")) {
			text = text.substring(0, text.length()-1);
		}

		this.setText(text);		
		this.setCaretPosition(carePosition);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(borderColor);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
	
/** ================================== COLOR FADING CODE ================================== **/
	
	
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
