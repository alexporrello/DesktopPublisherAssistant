package ticket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ui.Tools;
import ui.DesktopPublisherAssistant.MainWindow;

public class Ticket {

	/** The possible status of the current Jira ticket **/
	public static final String[] STATUS_OPTIONS = {"Not Started", "Started Progress", "Sent for Approval", "Done"};
	
	/** The URL of the location where all of the logs are stored **/
	public static final File TICKET_URL = new File(System.getenv("APPDATA") + "\\Desktop Publisher Assistant");

	/** If the URL does not exist, then make it **/
	public static void checkLogURL() {
		if(!Ticket.TICKET_URL.exists()) {
			Ticket.TICKET_URL.mkdir();
		}
	}

	/**
	 * Reads in a log file given its URL.
	 * @param url the URL of a log file to read
	 * @return an array, each element of which is a line in the log file
	 * @throws IOException if the file cannot be read
	 */
	public static String[] readLogFile(String url) throws IOException {
		checkLogURL();
		
		FileReader     fr = new FileReader(url);
		BufferedReader br = new BufferedReader(fr);

		String[] log = new String[TicketInfo.NUM_ENTRIES];
		int i = 0;

		String thisLine = "";

		while ((thisLine = br.readLine()) != null) {
			log[i++] = thisLine;
		}

		br.close();

		return log;
	}

	/**
	 * Reads in a log file and returns an entry.
	 * @param url the URL of a log file to read
	 * @param pol the part of the log to be returned
	 * @return the desired entry in the log
	 * @throws IOException if the file cannot be read
	 */
	public static String readLogFile(String url, TicketInfo pol) throws IOException {
		checkLogURL();
		
		String[] log = readLogFile(url);

		return log[pol.i];
	}

	/**
	 * Reads in a log file and populates the MainWindow's fields.
	 * @param ticket the log to read into the program.
	 * @param mw the window to populate.
	 * @throws IOException if the file cannot be read.
	 */
	public static void readLogFile(String url, MainWindow mw) throws IOException {
		checkLogURL();
		
		String[] log = readLogFile(url);

		mw.setDocTitle(log[TicketInfo.TITLE.i]);
		mw.set32PartNumber(log[TicketInfo.PART_NUM_32.i]);
		mw.set37PartNumber(log[TicketInfo.PART_NUM_37.i]);
		mw.setDocDate(log[TicketInfo.DATE.i]);
		mw.setGUID(log[TicketInfo.GUID.i]);
		mw.setPerforcePath(log[TicketInfo.PERFORCE_URL.i]);
		mw.setJiraTicketSummary(log[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		mw.setJiraTicketURL(log[TicketInfo.JIRA_TICKET_URL.i]);
		mw.setTCIS(log[TicketInfo.TCIS_URL.i]);
		mw.setStatus(Integer.parseInt(log[TicketInfo.STATUS.i]));
		mw.setAuthor(log[TicketInfo.REPORT.i]);
	}

	/**
	 * Writes the given file to its own log file.
	 * @throws IOException
	 */
	public static void writeChangesToLog(MainWindow mw) throws IOException {
		checkLogURL();
		
		FileWriter     fw = new FileWriter(new File(TICKET_URL.toPath() + "\\" + mw.getSaveFileName()));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(mw.toString());
		
		bw.close();
	}
	
	/**
	 * Writes the given file to its own log file.
	 * @throws IOException
	 */
	public static void writeChangesToLog(String[] log) throws IOException {
		checkLogURL();
		
		String toReturn = "";
		
		toReturn = log[TicketInfo.TITLE.i]; 

		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.PART_NUM_32.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.PART_NUM_37.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.DATE.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.GUID.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.PERFORCE_URL.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.JIRA_TICKET_DESCRIPTION.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.JIRA_TICKET_URL.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.TCIS_URL.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.STATUS.i]);
		toReturn = Tools.appendToNewLine(toReturn, log[TicketInfo.REPORT.i]);
		
		FileWriter     fw = new FileWriter(new File(TICKET_URL.toPath() + "\\" + log[TicketInfo.JIRA_TICKET_DESCRIPTION.i] + ".log"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(toReturn);
		
		bw.close();
	}
}
