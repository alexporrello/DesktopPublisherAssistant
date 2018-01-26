package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import ticket.Ticket;
import ticket.TicketInfo;
import ui.DesktopPublisherAssistant.MainWindow;
import ui.JMPanel;
import ui.Tools;

/**
 * Displays all of the logs created by the Desktop publisher and their respective statuses.
 * @author Alexander Porrello
 */
public class LogWindow extends JMPanel {
	private static final long serialVersionUID = -1014191579466760962L;

	/** All of the ticket files currently in existence on the user's computer **/
	private ArrayList<ShortLog> shortLogs = new ArrayList<ShortLog>();

	private MainWindow mw;

	JScrollPane logDialogScroll = new JScrollPane(new LogsInWindow());

	public LogWindow(MainWindow mw) {
		this.mw = mw;

		logDialogScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logDialogScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logDialogScroll.setPreferredSize(new Dimension(900,150));
		logDialogScroll.setBorder(BorderFactory.createEmptyBorder());

		JLabel[] labels = {
				new JLabel("Jira Ticket Description") {
					private static final long serialVersionUID = -2564660927510249775L;
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
					}
				},
				new JLabel("Jira Report") {
					private static final long serialVersionUID = -2564660927510249776L;
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
					}
				},
				new JLabel("32 Part Num") {
					private static final long serialVersionUID = -2564660927510249777L;
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
					}
				},
				new JLabel("37 Part Num") {
					private static final long serialVersionUID = -2564660927510249778L;
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.setColor(Color.LIGHT_GRAY);
						g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
					}
				},
				new JLabel("Status"),
				new JLabel("")};

		int y = 0;

		JMPanel labelsPanel = new JMPanel();
		labelsPanel.setLayout(new GridBagLayout());

		labelsPanel.add(labels[0], Tools.createGBC(1, y, 1.0, new Insets(0,0,-1,0)));
		labelsPanel.add(labels[1], Tools.createGBC(2, y, 0.1, new Insets(0,0,-1,0)));
		labelsPanel.add(labels[2], Tools.createGBC(3, y, 0.1, new Insets(0,0,-1,0)));
		labelsPanel.add(labels[3], Tools.createGBC(4, y, 0.1, new Insets(0,0,-1,0)));
		labelsPanel.add(labels[4], Tools.createGBC(5, y, 0.0, new Insets(0,0,-1,0)));
		labelsPanel.add(labels[5], Tools.createGBC(6, y, 0.0, new Insets(0,0,-1,0)));
		
		for(JLabel label : labels) {
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setOpaque(true);
			label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		}
		
		labels[4].setPreferredSize(new Dimension(105,24));
		labels[1].setPreferredSize(new Dimension(100, 24));
		
		setLayout(new GridBagLayout());
		setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(-1,5,5,5),
						BorderFactory.createCompoundBorder(
								BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
								BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		add(labelsPanel,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(logDialogScroll, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,       new Insets(0, 0, 0, 0), 0, 0));
	}

	public class LogsInWindow extends JMPanel {
		private static final long serialVersionUID = 8218069729268454703L;

		public LogsInWindow() {
			setLayout(new GridBagLayout());
			addAllToLogEntryPanel();
			watchService();
		}

		/**
		 * Watches the Desktop Publisher Assistant for changes and updates the log
		 * if any occur.
		 */
		public void watchService() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						WatchService watcher = FileSystems.getDefault().newWatchService();
						Path dir = Ticket.TICKET_URL.toPath();

						dir.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);

						for (;;) {
							WatchKey key = watcher.take();
							for (WatchEvent<?> event : key.pollEvents()) {
								if (event.kind() == ENTRY_CREATE || event.kind() == ENTRY_DELETE) {
									System.out.println("true");
									addAllToLogEntryPanel();
									continue;
								}
							}
						}
					} catch(InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		/**
		 * Locates all tickets, adds them to {@link #shortLogs}, and adds them to {@link #logEntryPanel}.
		 */
		public void addAllToLogEntryPanel() {
			try {
				this.removeAll();
				shortLogs.clear();

				for(File f : Ticket.TICKET_URL.listFiles()) {
					shortLogs.add(setUpNewShortLog(f));
				}
				Collections.sort(shortLogs);

				int y = 0;

				for(ShortLog sl : shortLogs) {				
					if(y%2 == 0) {
						sl.getLabel(ShortLogLabel.JIRA_TICKET_DESCRIPTION).setBackground(Color.WHITE);
						sl.getLabel(ShortLogLabel.JIRA_TICKET_REPORTER).setBackground(Color.WHITE);
						sl.getLabel(ShortLogLabel.PART_NUM_32).setBackground(Color.WHITE);
						sl.getLabel(ShortLogLabel.PART_NUM_37).setBackground(Color.WHITE);
						sl.status.setBackground(Color.WHITE);
					}

					int space = 0;

					this.add(sl.getLabel(ShortLogLabel.JIRA_TICKET_DESCRIPTION), Tools.createGBC(1, y, 1.0, new Insets(0,0,-1,0)));
					this.add(sl.getLabel(ShortLogLabel.JIRA_TICKET_REPORTER), Tools.createGBC(2, y, 0.1, new Insets(0,space,-1,0)));
					this.add(sl.getLabel(ShortLogLabel.PART_NUM_32), Tools.createGBC(3, y, 0.1, new Insets(0,space,-1,0)));
					this.add(sl.getLabel(ShortLogLabel.PART_NUM_37), Tools.createGBC(4, y, 0.1, new Insets(0,space,-1,0)));
					this.add(sl.status,  Tools.createGBC(5, y, 0.0, new Insets(0,0,-1,0)));

					y++;
				}

				this.revalidate();
				this.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Sets up a new ShortLog and returns it.
		 * @param f the file to be added.
		 * @return a set-up ShortLog file.
		 * @throws IOException
		 */
		private ShortLog setUpNewShortLog(File f) throws IOException {
			ShortLog toReturn = new ShortLog(Ticket.readLogFile(f.getAbsolutePath()), f.getAbsolutePath());

			toReturn.popupOptions.delete.addActionListener(e -> {					
				try {
					Files.deleteIfExists(new File(toReturn.ticketURL).toPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				addAllToLogEntryPanel();
			});

			toReturn.popupOptions.openH.addActionListener(e -> {
				mw.setAll(
						toReturn.ticket[TicketInfo.TITLE.i], 
						toReturn.ticket[TicketInfo.PART_NUM_32.i],
						toReturn.ticket[TicketInfo.PART_NUM_37.i], 
						toReturn.ticket[TicketInfo.DATE.i], 
						toReturn.ticket[TicketInfo.GUID.i], 
						toReturn.ticket[TicketInfo.PERFORCE_URL.i], 
						toReturn.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i], 
						toReturn.ticket[TicketInfo.REPORT.i],
						toReturn.ticket[TicketInfo.JIRA_TICKET_URL.i], 
						toReturn.ticket[TicketInfo.TCIS_URL.i],
						Integer.parseInt(toReturn.ticket[TicketInfo.STATUS.i]),
						toReturn.ticket[TicketInfo.JIRA_TICKET_KEY.i]);
			});

			return toReturn;
		}
	}
}
