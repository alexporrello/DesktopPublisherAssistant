package ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTextCopy extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextCopy(String text) {
		super(text, " ⎘ ");
		
		addMouseReleasedListener();
	}

	@Override
	public void addMouseReleasedListener() {
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(link.contains(arg0.getPoint())) {
					StringSelection stringSelection = new StringSelection(label.getText());
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clpbrd.setContents(stringSelection, null);
				}
			}
		});
	}
}