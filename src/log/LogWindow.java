package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.SwingUtilities;

import jm.JMPanel;
import jm.JMScrollPane;
import jtext.JTextClose;
import jm.JMColor;

import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import ticket.Ticket;
import ticket.TicketInfo;
import ui.MainWindow;
import ui.Tools;

/**
 * Displays all of the logs created by the Desktop publisher and their respective statuses.
 * @author Alexander Porrello
 */
public class LogWindow extends JMPanel {
	private static final long serialVersionUID = -1014191579466760962L;

	/** All of the ticket files currently in existence on the user's computer **/
	private ArrayList<ShortLog> shortLogs = new ArrayList<ShortLog>();

	/** The program body's main window **/
	private MainWindow mw;

	/** Determines how the tickets are sorted in {@link #logWindow} **/
	private Compare compare = Compare.JIRA_TICKET_DESCRIPTION;

	/** Determines if the tickets are sorted from back to front or front to back **/
	private Boolean invert = false;

	/** Determines if completed tickets should be displayed **/
	private Boolean hideFinishedTickets = true;//TODO

	/** The JScrollPane that holds {@link #logWindow} **/
	private JMScrollPane logDialogScroll;

	/** The panel upon which all the logs are displayed **/
	private LogPanel logWindow;

	/** The JTextFields with which the user searches through ticket information **/
	public JTextClose search = new JTextClose("");
	
	public LogWindow(MainWindow mw) {
		this.mw = mw;

		this.logWindow = new LogPanel();

		setBackground(JMColor.DEFAULT_BACKGROUND);
		setupScrollPane();
		setupSearch();
		setLayout(new GridBagLayout());

		add(new HeadingPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, 
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		add(logDialogScroll,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, 
				GridBagConstraints.BOTH,       new Insets(0, 0, 0, 0), 0, 0));
		add(search,             new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, 
				GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 15), 0, 0));
	}

	/** Sets up the JScrollPane that displays all the log entries  **/
	private void setupScrollPane() {
		logDialogScroll = new JMScrollPane(logWindow);
		logDialogScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logDialogScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logDialogScroll.setBorder(BorderFactory.createEmptyBorder());
		logDialogScroll.setBackground(Color.WHITE);

		logDialogScroll.setPreferredSize(new Dimension(900,150));
	}

	/** Sets up the search functionality **/
	private void setupSearch() {
		search.setVisible(false);
		search.setForeground(JMColor.DEFAULT_FONT_COLOR);
		search.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				logWindow.addToLogEntryPanel();
			}
		});
		search.getTextField().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				logWindow.addToLogEntryPanel();
			}
		});
	}

	/**
	 * The panel upon which all of the headings are displayed.
	 * @author Alexander Porrello
	 */
	private class HeadingPanel extends JMPanel {
		private static final long serialVersionUID = -1040672906315782425L;

		/** The labels that the user clicks to sort the tickets **/
		private JLabel[] headings = {
				new JLabelEdge("Jira Ticket Description"),
				new JLabelEdge("Date Created"),
				new JLabelEdge("Jira Report"),
				new JLabelEdge("32 Part Number"),
				new JLabelEdge("37 Part Number"),
				new JLabelEdge("Status", false),
				new JLabelEdge("", false)
		};

		public HeadingPanel() {
			setLayout(new GridBagLayout());

			setOpaque(false);
			setupAllLabels();

			add(headings[HF.TICKET_DESCRIPTION], Tools.createGBC(1, 0, 1.0, new Insets(0,0,-1,0)));
			for(int i = 1; i <= 6; i++) {
				add(headings[i], Tools.createGBC(i+1, 0, 0.0, new Insets(0,0,-1,0)));
			}
		}

		/** Sets up the JLabels that sort the displayed log entries **/
		private void setupAllLabels() {
			setupJLabel(HF.DATE_CREATED, ShortLog.CREATED_DATE_SIZE, Compare.DATE_CREATED);
			setupJLabel(HF.JIRA_REPORT,  ShortLog.JIRA_REPORT_SIZE,  Compare.JIRA_REPORT);
			setupJLabel(HF.PART_NUM_32,  ShortLog.PART_NUM_32_SIZE,  Compare.PART_NUM_32);
			setupJLabel(HF.PART_NUM_37,  ShortLog.PART_NUM_37_SIZE,  Compare.PART_NUM_37);
			setupJLabel(HF.STATUS,       ShortLog.STATUS_SIZE,       Compare.STATUS);
			setupJLabel(HF.SPACER,       new Dimension(Tools.SCROLL_BAR_WIDTH, 24),      Compare.NULL);

			headings[HF.TICKET_DESCRIPTION].setForeground(JMColor.DEFAULT_FONT_COLOR);
			headings[HF.TICKET_DESCRIPTION].setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
			addActionListenerToJLabel(HF.TICKET_DESCRIPTION, Compare.JIRA_TICKET_DESCRIPTION);
		}

		/** Sets up individual JLabels that the user clicks to sort the log entries **/
		private void setupJLabel(int label, Dimension size, Compare thisCompare) {
			headings[label].setForeground(JMColor.DEFAULT_FONT_COLOR);
			headings[label].setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
			headings[label].setPreferredSize(size);
			addActionListenerToJLabel(label, thisCompare);
		}

		/**
		 * Implements highlighting with hover and click and sorting feature.
		 * @param label the label to be processed.
		 * @param thisCompare sets the comparison method.
		 */
		private void addActionListenerToJLabel(int label, Compare thisCompare) {
			headings[label].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if(headings[label].contains(e.getPoint()) && compare != Compare.NULL) {
						if(compare == thisCompare) {
							invert = !invert;
						} else {
							compare = thisCompare;
							invert = false;
						}

						logWindow.addToLogEntryPanel();
					}
				}
			});
		}

		private class HF {
			public static final int TICKET_DESCRIPTION = 0;
			public static final int DATE_CREATED = 1;
			public static final int JIRA_REPORT = 2;
			public static final int PART_NUM_32 = 3;
			public static final int PART_NUM_37 = 4;
			public static final int STATUS = 5;
			public static final int SPACER = 6;
		}
	}
	
	//TODO
	public void hideFinishedTickets(Boolean hide) {
		this.hideFinishedTickets = hide;
		
		logWindow.addAllTicketsToLogEntryPanel();
	}

	/**
	 * The panel upon which all the log entries are displayed.
	 * @author Alexander Porrello
	 */
	private class LogPanel extends JMPanel {
		private static final long serialVersionUID = 8218069729268454703L;

		LogPanel() {
			setOpaque(false);
			setLayout(new GridBagLayout());
			addToLogEntryPanel();
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
									addToLogEntryPanel();
									mw.updateAuthorHashSet();
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

		public void addToLogEntryPanel() {
			if(!search.isVisible()) {
				addAllTicketsToLogEntryPanel();
			} else {
				addSearchQueryToEntryPanel(search.getText());
			}
		}

		private void addSearchQueryToEntryPanel(String query) {
			this.removeAll();
			shortLogs.clear();

			try {
				ArrayList<File> tickets = Ticket.searchThroughTickets(query);

				for(File f : tickets) {
					shortLogs.add(setUpNewShortLog(f));
				}
				Collections.sort(shortLogs);

				int y = 0;

				for(ShortLog sl : shortLogs) {
					sl.last = (y == tickets.size()-1);
					this.add(sl, Tools.createGBC(1, y++, 1.0, new Insets(0,0,0,0)));
				}

				this.revalidate();
				this.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Locates all tickets, adds them to {@link #shortLogs}, and adds them to {@link #logEntryPanel}.
		 */
		private void addAllTicketsToLogEntryPanel() {
			this.removeAll();
			shortLogs.clear();

			try {
				File[] tickets = Ticket.TICKET_URL.listFiles();		

				for(File f : tickets) {
					shortLogs.add(setUpNewShortLog(f));
				}
				Collections.sort(shortLogs);

				int y = 0;

				for(ShortLog sl : shortLogs) {
					sl.last = (y == tickets.length-1);
					
					if(hideFinishedTickets) {
						if(sl.getStatus() != Ticket.STATUS_OPTIONS.length-1) {
							this.add(sl, Tools.createGBC(1, y++, 1.0, new Insets(0,0,0,0)));
						}
					} else {
						this.add(sl, Tools.createGBC(1, y++, 1.0, new Insets(0,0,0,0)));
					}
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
			String[] log = Ticket.readLogFile(f.getAbsolutePath());
			Boolean  isn = mw.isTicketOpen(log[TicketInfo.TICKET_DESCRIPTION.i]);

			ShortLog toReturn = new ShortLog(log, f.getAbsolutePath(), compare, invert, isn);

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
						if(SwingUtilities.isLeftMouseButton(e) && toReturn.contains(e.getPoint())) {
							mw.setLog(toReturn.ticket);

							for(ShortLog sl : shortLogs) {
								sl.open(mw.isTicketOpen(sl.ticket[TicketInfo.TICKET_DESCRIPTION.i]));
							}
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

		boolean mousePressed;

		Color drawColor = JMColor.DEFAULT_BACKGROUND;

		Color originalColor = JMColor.DEFAULT_BACKGROUND;

		JLabelEdge(String s) { 
			super(s);

			setHorizontalAlignment(SwingConstants.LEFT);
			addActionListener();
		}

		JLabelEdge(String s, Boolean show) { 
			super(s);
			this.show = show;

			setHorizontalAlignment(SwingConstants.LEFT);
			addActionListener();
		}

		private void addActionListener() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					mousePressed = false;

					if(contains(arg0.getPoint())) {
						drawColor = JMColor.HOVER_COLOR;
						repaint();
					}
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					mousePressed = true;

					if(contains(arg0.getPoint())) {
						drawColor = JMColor.PRESS_COLOR;
						repaint();
					}
				}

				@Override
				public void mouseExited(MouseEvent arg0) {				
					drawColor = originalColor;
					repaint();
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					if(mousePressed) {
						drawColor = JMColor.PRESS_COLOR;
					} else {
						drawColor = JMColor.HOVER_COLOR;
					}

					repaint();
				}
			});
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(drawColor);
			g.fillRect(0, 4, getWidth(), getHeight()-8);

			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, getHeight()-1, getWidth(), getHeight());

			if(show) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(getWidth()-1, 4, getWidth()-1, getHeight()-5);
			}
			super.paintComponent(g);

		}
	}
}
