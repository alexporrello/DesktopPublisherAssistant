package ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class JTextCopy extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextCopy(String text) {
		super(text, " ⎘ ");
	}

	@Override
	public void buttonAction() {
		StringSelection stringSelection = new StringSelection(getTextField().getText());
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
}