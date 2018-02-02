package main;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fm.ParagraphTag;
import fm.ParagraphTagsDialog;
import pdf.XMPUpdateWindow;
import ui.DesktopPublisherAssistant;

public class Runner {



	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {			
			setLookAndFeel();
			runMainProgram();
			///runXMPUpdateWindowDialog();
						

			//testParagraphTagsDialog();
		});
	}
	
	public static void runXMPUpdateWindowDialog() {
		JDialog dialog = new JDialog();
		dialog.add(new XMPUpdateWindow(true));
		dialog.setSize(new Dimension(300, 300));
		dialog.setVisible(true);
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
