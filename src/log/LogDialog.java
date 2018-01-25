package log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ticket.Ticket;
import ticket.TicketInfo;
import ui.DesktopPublisherAssistant.MainWindow;
import ui.Tools;

/**
 * Displays all of the logs created by the Desktop publisher and their respective statuses.
 * @author Alexander Porrello
 */
public class LogDialog extends JDialog {
	private static final long serialVersionUID = -1014191579466760962L;

	/** For displaying all of the log's entries **/
	private JPanel logEntryPanel = new JPanel();

	/** All of the ticket files currently in existence on the user's computer **/
	ArrayList<ShortLog> shortLogs = new ArrayList<ShortLog>();

	private MainWindow mw;

	public LogDialog(MainWindow mw) {
		this.mw = mw;

		logEntryPanel.setLayout(new GridBagLayout());

		addAllToLogEntryPanel();

		JScrollPane scroll = new JScrollPane(logEntryPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new JLabel().getBorder());
		add(scroll, BorderLayout.CENTER);

		setIconImages(Tools.imageIcon());
		setSize(new Dimension(500, 200));
		setLocationByPlatform(true);
		setTitle("Log");
	}

	/** Locates all tickets, adds them to {@link #shortLogs}, and adds them to {@link #logEntryPanel}. **/
	public void addAllToLogEntryPanel() {
		logEntryPanel.removeAll();
		shortLogs.clear();
		
		try {
			for(File f : Ticket.TICKET_URL.listFiles()) {
				ShortLog sl = new ShortLog(Ticket.readLogFile(f.getAbsolutePath()), f.getAbsolutePath());
				
				sl.popupOptions.delete.addActionListener(e -> {					
					try {
						Files.deleteIfExists(new File(sl.ticketURL).toPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					addAllToLogEntryPanel();
				});
				sl.popupOptions.openH.addActionListener(e -> {
					mw.setAll(sl.ticket[TicketInfo.TITLE.i], 
							sl.ticket[TicketInfo.PART_NUM_32.i], sl.ticket[TicketInfo.PART_NUM_37.i], 
							sl.ticket[TicketInfo.DATE.i], 
							sl.ticket[TicketInfo.GUID.i], 
							sl.ticket[TicketInfo.PERFORCE_URL.i], 
							sl.ticket[TicketInfo.JIRA_TICKET_DESCRIPTION.i], sl.ticket[TicketInfo.REPORT.i], sl.ticket[TicketInfo.JIRA_TICKET_URL.i], 
							sl.ticket[TicketInfo.TCIS_URL.i], 
							Integer.parseInt(sl.ticket[TicketInfo.STATUS.i]));
				});

				shortLogs.add(sl);
			}
			Collections.sort(shortLogs);

			int y = 0;

			for(ShortLog sl : shortLogs) {				
				if(y%2 == 0) {
					sl.openTicket.setBackground(Color.WHITE);
					sl.status.setBackground(Color.WHITE);
				}

				logEntryPanel.add(sl.openTicket, Tools.createGBC(1, y, 1.0, new Insets(0,0,-1,0)));
				logEntryPanel.add(sl.status,     Tools.createGBC(2, y, 0.0, new Insets(0,0,-1,0)));

				y++;
			}
			
			logEntryPanel.revalidate();
			logEntryPanel.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
