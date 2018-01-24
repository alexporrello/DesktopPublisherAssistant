package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.DesktopPublisherAssistant;

public class Runner {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			setLookAndFeel();
			new DesktopPublisherAssistant().setVisible(true);
			
			//new XMPUpdateDialog().setVisible(true);
			
			//for(ParagraphTag tagType : ParagraphTag.TAG_TYPES) {
			//new ParagraphTagsDialog().setVisible(true);
			//}
		});
	}

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
