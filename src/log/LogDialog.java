package log;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ticket.Ticket;
import ui.Tools;

/**
 * Displays all of the logs created by the Desktop publisher and their respective statuses.
 * @author Alexander Porrello
 */
public class LogDialog extends JDialog {
	private static final long serialVersionUID = -1014191579466760962L;

	/** For displaying all of the log's entries **/
	private JPanel logEntryPanel = new JPanel();

	ArrayList<ShortLog> shortLogs = new ArrayList<ShortLog>();
	
	public LogDialog() {
		addAllToLogEntryPanel();

		JScrollPane scroll = new JScrollPane(logEntryPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new JLabel().getBorder());
		add(scroll);

		setIconImages(Tools.imageIcon());
		setSize(new Dimension(500, 200));
		setLocationByPlatform(true);
		setTitle("Log");
	}

	public void addAllToLogEntryPanel() {		
		try {
			
			logEntryPanel.setLayout(new GridBagLayout());

			for(File f : Ticket.TICKET_URL.listFiles()) {
				shortLogs.add(new ShortLog(Ticket.readLogFile(f.getAbsolutePath()), f.getAbsolutePath()));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
