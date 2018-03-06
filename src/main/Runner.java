package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fm.ParagraphTag;
import fm.ParagraphTagsDialog;
import jm.JMButton;
import pdf.XMPUpdateWindow;
import ticket.Ticket;
import ui.DesktopPublisherAssistant;

public class Runner {



	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {	
			if(!Ticket.TICKET_URL.exists()) {
				Ticket.TICKET_URL.mkdir();
			}

			Boolean runMainprogram = true;

			if(runMainprogram) {
				setLookAndFeel();
				runMainProgram();
			} else {
				componentTest(new JMButton(" Click It"));			
				///runXMPUpdateWindowDialog();
				//testParagraphTagsDialog();
			}
		});
	}

	public static void componentTest(Component component) {
		JFrame frame = new JFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(300, 150));
		frame.setLayout(new BorderLayout(10, 10));
		
		frame.add(component, BorderLayout.CENTER);
		
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
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
