package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itextpdf.text.Font;

public abstract class JTextButton extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	JTextField label;

	JLabel link;

	Color originalBackground;

	Boolean pressed = false;

	Boolean hovered = false;

	Boolean enabled = false;

	public JTextButton(String text, String buttonText) {
		label = new JTextField(text);
		link  = new JLabel(buttonText);

		link.addMouseListener(createMouseAdapter());
		link.setBackground(label.getBackground());
		link.setBorder(label.getBorder());
		link.setOpaque(true);

		label.addKeyListener(createKeyListener());
		
		setBorderColor(Tools.DEFAULT_BORDER_COLOR);

		setLayout(new GridBagLayout());
		add(label, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.BOTH,     new Insets(0,0,0,2), 0, 0));
		add(link,  new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));

		enableButton(false);

		addMouseReleasedListener();
	}

	private KeyListener createKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				enableButton(link.getText().length() > 0);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		};
	}
	
	private void enableButton(Boolean enable) {
		if(enable) {
			enabled = true;
			
			setBorderColor(Tools.DEFAULT_BORDER_COLOR);
			link.setForeground(Color.BLACK);
			link.setBackground(Color.WHITE);
		} else {
			enabled = false;
			
			setBorderColor(Tools.DISABLED_BORDER_COLOR);
			link.setForeground(Color.decode("#838383"));
			link.setBackground(Tools.DISABLED_COLOR);
		}
	}

	private MouseAdapter createMouseAdapter() {
		return new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(enabled) {
					pressed = false;

					setBorderColor(Tools.HOVEER_BORDER_COLOR);
					link.setBackground(Tools.HOVER_COLOR);		
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				if(enabled) {
					link.setBackground(Tools.CLICK_COLOR);
					pressed = true;
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if(enabled) {
					if(pressed) {
						setBorderColor(Tools.HOVEER_BORDER_COLOR);
						link.setBackground(Tools.HOVER_COLOR);
					} else {
						setBorderColor(Tools.DEFAULT_BORDER_COLOR);
						link.setBackground(originalBackground);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(enabled) {
					if(!pressed) {
						originalBackground = link.getBackground();
						link.setBackground(Tools.HOVER_COLOR);
					} else {
						link.setBackground(Tools.CLICK_COLOR);
					}

					setBorderColor(Tools.HOVEER_BORDER_COLOR);
				}
			}
		};
	}

	private void setBorderColor(Color borderColor) {
		link.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(borderColor, 1), 
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));
	}

	public String getText() {
		return label.getText();
	}

	public void setText(String toSet) {
		label.setText(toSet);
	}

	public JTextField getTextField() {
		return label;
	}

	public void setCaretPosition(int caretPosition) {
		label.setCaretPosition(caretPosition);
	}

	public abstract void addMouseReleasedListener();
}