package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fm.ParagraphTag;
import fm.ParagraphTagsDialog;
import ui.DesktopPublisherAssistant;

public class Runner {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			setLookAndFeel();
			runMainProgram();

			//testParagraphTagsDialog();
		});
	}
	
	public static void runMainProgram() {
		new DesktopPublisherAssistant().setVisible(true);
	}

	public static void testParagraphTagsDialog() {
		for(ParagraphTag tagType : ParagraphTag.TAG_TYPES) {
			System.out.println(tagType);
			new ParagraphTagsDialog().setVisible(true);
		}
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
