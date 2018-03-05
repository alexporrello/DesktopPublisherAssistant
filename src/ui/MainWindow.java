package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.itextpdf.text.DocumentException;

import jm.JMButton;
import jm.JMColor;
import jm.JMPanel;
import jm.JMTextField;
import jtext.JTextCopy;
import jtext.JTextLink;
import jtext.JTextPartNum;
import pdf.PDFPropertiesUpdater;
import ticket.Ticket;
import ticket.TicketInfo;

/**
 * The window upon which is displayed all fields and information.
 * @author Alexander Porrello
 */
public class MainWindow extends JMPanel {
	private static final long serialVersionUID = 8053959756132135670L;

	/** The title of the document **/
	private JMTextField title = new JMTextField("", true);

	/** The field for the online part number **/
	private JTextPartNum partNum37 = new JTextPartNum("");

	/** The field for the print part number **/
	private JTextPartNum partNum32 = new JTextPartNum("");

	/** The date of the document **/
	private JMTextField date = new JMTextField();

	/** The document's "global unique identifier" **/
	private JTextCopy GUID = new JTextCopy("");

	/** The URL to the file in perforce **/
	private JTextCopy perforce = new JTextCopy("");

	/** The name of the Jira ticket **/
	private JMTextField jiraSummary = new JMTextField();

	/** The author of the document **/
	private JMTextField author = new JMTextField();

	/** The URL to the Jira ticket **/
	private JTextLink jiraURL = new JTextLink("");

	/** The URL to the document in TCIS **/
	private JTextLink tcisURL = new JTextLink("");

	/** Allows user to select the current status of the Jira ticket **/
	private JComboBox<String> status = new JComboBox<String>(Ticket.STATUS_OPTIONS);

	/** The URL of the local directory **/
	private String workingDirectory = "";

	/** The URL to the local PDFs folder in the {@link #workingDirectory} **/
	private String localPDFsURL = "";

	/** The URL to the "Checklists: folder in the {@link #workingDirectory} **/
	private String localChecklistsURL = "";

	/** The JFrame in which this JPanel is displayed **/
	private JFrame parent;

	/** Load all of the reports the user has worked with and load them into here **/
	private HashSet<String> reports = new HashSet<String>();

	/** Listens to the clipboard for when the user copies new text **/
	private ClipBoardListener cbl = new ClipBoardListener();

	JMButton cwd;
	JMButton cps;
	JMButton cdpc;

	public MainWindow(JFrame parent) {
		this.parent = parent;

		setBackground(JMColor.DEFAULT_BACKGROUND);
		setLayout(new GridBagLayout());
		updateAuthorHashSet();
		createFields();
	}

	/** Adds all of the fields to the GUI **/
	private void createFields() {

		int top   = 2;
		int left  = 5;
		int bot   = 0;
		int right = 0;

		Insets insets = new Insets(top, left, bot, right);

		int y = 0;

		makeAllButtons();

		add(createJLabel("Doc Title: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(title), Tools.createGBC(1, y, 1.0, insets));
		add(cwd, Tools.createGBC(2, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 5)));
		y++;

		add(createJLabel("32 Part Number: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(partNum32.getTextField());
		add(partNum32, Tools.createGBC(1, y, 1.0, insets));
		add(cps, Tools.createGBC(2, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 5)));;
		y++;

		add(createJLabel("37 Part Number: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(partNum37.getTextField());
		add(partNum37, Tools.createGBC(1, y, 1.0, insets));
		add(cdpc, Tools.createGBC(2, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 5)));;
		y++;

		add(createJLabel("Doc Date: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(date), Tools.createGBC(1, y, 1.0, insets));
		y++;

		//add(new Separator(), Tools.createGBC(0, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 0), 2));
		//y++;

		add(createJLabel("GUID: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(GUID.getTextField());
		add(GUID, Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Perforce Path: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(perforce.getTextField());
		add(perforce, Tools.createGBC(1, y, 1.0, insets));
		y++;

		//add(new Separator(), Tools.createGBC(0, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 0), 2));
		//y++;

		add(createJLabel("Jira Ticket Summary: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(jiraSummary), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Jira Ticket Reporter: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(author), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Jira Ticket URL: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(jiraURL.getTextField());
		add(jiraURL, Tools.createGBC(1, y, 1.0, insets));
		y++;

		//add(new Separator(), Tools.createGBC(0, y, 0.0, new Insets(insets.top, insets.left, insets.bottom, 0), 2));
		//y++;

		add(createJLabel("TCIS URL: "), Tools.createGBC(0, y, 0.0, insets));
		setUpText(tcisURL.getTextField());
		add(tcisURL, Tools.createGBC(1, y, 1.0, insets));
		y++;

		//TODO Customize this to match theme
		add(createJLabel("Status: "), Tools.createGBC(0, y, 0.0, insets));
		add(status, Tools.createGBC(1, y, 1.0,insets));
		
		y++;
	}

	/** 
	 * Sets up the three JButtons in the main body of the program.
	 */
	private void makeAllButtons() {
		// Create the "Create Working Directory" Button
		cwd = new JMButton("  Create Working Directory  ");
		cwd.setToolTipText("Enable this button by entering the doc title.");
		cwd.setButtonEnabled(false);
		cwd.addActionListner(e -> createWorkingDirectory());

		// Create the "Copy Print Spec Doc" Button
		cps = new JMButton("  Copy Print Spec Doc  ");
		cps.setToolTipText("Enable button by entering the 32 part number and the doc title.");
		cps.setButtonEnabled(false);
		cps.addActionListner(e -> copyPrintSpecDocument());

		//Create the "Copy DocProChecklist.pdf" Button
		cdpc = new JMButton("  Copy DocProChecklist  ");
		cdpc.setToolTipText("Enable button by entering the 37 part number, doc title, and date.");
		cdpc.setButtonEnabled(false);
		cdpc.addActionListner(e -> copyDocProChecklist());
	}

	/**
	 * Creates a local directory on the desktop that mirrors directory structure in Perforce.
	 */
	private void createWorkingDirectory() {
		String url = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		workingDirectory   = url + "\\" + title.getText();
		localPDFsURL       = workingDirectory + "\\" + "PDFs";
		localChecklistsURL = workingDirectory + "\\" + "Checklists";

		new File(workingDirectory).mkdir();
		new File(localPDFsURL).mkdir();
		new File(localChecklistsURL).mkdir();
	}

	/**
	 * Copies the Print Spec Document to the directory created in {@link #createWorkingDirectory()}.
	 */
	private void copyPrintSpecDocument() {
		if(!new File(localPDFsURL).exists()) {
			createWorkingDirectory();
		}

		String modifiedNumber = partNum32.getText().toLowerCase();
		String[] split = modifiedNumber.split("-");
		if(split.length > 1) {
			modifiedNumber = split[0];
		}

		try {
			String output = localPDFsURL + "\\S" + modifiedNumber + ".pdf";
			Tools.copyOutResource("TCIS-Print-Specification_Template.pdf", localPDFsURL + "\\S" + modifiedNumber + ".pdf");
			PDFPropertiesUpdater.autoFillPrintSpec(output, title.getText(), getPartNumbers(), date.getText());
		} catch (IOException | DocumentException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Copies the DocProChecklist to the directory created in {@link #createWorkingDirectory()}.
	 */
	private void copyDocProChecklist() {
		if(!new File(localChecklistsURL).exists()) {
			createWorkingDirectory();
		}

		try {
			String output =  workingDirectory + "\\Checklists\\DocProChecklist.pdf";
			Tools.copyOutResource("DocProChecklist.pdf", output);
			PDFPropertiesUpdater.autoFillDocProChecklist(output, title.getText(), getPartNumbers(), date.getText());				
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			System.err.println("DocProChecklist.pdf could not be auto-filled.");
		}
	}

	/**
	 * Enables and disables buttons if conditions are met.
	 */
	private void enableButton() {
		if(!Tools.isEmpty(title)) {
			if(!Tools.isEmpty(partNum32.getTextField())) {
				cps.setButtonEnabled(true);
			} else {
				cps.setButtonEnabled(false);
			}

			if(!Tools.isEmpty(date) && (!Tools.isEmpty(partNum37.getTextField()) || !Tools.isEmpty(partNum32.getTextField()))) {
				cdpc.setButtonEnabled(true);
			} else {
				cdpc.setButtonEnabled(false);
			}

			cwd.setButtonEnabled(true);
		} else {
			cwd.setButtonEnabled(false);
			cdpc.setButtonEnabled(false);
			cps.setButtonEnabled(false);
		}
	}

	/**
	 * Adds a focus listener to a JTextField that highlights text field when focus is gained.
	 * Adds a key listener that removes any whitespace from the end of entries.
	 * @param toSetup the JTextField that will be setup.
	 * @return a setup JTextField
	 */
	private JTextField setUpText(JTextField toSetup) {
		toSetup.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				enableButton();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				enableButton();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {				
				enableButton();
			}
		});

		return toSetup;
	}

	/**
	 * Creates a JLabel.
	 * @param text the JLabel's text
	 * @return a JLabel
	 */
	private JLabel createJLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(JMColor.DEFAULT_FONT_COLOR);
		return label;
	}

	/**
	 * Gathers information from the fields and sends the publishing email to 
	 * Doc Pro Publishing group.
	 */
	public void emailDocProPublishingGroup() {
		String partNumbers = "Document Title";
		String tcisURL = "TCIS (APEX) URL";
		String checklistURL = "Path to DocProChecklist.pdf in Perforce";
		String pdfsURL = "Path to PDFs folder in Perforce";

		partNumbers = getPartNumbers();

		if(!Tools.isEmpty(this.tcisURL.getTextField())) {
			tcisURL = this.tcisURL.getText();
		}

		if(!Tools.isEmpty(perforce.getTextField())) {
			String perforceURL = perforce.getText();

			if(perforceURL.endsWith(" ")) {
				perforceURL = perforceURL.trim();
			}
			if(!perforceURL.endsWith("/")) {
				perforceURL = perforceURL + "/";
			}

			checklistURL = perforceURL + "Checklists/DocProChecklist.pdf";
			pdfsURL      = perforceURL + "PDFs";
		}

		String subject = "PUBLISHING: " + partNumbers;
		String body    = checklistURL + "\n" + pdfsURL + "\n\n"+ tcisURL;

		List<String> recips = new ArrayList<String>();
		recips.add("Doc.Pro.Publishing.Group@ni.com");

		try {
			MailTo.mailto(recips, subject, body);
		} catch (IOException | URISyntaxException e1) {
			StringSelection stringSelection = new StringSelection(subject + "\n\n" + body);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		}			
	}

	/**
	 * Returns the title of the document from info gathered in {@link #partNum32} and {@link #partNum37}.
	 * @return a string of the document title.
	 */
	private String getPartNumbers() {
		String docTitle = "Document Title";

		if(!Tools.isEmpty(partNum32.getTextField())) {
			if(!Tools.isEmpty(partNum37.getTextField())) {
				docTitle = partNum32.getText() + ", " + partNum37.getText();
			} else {
				docTitle = partNum32.getText();
			}
		} else if(!Tools.isEmpty(partNum37.getTextField())) {
			docTitle = partNum37.getText();
		}

		return docTitle;
	}

	/** Sets everything back to default **/
	public void clearAll() {
		title.setText("");
		partNum37.setText("");
		partNum32.setText("");
		date.setText("");
		GUID.setText("");
		perforce.setText("");
		jiraSummary.setText("");
		author.setText("");
		jiraURL.setText("");
		tcisURL.setText("");
		status.setSelectedIndex(0);
		workingDirectory = "";
		localPDFsURL = "";
		localChecklistsURL = "";
		parent.setTitle("Desktop Publisher Assistant");
		title.requestFocus();
	}

	/**
	 * If a given string is recognized, it is auto-added to empty fields.
	 * @param s the string to be auto-added.d TODO
	 */
	public void autoAddString(String s) {
		s = s.trim();

		if(s.startsWith("//")) {
			perforce.setTextIfEmpty(s);
		} else if(s.contains("32") && s.contains("37") && s.split(", ").length == 2) {
			String partNumA = s.split(", ")[0];
			String partNumB = s.split(", ")[1];
			
			if(partNumA.startsWith("32")) {
				partNum32.setTextIfEmpty(partNumA);
				partNum37.setTextIfEmpty(partNumB);
			} else {
				partNum32.setTextIfEmpty(partNumB);
				partNum37.setTextIfEmpty(partNumA);
			}
		} else if(s.startsWith("32")) {
			partNum32.setTextIfEmpty(s);
		} else if(s.startsWith("37")) {
			partNum37.setTextIfEmpty(s);
		} else if(s.contains("GUID")) {
			GUID.setTextIfEmpty(s);
		} else if(s.contains("nijira")) {
			jiraURL.setTextIfEmpty(s);
		} else if(s.contains("Prepare") || s.contains("Apply") || s.contains("Signoff")) {
			setTextIfEmpty(jiraSummary, s);
		} else if(s.contains("apex.natinst")) {			
			tcisURL.setTextIfEmpty(s);
		} else {
			String[] titles = {"specifications", "user manual", "specs"};
			
			for(String title : titles) {
				if(contains(s, title)) {
					setTextIfEmpty(this.title, s);
				}
			}
			
			String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
			
			for(String month : months) {
				if(s.toLowerCase().contains(month.toLowerCase())) {
					setTextIfEmpty(date, s);
				}
			}

			for(String report : reports) {
				if(report.toLowerCase().contains(s.toLowerCase())) {
					setTextIfEmpty(author, s);
				}
			}
		}
	}

	private Boolean contains(String s, String ss) {
		return s.toLowerCase().contains(ss.toLowerCase());
	}
	
	/**
	 * Sets a given JTextField's text only if it is empty.
	 * @param field the field to be set
	 * @param s the string to set
	 */
	private void setTextIfEmpty(JTextField field, String s) {
		if(Tools.isEmpty(field)) {
			field.setText(s);
			enableButton();
		}
	}

	/**
	 * @return The name of the file if it will be returned.
	 * @throws NoSuchFileException if no Jira Ticket Summary has been entered. 
	 */
	public String getSaveFileName() throws NoSuchFileException {
		if(!Tools.isEmpty(jiraSummary)) {
			return jiraSummary.getText().trim() + ".log";
		} else {
			throw new NoSuchFileException("Enter the Jira Ticket Summary to save the file.");
		}
	}

	/**
	 * Populates all the fields in the program's body.
	 * @param ticket the ticket file to add to the program.
	 */
	public void setLog(String[] ticket) {
		setAll(
				ticket[TicketInfo.TITLE.i], 
				ticket[TicketInfo.PART_NUM_32.i],
				ticket[TicketInfo.PART_NUM_37.i], 
				ticket[TicketInfo.DATE.i], 
				ticket[TicketInfo.GUID.i], 
				ticket[TicketInfo.PERFORCE_URL.i], 
				ticket[TicketInfo.TICKET_DESCRIPTION.i], 
				ticket[TicketInfo.REPORT.i],
				ticket[TicketInfo.JIRA_TICKET_URL.i], 
				ticket[TicketInfo.TCIS_URL.i],
				Integer.parseInt(ticket[TicketInfo.STATUS.i]));
	}

	/**
	 * Populates all of the fields in the program's body.
	 * @param title the ticket's title
	 * @param partNum32 the ticket document's 32 part number
	 * @param partNum37 the ticket document's 37 part number
	 * @param date the ticket document's date
	 * @param GUID the ticket document's GUID
	 * @param perforce the ticket document's Perforce path
	 * @param jiraSummary the ticket's summary
	 * @param author the ticket's author
	 * @param jiraURL the ticket's URL
	 * @param tcisURL the ticket document's URL in TCIS
	 * @param status the ticket's status
	 */
	public void setAll(String title, String partNum32, String partNum37, String date, String GUID, String perforce,
			String jiraSummary, String author, String jiraURL, String tcisURL, int status) {

		clearAll();

		this.parent.setTitle(title);
		this.title.setText(title);
		this.partNum37.setText(partNum37);
		this.partNum32.setText(partNum32);
		this.date.setText(date);
		this.GUID.setText(GUID);
		this.perforce.setText(perforce);
		this.jiraSummary.setText(jiraSummary);
		this.author.setText(author);
		this.jiraURL.setText(jiraURL);
		this.tcisURL.setText(tcisURL);
		this.status.setSelectedIndex(status);

		enableButton();
	}

	/** Populates the document title field with a given string **/
	public void setDocTitle(String toSet) {
		title.setText(toSet);
		title.setCaretPosition(0);
		parent.setTitle(toSet);
		enableButton();
	}

	/** Populates the 32 part number field with a given string **/
	public void set32PartNumber(String toSet) {
		partNum32.setText(toSet);
		partNum32.setCaretPosition(0);
		enableButton();
	}

	/** Populates the 37 part number field with a given string **/
	public void set37PartNumber(String toSet) {
		partNum37.setText(toSet);
		partNum37.setCaretPosition(0);
		enableButton();
	}

	/** Populates the date field with a given string **/
	public void setDocDate(String toSet) {
		date.setText(toSet);
		date.setCaretPosition(0);
		enableButton();
	}

	/** Populates the GUID field with a given string **/
	public void setGUID(String toSet) {
		GUID.setText(toSet);
		GUID.setCaretPosition(0);
	}

	/** Populates the Perforce path field with a given string **/
	public void setPerforcePath(String toSet) {
		perforce.setText(toSet);
		perforce.setCaretPosition(0);
	}

	/** Populates the Jira Ticket Summary field with a given string **/
	public void setJiraTicketSummary(String toSet) {
		jiraSummary.setText(toSet);
		jiraSummary.setCaretPosition(0);
	}

	/** Populates the Jira ticket URL field with a given string **/
	public void setJiraTicketURL(String toSet) {
		jiraURL.setText(toSet);
		jiraURL.setCaretPosition(0);
	}

	/** Populates the tcisURL field with a given string **/
	public void setTCIS(String toSet) {
		tcisURL.setText(toSet);
		tcisURL.setCaretPosition(0);
	}

	/** Populates the status combo box with a given int **/
	public void setStatus(int i) {
		status.setSelectedIndex(i);
	}

	/** Populates the author field with a given string **/
	public void setAuthor(String toSet) {
		author.setText(toSet);
	}

	/**
	 * Checks if the current ticket is open in this Main Window.
	 * @param ticketDescription the ticket description of the f
	 * @return
	 */
	public Boolean isTicketOpen(String ticketDescription) {
		if(Tools.isEmpty(jiraSummary)) {
			return false;
		} else {
			return jiraSummary.getText().equals(ticketDescription);
		}
	}

	public void startClipboardListener() {
		cbl = new ClipBoardListener();
		cbl.start();
	}

	public void stopClipboardListener() {
		cbl.stop = true;
		cbl.interrupt();
		
		System.out.println(cbl.isInterrupted());
	}

	/**
	 * Loads all of the Jira reports the writer has worked with into {@link #reports}.
	 */
	public void updateAuthorHashSet() {
		for(File f : Ticket.TICKET_URL.listFiles()) {
			try {
				reports.add(Ticket.readLogFile(f.getAbsolutePath(), TicketInfo.REPORT).trim());
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		String toReturn = "";

		toReturn = title.getText();

		toReturn = Tools.appendToNewLine(toReturn, partNum32.getText());
		toReturn = Tools.appendToNewLine(toReturn, partNum37.getText());
		toReturn = Tools.appendToNewLine(toReturn, date.getText());
		toReturn = Tools.appendToNewLine(toReturn, GUID.getText());
		toReturn = Tools.appendToNewLine(toReturn, perforce.getText());
		toReturn = Tools.appendToNewLine(toReturn, jiraSummary.getText());
		toReturn = Tools.appendToNewLine(toReturn, jiraURL.getText());
		toReturn = Tools.appendToNewLine(toReturn, tcisURL.getText());
		toReturn = Tools.appendToNewLine(toReturn, status.getSelectedIndex() + "");
		toReturn = Tools.appendToNewLine(toReturn, author.getText());

		return toReturn;
	}

	public class Separator extends JMPanel {
		private static final long serialVersionUID = -7433172482073590125L;

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
		}

	}

	public class ClipBoardListener extends Thread implements ClipboardOwner {

		public Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();  
		public Boolean stop = false;

		@Override
		public void run() {
			if(!stop) {
				Transferable trans = sysClip.getContents(this);
				TakeOwnership(trans);
			}
		}  

		@Override
		public void lostOwnership(Clipboard c, Transferable t) {
			if(!stop) {
				try {  
					ClipBoardListener.sleep(10);
				} catch(Exception e) {  
					System.err.println("Exception: " + e);  
				}  
				Transferable contents = sysClip.getContents(this);  

				try {
					processClipboard(contents, c);
				} catch (Exception ex) {
					Logger.getLogger(ClipBoardListener.class.getName()).log(Level.SEVERE, null, ex);
				}

				TakeOwnership(contents);
			}
		}  

		void TakeOwnership(Transferable t) {
			sysClip.setContents(t, this);
		}  

		public void processClipboard(Transferable t, Clipboard c) {
			if(!stop) {
				String tempText;
				Transferable trans = t;

				try {
					if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						tempText = (String) trans.getTransferData(DataFlavor.stringFlavor);
						autoAddString(tempText);
					}
				} catch (UnsupportedFlavorException | IOException e2) {
					System.err.println("This is an unsupported flavor. Contents from clipboard could not be read.");
				}
			}
		}
	}
}
