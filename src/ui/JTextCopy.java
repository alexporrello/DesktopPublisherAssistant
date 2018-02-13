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
	}

	@Override
	public void addMouseReleasedListener() {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(button.contains(arg0.getPoint())) {
					StringSelection stringSelection = new StringSelection(getTextField().getText());
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clpbrd.setContents(stringSelection, null);
				}
			}
		});
	}
}