package ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JTextLink extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	private JTextField label;

	private JLabel link = new JLabel(" > ");

	public JTextLink(String text) {
		label = new JTextField(text);

		link.setBorder(label.getBorder());
		link.setOpaque(true);		
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				link.setBackground(label.getBackground());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				link.setBackground(Color.decode("#1A86C8"));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(label.getText().startsWith("https")) {
					try {
						Desktop.getDesktop().browse(new URI(label.getText()));
					} catch (IOException | URISyntaxException e) {
						System.err.println("URL could not be opened: " + label.getText());
					}
				}
			}
		});

		setLayout(new GridBagLayout());
		add(label, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,0,0,2), 0, 0));
		add(link,  new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0, 0));
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
}