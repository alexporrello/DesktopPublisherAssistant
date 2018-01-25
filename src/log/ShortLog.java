package log;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import ticket.Ticket;
import ticket.TicketInfo;
import ui.DesktopPublisherAssistant;

public class ShortLog implements Comparable<ShortLog> {

	/** Determines whether ShortLog is compared by status or the Jira ticket description **/
	private boolean sortByStatus = true;

	/** Makes it so the user can change a ticket's status without opening the ticket **/
	public ClickLabel<String> status = new ClickLabel<String>(Ticket.STATUS_OPTIONS, SwingConstants.RIGHT);

	/** Opens the ticket in a new Desktop Publisher Assistant window **/
	public JLabel openTicket;

	/** All of the fields in the ticket **/
	public String[] ticket;

	/** The URL of the ticket, for opening purposes **/
	public String ticketURL;

	public LeftClickOptions popupOptions;
	
	public ShortLog(String[] ticket2, String ticketURL) {
		this.ticket    = ticket2;
		this.ticketURL = ticketURL;
		this.popupOptions = new LeftClickOptions(this.ticketURL);
		
		setupOpenTicket();
		setupStatus();
	}

	private void setupOpenTicket() {
		openTicket = new JLabel(this.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		openTicket.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(SwingUtilities.isLeftMouseButton(arg0)) {
					new DesktopPublisherAssistant(ticketURL);
				}
			}

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
		openTicket.setBorder(BorderFactory.createEmptyBorder(5,10,5,5));
		openTicket.setOpaque(true);
	}
	
	public void setupStatus() {
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

	class LeftClickOptions extends JPopupMenu {
		private static final long serialVersionUID = -4929695537157281490L;

		public JMenuItem openH  = new JMenuItem("Open in Current Window");
		public JMenuItem open   = new JMenuItem("Open in New Window");
		public JMenuItem delete = new JMenuItem("Delete Ticket");

		public LeftClickOptions(String ticketURL){
			add(setupMenuItem(openH));
			open.addActionListener(e -> {
				new DesktopPublisherAssistant(ticketURL);
			});
			add(setupMenuItem(open));

			addSeparator();

			add(setupMenuItem(delete));
		}
		
		public JMenuItem setupMenuItem(JMenuItem tosetup) {
			tosetup.setBorder(BorderFactory.createEmptyBorder(-4,-5,-2,-5));
			
			return tosetup;
		}
	}
}
