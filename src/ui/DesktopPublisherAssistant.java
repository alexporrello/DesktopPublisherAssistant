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

import jm.JMColor;
import jm.JMExpandablePanel;
import jm.JMExpandablePanel.Anchor;
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
		setJMenuBar(new MenuBar(mainWindow, logWindow, xmpUpdate, this));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(JMColor.DEFAULT_BACKGROUND);
		setTitle("Desktop Publisher Assistant");
		setSize(new Dimension(1000, 535));
		setIconImages(Tools.imageIcon());
		setLayout(new BorderLayout());
		setLocationByPlatform(true);

		getContentPane().setBackground(JMColor.DEFAULT_BACKGROUND);

		addMainWindow();
		addXMPUpdateWindow();

		JMExpandablePanel expand = new JMExpandablePanel(logWindow, Anchor.SOUTH);
		
		Border outside = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);
		Border inside  = BorderFactory.createEmptyBorder(-1, 5, 5, 5);
		Border compound = BorderFactory.createCompoundBorder(inside, outside);
		expand.setBorder(compound);
		
		add(expand, BorderLayout.SOUTH);



		addWindowListener(new WindowAdapter() {			
			@Override
			public void windowActivated(WindowEvent arg0) {
				mainWindow.autoAddString(Tools.getCopiedText());
			}
		});
	}

	private void addXMPUpdateWindow() {
		xmpUpdate.setBorder(BorderFactory.createEmptyBorder(5,0,5,5));
		JMExpandablePanel expand = new JMExpandablePanel(xmpUpdate, Anchor.EAST);
		
		Border outside = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);
		Border inside  = BorderFactory.createEmptyBorder(5, -1, 0, 5);
		Border compound = BorderFactory.createCompoundBorder(inside, outside);
		expand.setBorder(compound);

		add(expand, BorderLayout.EAST);
	}

	private void addMainWindow() {
		mainWindow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JMScrollPane scroll = new JMScrollPane(mainWindow);
		scroll.getViewport().setBackground(JMColor.DEFAULT_BACKGROUND);
		scroll.setOpaque(false);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		Border outside = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);
		Border inside  = BorderFactory.createEmptyBorder(5, 5, 0, 0);
		Border compound = BorderFactory.createCompoundBorder(inside, outside);
		scroll.setBorder(compound);

		add(scroll, BorderLayout.CENTER);
	}
}
