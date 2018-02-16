package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class JTextButton extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	private JTextField textField;

	JLabel button;

	private Color originalBackground;

	private Boolean pressed = false;

	private Boolean enabled = false;

	private Boolean disabledWhenEmpty = true;

	public JTextButton(String text, String buttonText) {
		setUpButton(text, buttonText, true);
	}

	public JTextButton(String text, String buttonText, Boolean disabledWhenEmpty) {
		setUpButton(text, buttonText, disabledWhenEmpty);
	}
	
	private void setUpButton(String text, String buttonText, Boolean disabledWhenEmpty) {
		textField = new JTextField(text);
		button  = new JLabel(buttonText);
		
		this.disabledWhenEmpty = disabledWhenEmpty;

		button.addMouseListener(createMouseAdapter());
		button.setBackground(textField.getBackground());
		button.setBorder(textField.getBorder());
		button.setOpaque(true);

		textField.addKeyListener(createKeyAdapter());

		setBorderColor(Tools.DEFAULT_BORDER_COLOR);

		setLayout(new GridBagLayout());
		add(textField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.BOTH,     new Insets(0,0,0,2), 0, 0));
		add(button,  new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));

		enableButton(false);

		addMouseReleasedListener();
	}
	
	private KeyAdapter createKeyAdapter() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!disabledWhenEmpty) {
					enableButton(textField.getText().length() > 0);
				}
			}
		};
	}

	private void enableButton(Boolean enable) {
		this.enabled = enable;

		if(this.enabled || !disabledWhenEmpty) {
			setBorderColor(Tools.DEFAULT_BORDER_COLOR);
			button.setForeground(Color.BLACK);
			button.setBackground(Color.WHITE);
		} else {
			setBorderColor(Tools.DISABLED_BORDER_COLOR);
			button.setForeground(Color.decode("#838383"));
			button.setBackground(Tools.DISABLED_COLOR);
		}
	}

	private MouseAdapter createMouseAdapter() {
		return new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(enabled || !disabledWhenEmpty) {
					pressed = false;

					setBorderColor(Tools.HOVEER_BORDER_COLOR);
					button.setBackground(Tools.HOVER_COLOR);		
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if(enabled || !disabledWhenEmpty) {
					button.setBackground(Tools.CLICK_COLOR);
					pressed = true;
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if(enabled || !disabledWhenEmpty) {
					if(pressed) {
						setBorderColor(Tools.HOVEER_BORDER_COLOR);
						button.setBackground(Tools.HOVER_COLOR);
					} else {
						setBorderColor(Tools.DEFAULT_BORDER_COLOR);
						button.setBackground(originalBackground);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(enabled || !disabledWhenEmpty) {
					if(!pressed) {
						originalBackground = button.getBackground();
						button.setBackground(Tools.HOVER_COLOR);
					} else {
						button.setBackground(Tools.CLICK_COLOR);
					}

					setBorderColor(Tools.HOVEER_BORDER_COLOR);
				}
			}
		};
	}

	private void setBorderColor(Color borderColor) {
		button.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(borderColor, 1), 
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));
	}

	public String getText() {
		return textField.getText();
	}

	public void setTextIfEmpty(String toSet) {
		if(textField.getText().length() == 0) {
			setText(toSet);
		}
	}

	public void setText(String toSet) {
		textField.setText(toSet);

		enableButton(toSet.length() > 0);
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setCaretPosition(int caretPosition) {
		textField.setCaretPosition(caretPosition);
	}

	public abstract void addMouseReleasedListener();
}