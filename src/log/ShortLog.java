package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ticket.Ticket;
import ticket.TicketInfo;
import ui.JMPanel;
import ui.Tools;

public class ShortLog implements Comparable<ShortLog> {

	public static final Dimension STATUS_SIZE = new Dimension(110,24);
	public static final Dimension CREATED_DATE_SIZE = new Dimension(110, 24);
	public static final Dimension PART_NUM_32_SIZE  = new Dimension(100, 24);
	public static final Dimension PART_NUM_37_SIZE  = new Dimension(100, 24);
	public static final Dimension JIRA_REPORT_SIZE = new Dimension(130, 24);

	public Compare compare = Compare.JIRA_TICKET_DESCRIPTION;

	/** Makes it so the user can change a ticket's status without opening the ticket **/
	public ClickLabel<String> status = new ClickLabel<String>(Ticket.STATUS_OPTIONS, SwingConstants.LEFT);

	/** All of the fields in the ticket **/
	public String[] ticket;	

	public JLabel[] labels = {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};

	/** The URL of the ticket, for opening purposes **/
	public String ticketURL;

	/** The context menu that appears when the user left clicks **/
	public LeftClickOptions popupOptions;

	/** Determines if the ShortLog is organized front to back or back to front **/
	public boolean invert;

	/** The date that this log file was created **/
	public String logDate;

	JMPanel view = new JMPanel();

	Color originalColor;

	Boolean mousePressed = false;
	
	public ShortLog(String[] ticket, String ticketURL, Compare compare, Boolean invert) {
		this.invert       = invert;
		this.ticket       = ticket;
		this.compare      = compare;
		this.ticketURL    = ticketURL;
		this.popupOptions = new LeftClickOptions(this.ticketURL);		
		this.logDate      = new SimpleDateFormat("M/d/yy HH:mm").format(new File(ticketURL).lastModified());

		status.setPreferredSize(ShortLog.STATUS_SIZE);

		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i] = new JLabel(this.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);

		labels[ShortLogLabel.CREATED_DATE.i] = new JLabel(logDate);
		labels[ShortLogLabel.CREATED_DATE.i].setPreferredSize(ShortLog.CREATED_DATE_SIZE);

		labels[ShortLogLabel.PART_NUM_32.i] = new JLabel(this.ticket[TicketInfo.PART_NUM_32.i]);
		labels[ShortLogLabel.PART_NUM_32.i].setPreferredSize(ShortLog.PART_NUM_32_SIZE);

		labels[ShortLogLabel.PART_NUM_37.i] = new JLabel(this.ticket[TicketInfo.PART_NUM_37.i]);
		labels[ShortLogLabel.PART_NUM_37.i].setPreferredSize(ShortLog.PART_NUM_37_SIZE);

		labels[ShortLogLabel.JIRA_TICKET_REPORTER.i] = new JLabel(this.ticket[TicketInfo.REPORT.i]);
		labels[ShortLogLabel.JIRA_TICKET_REPORTER.i].setPreferredSize(ShortLog.JIRA_REPORT_SIZE);

		for(JLabel label : labels) {
			label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					mousePressed = false;
					
					if(view.contains(arg0.getPoint())) {
						view.setBackground(Color.decode("#D9EBF9"));
					}
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					mousePressed = true;
					view.setBackground(Color.decode("#BCDCF4"));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					view.setBackground(originalColor);
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					originalColor = view.getBackground();

					if(!mousePressed) {
						view.setBackground(Color.decode("#D9EBF9"));
					} else {
						view.setBackground(Color.decode("#BCDCF4"));
					}
				}
			});
		}

		setUpOpenTicket();
		setupStatus();
	}

	private void setUpOpenTicket() {
		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i].addMouseListener(new MouseAdapter() {			
			@Override
			public void mousePressed(MouseEvent e){
				if(e.isPopupTrigger()) {
					popupOptions.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger()) {
					popupOptions.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i].setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
		//labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i].setOpaque(true);
	}

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
		status.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
	}

	public JLabel getLabel(ShortLogLabel sll) {
		return labels[sll.i];
	}

	public void setBackgroundWhite() {
		//		getLabel(ShortLogLabel.JIRA_TICKET_DESCRIPTION).setBackground(Color.WHITE);
		//		getLabel(ShortLogLabel.CREATED_DATE).setBackground(Color.WHITE);
		//		getLabel(ShortLogLabel.JIRA_TICKET_REPORTER).setBackground(Color.WHITE);
		//		getLabel(ShortLogLabel.PART_NUM_32).setBackground(Color.WHITE);
		//		getLabel(ShortLogLabel.PART_NUM_37).setBackground(Color.WHITE);
		//		status.setBackground(Color.WHITE);

		view.setBackground(Color.WHITE);
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

	public JMPanel createView() {
		view.setLayout(new GridBagLayout());

		int space = 0;
		int y = 0;

		view.add(getLabel(ShortLogLabel.JIRA_TICKET_DESCRIPTION), Tools.createGBC(1, y, 1.0, new Insets(0,0,-1,0)));
		view.add(getLabel(ShortLogLabel.CREATED_DATE),            Tools.createGBC(2, y, 0.0, new Insets(0,space,-1,0)));
		view.add(getLabel(ShortLogLabel.JIRA_TICKET_REPORTER),    Tools.createGBC(3, y, 0.0, new Insets(0,space,-1,0)));
		view.add(getLabel(ShortLogLabel.PART_NUM_32),             Tools.createGBC(4, y, 0.0, new Insets(0,space,-1,0)));
		view.add(getLabel(ShortLogLabel.PART_NUM_37),             Tools.createGBC(5, y, 0.0, new Insets(0,space,-1,0)));
		view.add(status,                                          Tools.createGBC(6, y, 0.0, new Insets(0,space,-1,space)));

		return view;
	}

	@Override
	public int compareTo(ShortLog e) {
		if(compare == Compare.JIRA_TICKET_DESCRIPTION) {
			return compareTo(ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i], e.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		} else if(compare == Compare.JIRA_REPORT) {
			return compareTo(ticket[TicketInfo.REPORT.i], e.ticket[TicketInfo.REPORT.i]);
		} else if(compare == Compare.PART_NUM_32) {
			return compareTo(ticket[TicketInfo.PART_NUM_32.i], e.ticket[TicketInfo.PART_NUM_32.i]);
		} else if(compare == Compare.PART_NUM_37){
			return compareTo(ticket[TicketInfo.PART_NUM_37.i], e.ticket[TicketInfo.PART_NUM_37.i]);
		} else if(compare == Compare.DATE_CREATED) {
			return compareTo(logDate, e.logDate);
		} else {
			return compareTo(ticket[TicketInfo.STATUS.i], e.ticket[TicketInfo.STATUS.i]);
		}
	}
}
