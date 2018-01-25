package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.itextpdf.text.DocumentException;

import fm.ParagraphTag;
import fm.ParagraphTagsDialog;
import log.LogDialog;
import pdf.PDFPropertiesUpdater;
import pdf.XMPUpdateDialog;
import ui.DesktopPublisherAssistant;

public class Runner {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			setLookAndFeel();
			//runMainProgram();
			
			testLogDialog();
			//testXMPUpdateDialog();
			//testParagraphTagsDialog();
			//testPDFProperties();
		});
	}
	
	public static void runMainProgram() {
		new DesktopPublisherAssistant().setVisible(true);
	}

	public static void testXMPUpdateDialog() {
		new XMPUpdateDialog().setVisible(true);
	}
	
	public static void testLogDialog() {
		new LogDialog(new DesktopPublisherAssistant().mainWindow).setVisible(true);
	}
	
	public static void testParagraphTagsDialog() {
		for(ParagraphTag tagType : ParagraphTag.TAG_TYPES) {
			System.out.println(tagType);
			new ParagraphTagsDialog().setVisible(true);
		}
	}

	public static void testPDFProperties() {
		String path = "D:\\users\\aporrell\\Desktop\\Demonstration";
		String pathToPDF = path + "\\01.pdf";
		//String pathToOutputPDF = path + "\\376627b_updated.pdf";

		try {
			PDFPropertiesUpdater.updateOpenProperties(pathToPDF);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
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
