package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import jm.JMPanel;
import jm.JMColor;
import ticket.Ticket;
import ticket.TicketInfo;
import ui.Tools;

/**
 * A log as it is displayed in LogWindow.
 * @author Alexander Porrello
 */
public class ShortLog extends JMPanel implements Comparable<ShortLog> {
	private static final long serialVersionUID = 4949615017530138259L;

	public static final Dimension STATUS_SIZE       = new Dimension(110,  24);
	public static final Dimension CREATED_DATE_SIZE = new Dimension(110, 24);
	public static final Dimension PART_NUM_32_SIZE  = new Dimension(100, 24);
	public static final Dimension PART_NUM_37_SIZE  = new Dimension(100, 24);
	public static final Dimension JIRA_REPORT_SIZE  = new Dimension(130, 24);

	// ====================== PRIVATE FIELDS ====================== //

	/** Used for pulling labels out of {@link #labels} **/
	private int TICKET_DESCRIPTION, DATE_CREATED, TICKET_REPORTER, PART_NUM_32, PART_NUM_37;

	/** Makes it so the user can change a ticket's status without opening the ticket **/
	private ClickLabel<String> status = new ClickLabel<String>(Ticket.STATUS_OPTIONS, SwingConstants.LEFT);

	/** Determines how the log entries are sorted **/
	private Compare compare = Compare.JIRA_TICKET_DESCRIPTION;

	/** The date used when the file is sorted **/
	private String dateForSort;

	/** Determines if the ShortLog is organized front to back or back to front **/
	private boolean invert;

	/** Determines if the ShortLog is open in the system editor **/
	private boolean open;
	
	/** The date that this log file was created **/
	private String logDate;

	/** Set to true if the mouse has been pressed; else, false. **/
	private Boolean mousePressed = false;

	/** Used when user hovers over log **/
	private Color originalColor;

	// ====================== PUBLIC FIELDS ====================== //

	/** Determines if the separation line below the log is drawn **/
	public boolean last = false;
	
	/** Displays the short log's info, such as status, date created, part numbers, and ticket report **/
	public JLabel[] labels = {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};

	/** The context menu that appears when the user left clicks **/
	public LeftClickOptions popupOptions;

	/** All of the fields in the ticket **/
	public String[] ticket;	

	/** The URL of the ticket, for opening purposes **/
	public String ticketURL;

	public ShortLog(String[] ticket, String ticketURL, Compare compare, Boolean invert, Boolean open) {
		this.ticket       = ticket;
		this.ticketURL    = ticketURL;
		this.compare      = compare;
		this.invert       = invert;
		this.open         = open;
		
		popupOptions = new LeftClickOptions(this.ticketURL);

		getLogDates();
		setValues();

		setUpLabel(TICKET_DESCRIPTION, this.ticket[TicketInfo.TICKET_DESCRIPTION.i], null);
		setUpLabel(DATE_CREATED,       logDate,                                      ShortLog.CREATED_DATE_SIZE);
		setUpLabel(PART_NUM_32,        this.ticket[TicketInfo.PART_NUM_32.i],        ShortLog.PART_NUM_32_SIZE);
		setUpLabel(PART_NUM_37,        this.ticket[TicketInfo.PART_NUM_37.i],        ShortLog.PART_NUM_37_SIZE);
		setUpLabel(TICKET_REPORTER,    this.ticket[TicketInfo.REPORT.i],             ShortLog.JIRA_REPORT_SIZE);

		setUpView();
		setupStatus();

		createUserInterface();
	}

	/** Gets the date on which the log file was created. **/
	private void getLogDates() {
		Long lastModified = new File(ticketURL).lastModified();

		this.logDate      = new SimpleDateFormat("M/d/yy h:mm a").format(lastModified);
		this.dateForSort  = new SimpleDateFormat("MM/dd/yy HH:mm:SS").format(lastModified);
	}

	/** Sets the values of the integers that are used to pull info from {@link #labels} **/
	private void setValues() {
		TICKET_DESCRIPTION = 0;
		DATE_CREATED = 1;
		TICKET_REPORTER = 2; 
		PART_NUM_32 = 3;
		PART_NUM_37 = 4;
	}

	Boolean mouseEntered = false;
	Color   drawColor    = getBackground();

	/** 
	 * Sets up a JLabel.
	 * @param label the label in {@link #labels} to be set up. 
	 * @param text the JLabel's displayed text.
	 * @param d the size of the JLabel (should be null for {@link #TICKET_DESCRIPTION})
	 */
	private void setUpLabel(int label, String text, Dimension d) {
		labels[label] = new JLabel(text);
		labels[label].setOpaque(false);
		labels[label].setHorizontalAlignment(SwingConstants.LEFT);

		if(label != TICKET_DESCRIPTION) {
			labels[label].setPreferredSize(d);
			labels[label].setBorder(BorderFactory.createEmptyBorder(2,10,2,2));
		} else {
			labels[label].setBorder(BorderFactory.createEmptyBorder(5,10,5,0));
		}

		labels[label].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				mousePressed = false;

				if(contains(e.getPoint())) {
					drawColor = JMColor.HOVER_COLOR;
					
					if(e.isPopupTrigger()) {
						popupOptions.show(e.getComponent(), e.getX(), e.getY());
					}
					
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = true;
				
				if(e.isPopupTrigger()) {
					popupOptions.show(e.getComponent(), e.getX(), e.getY());
				}
				
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {				
				drawColor = originalColor;
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				originalColor = getBackground();

				if(mousePressed) {
					drawColor = JMColor.PRESS_COLOR;
				} else {
					drawColor = JMColor.HOVER_COLOR;
				}

				repaint();
			}
			
			
		});
	}

	/** Sets up the view JTextArea **/
	private void setUpView() {
		setLayout(new GridBagLayout());
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {				
				if(getWidth() <= 740) {
					setLabelVisible(labels[DATE_CREATED], 740);
					setLabelVisible(labels[TICKET_REPORTER], 630);
					setLabelVisible(status, 500);
					setLabelVisible(labels[PART_NUM_32], 385);
					setLabelVisible(labels[PART_NUM_37], 385);
				}
			}
		});
	}
	
	/**
	 * Sets a given label visible if the width of {@link #view} is greater than 
	 * a given width.
	 * @param label the label to set visible or invisible
	 * @param width the width to determine if label is visible or invisible.
	 */
	private void setLabelVisible(JLabel label, int width) {
		if(getWidth() < width) {
			label.setVisible(false);
		} else {
			label.setVisible(true);
		}
	}

	/** Sets up the status button. **/
	private void setupStatus() {
		status.setSelectedIndex(Integer.parseInt(this.ticket[TicketInfo.STATUS.i]));
		status.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				ticket[TicketInfo.STATUS.i] = status.getSelectedIndex() + "";

				try {
					Ticket.writeChangesToLog(ticket);
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		});
		status.setBorder(BorderFactory.createEmptyBorder(5,10,5,0));
		status.setPreferredSize(ShortLog.STATUS_SIZE);
	}

	/**
	 * Adds all of the JLabels to {@link #view}.
	 * @return {@link #view}
	 */
	public void createUserInterface() {
		int space = 0;
		int y = 0;

		add(labels[TICKET_DESCRIPTION], Tools.createGBC(1, y, 1.0, new Insets(0,0,    0,0)));
		add(labels[DATE_CREATED],       Tools.createGBC(2, y, 0.0, new Insets(0,space,0,0)));
		add(labels[TICKET_REPORTER],    Tools.createGBC(3, y, 0.0, new Insets(0,space,0,0)));
		add(labels[PART_NUM_32],        Tools.createGBC(4, y, 0.0, new Insets(0,space,0,0)));
		add(labels[PART_NUM_37],        Tools.createGBC(5, y, 0.0, new Insets(0,space,0,0)));
		add(status,                     Tools.createGBC(6, y, 0.0, new Insets(0,space,0,space)));
	}

	public void open(Boolean open) {
		this.open = open;
		repaint();
	}
	
	/**
	 * Compares two given strings.
	 * @param oa the first string to be compared.
	 * @param ob the second string to be compared.
	 * @return the result of the comparison.
	 */
	private int compareTo(String oa, String ob) {
		if(!invert) {
			return oa.compareTo(ob);
		} else {
			return ob.compareTo(oa);
		}
	}

	@Override
	public int compareTo(ShortLog e) {
		if(compare == Compare.JIRA_TICKET_DESCRIPTION) {
			return compareTo(ticket[TicketInfo.TICKET_DESCRIPTION.i], e.ticket[TicketInfo.TICKET_DESCRIPTION.i]);
		} else if(compare == Compare.JIRA_REPORT) {
			return compareTo(ticket[TicketInfo.REPORT.i], e.ticket[TicketInfo.REPORT.i]);
		} else if(compare == Compare.PART_NUM_32) {
			return compareTo(ticket[TicketInfo.PART_NUM_32.i], e.ticket[TicketInfo.PART_NUM_32.i]);
		} else if(compare == Compare.PART_NUM_37){
			return compareTo(ticket[TicketInfo.PART_NUM_37.i], e.ticket[TicketInfo.PART_NUM_37.i]);
		} else if(compare == Compare.DATE_CREATED) {
			return compareTo(dateForSort, e.dateForSort);
		} else {
			return compareTo(ticket[TicketInfo.STATUS.i], e.ticket[TicketInfo.STATUS.i]);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(!open) {
			g.setColor(drawColor);
		} else {
			g.setColor(JMColor.PRESS_COLOR);
		}
		
		g.fillRect(0, 0, getWidth()-5, getHeight());
		
		
		if(!last) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, getHeight()-1, getWidth()-5, getHeight());
		}
	}
}
