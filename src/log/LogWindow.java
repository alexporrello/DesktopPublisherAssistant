package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import ui.MainWindow;
import ui.JMPanel;
import ui.Tools;

/**
 * Displays all of the logs created by the Desktop publisher and their respective statuses.
 * @author Alexander Porrello
 */
public class LogWindow extends JMPanel {
	private static final long serialVersionUID = -1014191579466760962L;

	private final int TICKET_DESCRIPTION = 0;
	private final int DATE_CREATED = 1;
	private final int JIRA_REPORT = 2;
	private final int PART_NUM_32 = 3;
	private final int PART_NUM_37 = 4;
	private final int STATUS = 5;
	private final int SPACER = 6;

	/** All of the ticket files currently in existence on the user's computer **/
	private ArrayList<ShortLog> shortLogs = new ArrayList<ShortLog>();

	/** Determines how the tickets are sorted in {@link #logWindow} **/
	private Compare compare = Compare.JIRA_TICKET_DESCRIPTION;

	/** Determines if the tickets are sorted from back to front or front to back **/
	private Boolean invert = false;

	/** The labels that the user clicks to sort the tickets **/
	private JLabel[] infoLabels = {
			new JLabelEdge("Jira Ticket Description"),
			new JLabelEdge("Date Created"),
			new JLabelEdge("Jira Report"),
			new JLabelEdge("32 Part Number"),
			new JLabelEdge("37 Part Number"),
			new JLabelEdge("Status", false),
			new JLabelEdge("", false)
	};

	/** The JScrollPane that holds {@link #logWindow} **/
	private JScrollPane logDialogScroll;

	/** The panel upon which all the logs are displayed **/
	private LogPanel logWindow = new LogPanel();

	/** The program body's main window **/
	private MainWindow mw;

	public LogWindow(MainWindow mw) {
		this.mw = mw;

		setupScrollPane();
		setupAllLabels();

		setLayout(new GridBagLayout());
		setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(-1,5,5,5),
						BorderFactory.createCompoundBorder(
								BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
								BorderFactory.createEmptyBorder(5, 5, 5, 5))));

		add(createInfoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, 
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(logDialogScroll,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, 
				GridBagConstraints.BOTH,       new Insets(0, 0, 0, 0), 0, 0));
	}

	/** Sets up the JScrollPane that displays all the log entries  **/
	private void setupScrollPane() {
		logDialogScroll = new JScrollPane(logWindow);

		logDialogScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logDialogScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logDialogScroll.setBorder(BorderFactory.createEmptyBorder());
		logDialogScroll.setPreferredSize(new Dimension(900,150));
	}

	/** Sets up the JLabels that sort the displayed log entries **/
	private void setupAllLabels() {
		setupJLabel(DATE_CREATED, ShortLog.CREATED_DATE_SIZE, Compare.DATE_CREATED);
		setupJLabel(JIRA_REPORT,  ShortLog.JIRA_REPORT_SIZE,  Compare.JIRA_REPORT);
		setupJLabel(PART_NUM_32,  ShortLog.PART_NUM_32_SIZE,  Compare.PART_NUM_32);
		setupJLabel(PART_NUM_37,  ShortLog.PART_NUM_37_SIZE,  Compare.PART_NUM_37);
		setupJLabel(STATUS,       ShortLog.STATUS_SIZE,       Compare.STATUS);
		setupJLabel(SPACER,       new Dimension(25, 24),      Compare.NULL);

		infoLabels[TICKET_DESCRIPTION].setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		addActionListenerToJLabel(TICKET_DESCRIPTION, Compare.JIRA_TICKET_DESCRIPTION);
	}

	/** Sets up individual JLabels that the user clicks to sort the log entries **/
	private void setupJLabel(int label, Dimension size, Compare thisCompare) {
		infoLabels[label].setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
		infoLabels[label].setPreferredSize(size);
		addActionListenerToJLabel(label, thisCompare);
	}

	private void addActionListenerToJLabel(int label, Compare thisCompare) {
		infoLabels[label].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				infoLabels[label].setBackground(Color.decode("#D9EBF9"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				infoLabels[label].setBackground(new JLabel().getBackground());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				infoLabels[label].setBackground(Color.decode("#BCDCF4"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				infoLabels[label].setBackground(new JLabel().getBackground());

				if(infoLabels[label].contains(e.getPoint()) && compare != Compare.NULL) {
					if(compare == thisCompare) {
						invert = !invert;
					} else {
						compare = thisCompare;
						invert = false;
					}

					logWindow.addAllToLogEntryPanel();
				}
			}
		});
	}

	/** Sets up the info panel that displays the JLabels that the user clicks to sort log entries **/
	private JMPanel createInfoPanel() {
		JMPanel infoPanel = new JMPanel();

		infoPanel.setLayout(new GridBagLayout());
		infoPanel.add(infoLabels[TICKET_DESCRIPTION], Tools.createGBC(1, 0, 1.0, new Insets(0,0,-1,0)));

		for(int i = 1; i <= 6; i++) {
			infoPanel.add(infoLabels[i], Tools.createGBC(i+1, 0, 0.0, new Insets(0,0,-1,0)));
		}

		return infoPanel;
	}

	/**
	 * The panel upon which all the log entries are displayed.
	 * @author Alexander Porrello
	 */
	private class LogPanel extends JMPanel {
		private static final long serialVersionUID = 8218069729268454703L;

		LogPanel() {
			setLayout(new GridBagLayout());
			addAllToLogEntryPanel();
			watchService();
		}

		/**
		 * Watches the Desktop Publisher Assistant for changes and updates the log
		 * if any occur.
		 */
		private void watchService() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						WatchService watcher = FileSystems.getDefault().newWatchService();
						Path dir = Ticket.TICKET_URL.toPath();

						dir.register(watcher, ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE);

						for(;;) {
							WatchKey key = watcher.take();
							for (WatchEvent<?> event : key.pollEvents()) {
								if (event.kind() == ENTRY_CREATE || event.kind() == ENTRY_DELETE || event.kind() == ENTRY_MODIFY) {
									addAllToLogEntryPanel();
									continue;
								}
							}

							key.reset();
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
		private void addAllToLogEntryPanel() {
			this.removeAll();
			shortLogs.clear();

			try {
				for(File f : Ticket.TICKET_URL.listFiles()) {
					shortLogs.add(setUpNewShortLog(f));
				}
				Collections.sort(shortLogs);

				int y = 0;

				for(ShortLog sl : shortLogs) {
					if(y%2 == 0) {
						sl.setBackgroundWhite();
					}

					this.add(sl.createView(), Tools.createGBC(1, y++, 1.0, new Insets(0,0,-1,0)));
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
			ShortLog toReturn = new ShortLog(Ticket.readLogFile(f.getAbsolutePath()), f.getAbsolutePath(), compare, invert);

			toReturn.popupOptions.openH.addActionListener(e -> mw.setLog(toReturn.ticket));
			toReturn.popupOptions.delete.addActionListener(e -> {					
				try {
					Files.deleteIfExists(new File(toReturn.ticketURL).toPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			for(JLabel label : toReturn.labels) {
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if(toReturn.view.contains(e.getPoint())) {
							mw.setLog(toReturn.ticket);
						}
					}
				});
			}
			
			return toReturn;
		}
	}

	/**
	 * JLabel with a line drawn to the right.
	 * @author Alexander Porrello
	 */
	private class JLabelEdge extends JLabel {
		private static final long serialVersionUID = -2564660927510249775L;

		private Boolean show = true;

		JLabelEdge(String s) { 
			super(s);

			setHorizontalAlignment(SwingConstants.LEFT);
			setOpaque(true);
		}

		JLabelEdge(String s, Boolean show) { 
			super(s);
			this.show = show;

			setHorizontalAlignment(SwingConstants.LEFT);
			setOpaque(true);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if(show) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
			}
		}
	}
}
