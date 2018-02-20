package jm;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class JMTextField extends JTextField {
	private static final long serialVersionUID = -2346021547935315452L;

	public JMTextField() {
		setupTextField();
	}

	public JMTextField(String s) {
		super(s);
		setupTextField();
	}
	
	private void setupTextField() {
		copyContentsOnDoubleClick();
		selectAllWhenFocused();
		
		createBorder(Color.LIGHT_GRAY);
	}

	/**
	 * Sets the border for this JTextField.
	 * @param borderColor is the color of the border.
	 */
	private void createBorder(Color borderColor) {
		setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(borderColor, 1), 
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
