package jm;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import ui.Tools;

public class JMTextField extends JTextField {
	private static final long serialVersionUID = -2346021547935315452L;

	Boolean replaceSlashWithDash = false;
	
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
	}

	/**
	 * Sets the border for this JTextField.
	 * @param borderColor is the color of the border.
	 */
	private void createBorder(Color borderColor) {
		setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor), 
						BorderFactory.createEmptyBorder(3, 3, 3, 3)));
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
				createBorder(JMColor.HOVER_BORDER_COLOR);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				select(0, 0);
				removeEndSpace();
				createBorder(Color.LIGHT_GRAY);
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

}
