package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class JTextButton extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	JTextField label;

	JLabel link;

	public JTextButton(String text, String buttonText) {
		label = new JTextField(text);
		link  = new JLabel(buttonText);

		link.addMouseListener(createMouseAdapter());
		link.setBackground(label.getBackground());
		link.setBorder(label.getBorder());
		link.setOpaque(true);
		
		setLayout(new GridBagLayout());
		add(label, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.BOTH,     new Insets(0,0,0,2), 0, 0));
		add(link,  new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, 
				GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));
	}
	
	private MouseAdapter createMouseAdapter() {
		return new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				link.setBackground(label.getBackground());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				link.setBackground(Color.decode("#1A86C8"));
			}
		};
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