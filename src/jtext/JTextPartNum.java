package jtext;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class JTextPartNum extends JTextCopy {
	private static final long serialVersionUID = -4515503589333680143L;

	public JTextPartNum(String text) {
		super(text);
	}

	@Override
	public void buttonAction() {
		String[] split = getTextField().getText().split("-");
		
		StringSelection stringSelection;
		String selection = "";
		
		Boolean partNum = getTextField().getText().contains("32") || getTextField().getText().contains("37");
		
		if(partNum && split.length == 2) {
			selection = split[0].toLowerCase();
			
			if(split[0].startsWith("32")) {
				selection = "P" + selection;
			}
			
			if(!split[1].equals("01")) { 
				selection = selection + "_" + split[1];
			}
			
			stringSelection = new StringSelection(selection);
		} else {
			stringSelection = new StringSelection(getTextField().getText());
		}

		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
}
