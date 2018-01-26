package log;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ticket.Ticket;
import ticket.TicketInfo;

public class ShortLog implements Comparable<ShortLog> {
	
	/** Determines whether ShortLog is compared by status or the Jira ticket description **/
	private boolean sortByStatus = true;

	/** Makes it so the user can change a ticket's status without opening the ticket **/
	public ClickLabel<String> status = new ClickLabel<String>(Ticket.STATUS_OPTIONS, SwingConstants.LEFT);

	/** All of the fields in the ticket **/
	public String[] ticket;	

	private JLabel[] labels = {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	
	/** The URL of the ticket, for opening purposes **/
	public String ticketURL;

	/** The context menu that appears when the user left clicks **/
	public LeftClickOptions popupOptions;
	
	public ShortLog(String[] ticket, String ticketURL) {
		this.ticket    = ticket;
		this.ticketURL = ticketURL;
		this.popupOptions = new LeftClickOptions(this.ticketURL);
		
		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i]  = new JLabel(this.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		labels[ShortLogLabel.JIRA_TICKET_REPORTER.i]     = new JLabel(this.ticket[TicketInfo.REPORT.i]);
		labels[ShortLogLabel.PART_NUM_32.i]              = new JLabel(this.ticket[TicketInfo.PART_NUM_32.i]);
		labels[ShortLogLabel.PART_NUM_37.i]              = new JLabel(this.ticket[TicketInfo.PART_NUM_37.i]);
				
		for(JLabel label : labels) {
			label.setOpaque(true);
			label.setBorder(BorderFactory.createEmptyBorder(2,10,2,10));
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
		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i].setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i].setOpaque(true);
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
		status.setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
	}

	public JLabel getLabel(ShortLogLabel sll) {
		return labels[sll.i];
	}
	
	/**
	 * Sets the log sortable by its status.
	 * @param sbs true if the log is sortable by status; else, false.
	 */
	public void sortByStatus(Boolean sbs) {
		this.sortByStatus = sbs;
	}

	/**
	 * Sets the log sortable by its Jira ticket number.
	 * @param sbs true if the log is sortable by its Jira ticket number; else, false.
	 */
	public void sortByTicket(Boolean sbs) {
		this.sortByStatus = !sbs;
	}

	@Override
	public int compareTo(ShortLog e) {
		if(sortByStatus) {
			return ticket[TicketInfo.STATUS.i].compareTo(e.ticket[TicketInfo.STATUS.i]);
		} else {
			return ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i].compareTo(e.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		}
	}
}
