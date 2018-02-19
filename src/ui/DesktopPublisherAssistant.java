package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import jm.JMScrollPane;
import log.LogWindow;
import pdf.XMPUpdateWindow;
import ticket.Ticket;

/**
 * Created to aid in keeping track of information for PPMs.
 * @author Alexander Porrello
 */
public class DesktopPublisherAssistant extends JFrame {
	private static final long serialVersionUID = 1165657274713594230L;

	public MainWindow mainWindow = new MainWindow(this);

	public LogWindow logWindow = new LogWindow(mainWindow);
	
	public XMPUpdateWindow xmpUpdate = new XMPUpdateWindow();
	
	public DesktopPublisherAssistant() {
		setup();
	}

	public DesktopPublisherAssistant(String url) {
		setup();

		try {
			Ticket.readLogFile(url, mainWindow);
			setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Desktop Publisher Assistant");
		setJMenuBar(new MenuBar(mainWindow, logWindow, xmpUpdate, this));
		setIconImages(Tools.imageIcon());
		setSize(new Dimension(1000, 530));
		setLayout(new BorderLayout());
		setLocationByPlatform(true);

		addMainWindow();
		addXMPUpdateWindow();
		add(logWindow, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {			
			@Override
			public void windowActivated(WindowEvent arg0) {
				mainWindow.autoAddString(Tools.getCopiedText());
			}
		});
	}
	
	private void addXMPUpdateWindow() {
		Border inside = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Border outside = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,0,0,5), inside);
		
		xmpUpdate.setBorder(outside);
		
		add(xmpUpdate, BorderLayout.EAST);
	}
	
	private void addMainWindow() {
		JMScrollPane scroll = new JMScrollPane(mainWindow);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		mainWindow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		

		Border outside = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,0,-1), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		scroll.setBorder(outside);
		
		add(scroll, BorderLayout.CENTER);
	}
}
