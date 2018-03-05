package jtext;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;

import jm.JMButton;
import jm.JMTextField;

public abstract class JTextButton extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	private JMTextField textField;

	private JMButton button;

	private Boolean disabledWhenEmpty = true;

	public JTextButton(String text, String buttonText) {
		setUpButton(text, buttonText, true);
	}

	public JTextButton(String text, String buttonText, Boolean disabledWhenEmpty) {
		setUpButton(text, buttonText, disabledWhenEmpty);
	}

	private void setUpButton(String text, String buttonText, Boolean disabledWhenEmpty) {
		this.disabledWhenEmpty = disabledWhenEmpty;
		
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		textField = new JMTextField(text);
		button    = new JMButton(buttonText);
		
		textField.addKeyListener(createKeyAdapter());
		add(textField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.BOTH,     new Insets(0,0,0,2), 0, 0));
		
		button.addActionListner(e -> buttonAction());
		button.setButtonEnabled(false);
		add(button,  new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));
		
		if(text.length() > 0 || !disabledWhenEmpty) {
			button.setButtonEnabled(true);
		}
	}

	private KeyAdapter createKeyAdapter() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(disabledWhenEmpty) {
					button.setButtonEnabled(textField.getText().length() > 0);
				}
			}
		};
	}

	public void setTextIfEmpty(String toSet) {
		if(textField.getText().length() == 0) {
			setText(toSet);
			button.setButtonEnabled(true);
		}
	}

	public void setText(String toSet) {
		textField.setText(toSet);

		button.setButtonEnabled(toSet.length() > 0);
	}
	
	public String getText() {
		return textField.getText();
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setCaretPosition(int caretPosition) {
		textField.setCaretPosition(caretPosition);
	}

	/**
	 * The action completed when the button is pressed.
	 */
	public abstract void buttonAction();
}