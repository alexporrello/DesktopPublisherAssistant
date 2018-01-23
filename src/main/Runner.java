package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.itextpdf.text.DocumentException;

import pdf.XMPWork;

public class Runner {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			setLookAndFeel();
			//new DesktopPublisherAssistant().setVisible(true);
			//new LogDialog().setVisible(true);
			
			//for(ParagraphTag tagType : ParagraphTag.TAG_TYPES) {
			//new ParagraphTagsDialog().setVisible(true);
			//}
				
				try {
					new XMPWork();
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
