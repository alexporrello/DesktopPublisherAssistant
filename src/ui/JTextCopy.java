package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JTextCopy extends JPanel {
	private static final long serialVersionUID = -2231714098377893220L;

	private JTextField label;

	private JLabel link = new JLabel(" ⎘ ");

	public JTextCopy(String text) {
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
			public void mouseReleased(MouseEvent arg0) {
				if(link.contains(arg0.getPoint())) {
					StringSelection stringSelection = new StringSelection(label.getText());
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clpbrd.setContents(stringSelection, null);
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