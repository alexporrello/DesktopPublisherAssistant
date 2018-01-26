package log;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ticket.Ticket;
import ticket.TicketInfo;

public class ShortLog implements Comparable<ShortLog> {
	
	public Compare compare = Compare.JIRA_TICKET_DESCRIPTION;

	/** Makes it so the user can change a ticket's status without opening the ticket **/
	public ClickLabel<String> status = new ClickLabel<String>(Ticket.STATUS_OPTIONS, SwingConstants.LEFT);

	/** All of the fields in the ticket **/
	public String[] ticket;	

	private JLabel[] labels = {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	
	/** The URL of the ticket, for opening purposes **/
	public String ticketURL;

	/** The context menu that appears when the user left clicks **/
	public LeftClickOptions popupOptions;
	
	public ShortLog(String[] ticket, String ticketURL, Compare compare) {
		this.ticket    = ticket;
		this.compare   = compare;
		this.ticketURL = ticketURL;
		this.popupOptions = new LeftClickOptions(this.ticketURL);
		
		status.setPreferredSize(new Dimension(110,24));

		labels[ShortLogLabel.JIRA_TICKET_DESCRIPTION.i] = new JLabel(this.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		
		labels[ShortLogLabel.PART_NUM_32.i] = new JLabel(this.ticket[TicketInfo.PART_NUM_32.i]);
		labels[ShortLogLabel.PART_NUM_32.i].setPreferredSize(new Dimension(100, 24));
		
		labels[ShortLogLabel.PART_NUM_37.i] = new JLabel(this.ticket[TicketInfo.PART_NUM_37.i]);
		labels[ShortLogLabel.PART_NUM_37.i].setPreferredSize(new Dimension(100, 24));

		labels[ShortLogLabel.JIRA_TICKET_REPORTER.i] = new JLabel(this.ticket[TicketInfo.REPORT.i]);
		labels[ShortLogLabel.JIRA_TICKET_REPORTER.i].setPreferredSize(new Dimension(130, 24));
		
		
		for(JLabel label : labels) {
			label.setOpaque(true);
			label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					System.out.println(label.getSize());
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
		status.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
	}

	public JLabel getLabel(ShortLogLabel sll) {
		return labels[sll.i];
	}

	@Override
	public int compareTo(ShortLog e) {
		if(compare == Compare.JIRA_TICKET_DESCRIPTION) {
			return ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i].compareTo(e.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		} else if(compare == Compare.JIRA_REPORT) {
			return ticket[TicketInfo.REPORT.i].compareTo(e.ticket[TicketInfo.REPORT.i]);
		} else if(compare == Compare.PART_NUM_32) {
			return ticket[TicketInfo.PART_NUM_32.i].compareTo(e.ticket[TicketInfo.PART_NUM_32.i]);
		} else if(compare == Compare.PART_NUM_37){
			return ticket[TicketInfo.PART_NUM_37.i].compareTo(e.ticket[TicketInfo.PART_NUM_37.i]);
		} else {
			return ticket[TicketInfo.STATUS.i].compareTo(e.ticket[TicketInfo.STATUS.i]);
		}
	}
}
