package ui;

import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import ticket.Ticket;

/**
 * The window upon which is displayed all fields and information.
 * @author Alexander Porrello
 */
public class MainWindow extends JMPanel {
	private static final long serialVersionUID = 8053959756132135670L;

	/** The title of the document **/
	private JTextField title = new JTextField();

	/** The field for the online part number **/
	private JTextField partNum37 = new JTextField();

	/** The field for the print part number **/
	private JTextField partNum32 = new JTextField();

	/** The date of the document **/
	private JTextField date = new JTextField();

	/** The document's "global unique identifier" **/
	private JTextField GUID = new JTextField();

	/** The URL to the file in perforce **/
	private JTextField perforce = new JTextField();

	/** The name of the Jira ticket **/
	private JTextField jiraSummary = new JTextField();

	/** The author of the document **/
	private JTextField author = new JTextField();

	/** The URL to the Jira ticket **/
	private JTextField jiraURL = new JTextField();

	/** The URL to the Jira ticket **/
	private JTextField jiraKey = new JTextField();
	
	/** The URL to the document in TCIS **/
	private JTextField tcisURL = new JTextField();

	/** Allows user to select the current status of the Jira ticket **/
	private JComboBox<String> status = new JComboBox<String>(Ticket.STATUS_OPTIONS);

	/** The URL of the local directory **/
	private String workingDirectory = "";

	/** The URL to the local PDFs folder in the {@link #workingDirectory} **/
	private String localPDFsURL = "";

	/** The URL to the "Checklists: folder in the {@link #workingDirectory} **/
	private String localChecklistsURL = "";

	private JFrame parent;
	
	public MainWindow(JFrame parent) {
		this.parent = parent;
		
		setLayout(new GridBagLayout());
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

		setupDocTitleField(y, insets);
		y++;

		setupPrintSpecField(y, insets);
		y++;

		add(createJLabel("37 Part Number: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(partNum37), Tools.createGBC(1, y, 1.0, insets));
		y++;
		y++;

		add(createJLabel("Doc Date: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(date), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("GUID: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(GUID), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Preforce Path: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(perforce), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Jira Ticket Summary: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(jiraSummary), Tools.createGBC(1, y, 1.0, insets));
		y++;

		//add(createJLabel("Jira Ticket Key: "), Tools.createGBC(0, y, 0.0, insets));
		//add(setUpText(jiraKey), Tools.createGBC(1, y, 1.0, insets));
		//y++;
		
		add(createJLabel("Jira Ticket Reporter: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(author), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Jira Ticket URL: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(jiraURL), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("TCIS URL: "), Tools.createGBC(0, y, 0.0, insets));
		add(setUpText(tcisURL), Tools.createGBC(1, y, 1.0, insets));
		y++;

		add(createJLabel("Status: "), Tools.createGBC(0, y, 0.0, insets));
		add(status, Tools.createGBC(1, y, 1.0,insets));
		y++;
	}

	/**
	 * Sets up an adds the "Create Working Directory" button and set the window title once the 
	 * doc name is added.
	 * @param yPosn the y position of the field.
	 */
	private void setupDocTitleField(int yPosn, Insets insets) {		
		JButton cwd = new JButton("Create Working Directory");
		cwd.addActionListener(e -> {
			createWorkingDirectory();
		});

		title.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(title.getText().length() > 0) {
					parent.setTitle(title.getText());
					replaceCommaWithDash();
				}
			}
		});
		title.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				replaceCommaWithDash();
			}
		});

		add(createJLabel("Doc Title: "), Tools.createGBC(0, yPosn, 0.0, insets));
		add(setUpText(title), Tools.createGBC(1, yPosn, 1.0, insets));
		add(cwd, Tools.createGBC(2, yPosn, 0.0, new Insets(insets.top, insets.left, insets.bottom, 5)));
	}

	private void replaceCommaWithDash() {
		if(title.getText().contains(",")) {
			int caret = title.getCaretPosition();
			title.setText(title.getText().replace(",", "-"));
			title.setCaretPosition(caret);
		}
	}

	public void createWorkingDirectory() {
		if(!title.getText().equals("")) {
			String url = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

			workingDirectory   = url + "\\" + title.getText();
			localPDFsURL       = workingDirectory + "\\" + "PDFs";
			localChecklistsURL = workingDirectory + "\\Checklists";

			new File(workingDirectory).mkdir();
			new File(localPDFsURL).mkdir();
			new File(localChecklistsURL).mkdir();

			try {
				Files.copy(
						DesktopPublisherAssistant.class.getClassLoader().getResourceAsStream("DocProChecklist.pdf"), 
						new File(localChecklistsURL + "\\DocProChecklist.pdf").toPath(), 
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets up the "Copy Print Spec Doc" button and adds the 37 part fields.
	 * @param yPosn the y position of the field.
	 */
	private void setupPrintSpecField(int yPosn, Insets insets) {
		JButton cpsd = new JButton("Copy Print Spec Doc");
		cpsd.addActionListener(e -> {
			if(!partNum32.getText().equals("") && new File(localPDFsURL).exists()) {
				String modifiedNumber = partNum32.getText().toLowerCase();
				String[] split = modifiedNumber.split("-");
				if(split.length > 1) {
					modifiedNumber = split[0];
				}

				try {
					Files.copy(
							DesktopPublisherAssistant.class.getClassLoader().getResourceAsStream("TCIS-Print-Specification_Template.pdf"), 
							new File(localPDFsURL + "\\S" + modifiedNumber + ".pdf").toPath(), 
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		add(createJLabel("32 Part Number: "), Tools.createGBC(0, yPosn, 0.0, insets));
		add(setUpText(partNum32), Tools.createGBC(1, yPosn, 1.0, insets));
		add(cpsd, Tools.createGBC(2, yPosn, 0.0, new Insets(insets.top, insets.left, insets.bottom, 5)));;
	}

	public void emailDocProPublishingGroup() {
		String docTitle = "Document Title";
		String tcisURL = "TCIS (APEX) URL";
		String checklistURL = "Path to DocProChecklist.pdf in Perforce";
		String pdfsURL = "Path to PDFs folder in Perforce";

		if(!Tools.isEmpty(partNum32)) {
			if(!Tools.isEmpty(partNum37)) {
				docTitle = partNum32.getText() + ", " + partNum37.getText();
			} else {
				docTitle = partNum32.getText();
			}
		} else if(!Tools.isEmpty(partNum37)) {
			docTitle = partNum37.getText();
		}

		if(!Tools.isEmpty(this.tcisURL)) {
			tcisURL = this.tcisURL.getText();
		}

		if(!Tools.isEmpty(perforce)) {
			checklistURL = perforce.getText() + "Checklists/DocProChecklist.pdf";
			pdfsURL      = perforce.getText() + "PDFs";
		}

		String subject = "PUBLISHING: " + docTitle;
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
	 * Adds a focus listener to a JTextField that highlights text field when focus is gained.
	 * Adds a key listener that removes any whitespace from the end of entries.
	 * @param toSetup the JTextField that will be setup.
	 * @return a setup JTextField
	 */
	private JTextField setUpText(JTextField toSetup) {
		/** When a JTextFields get focus, select all of the text **/
		toSetup.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				toSetup.selectAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				toSetup.select(0, 0);
			}
		});

		/** If the user enters a string that has a blank character at the end, delete it. **/
		toSetup.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(toSetup.getText().endsWith(" ")) {
					toSetup.setText(toSetup.getText().substring(0, toSetup.getText().length()-1));
				}
			}
		});

		/** If user triple-clicks and the JTextField contains a URL, open it. **/
		toSetup.addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(e.getClickCount() == 3) {
						if(toSetup.getText().contains("https:")) {
							Desktop.getDesktop().browse(new URI(toSetup.getText()));
						}
					}
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
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
		return label;
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
		jiraKey.setText("");
		workingDirectory = "";
		localPDFsURL = "";
		localChecklistsURL = "";
		parent.setTitle("Desktop Publisher Assistant");
		title.requestFocus();
	}

	public void setAll(String title, String partNum32, String partNum37, String date, String GUID, String perforce,
			String jiraSummary, String author, String jiraURL, String tcisURL, int status, String jiraKey) {

		clearAll();

		parent.setTitle(title);

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
		this.jiraKey.setText(jiraKey);
	}

	/**
	 * If a given string is recognized, it is auto-added to empty fields.
	 * @param s the string to be auto-added.
	 */
	public void autoAddString(String s) {
		if(s.startsWith("32")) {
			setTextIfEmpty(partNum32, s);
		} else if(s.startsWith("37")) {
			setTextIfEmpty(partNum37, s);
		} else if(s.startsWith("//")) {
			setTextIfEmpty(perforce, s);
		} else if(s.contains("GUID")) {
			setTextIfEmpty(GUID, s);
		} else if(s.contains("nijira")) {
			setTextIfEmpty(jiraURL, s);
		} else if(s.contains("Prepare") || s.contains("Apply")) {
			setTextIfEmpty(jiraSummary, s);
		} else if(s.contains("apex.natinst")) {
			setTextIfEmpty(tcisURL, s);
		} else if(s.contains("TCIS-")) {
			setTextIfEmpty(jiraKey, s);
		}
	}

	/**
	 * Sets a given JTextField's text only if it is empty.
	 * @param field the field to be set
	 * @param s the string to set
	 */
	private void setTextIfEmpty(JTextField field, String s) {
		if(field.getText().length() == 0) {
			field.setText(s);
		}
	}

	/**
	 * @return The name of the file if it will be returned.
	 */
	public String getSaveFileName() {
		if(jiraSummary.getText().length() > 0) {
			return jiraSummary.getText() + ".log";
		} else {
			return "untitledPPM.log";
		}
	}

	public void setDocTitle(String toSet) {
		title.setText(toSet);
		title.setCaretPosition(0);
		parent.setTitle(toSet);
		replaceCommaWithDash();
	}

	public void set32PartNumber(String toSet) {
		partNum32.setText(toSet);
		partNum32.setCaretPosition(0);
	}

	public void set37PartNumber(String toSet) {
		partNum37.setText(toSet);
		partNum37.setCaretPosition(0);
	}

	public void setDocDate(String toSet) {
		date.setText(toSet);
		date.setCaretPosition(0);
	}

	public void setGUID(String toSet) {
		GUID.setText(toSet);
		GUID.setCaretPosition(0);
	}

	public void setPerforcePath(String toSet) {
		perforce.setText(toSet);
		perforce.setCaretPosition(0);
	}

	public void setJiraTicketSummary(String toSet) {
		jiraSummary.setText(toSet);
		jiraSummary.setCaretPosition(0);
	}

	public void setJiraTicketURL(String toSet) {
		jiraURL.setText(toSet);
		jiraURL.setCaretPosition(0);
	}

	public void setTCIS(String toSet) {
		tcisURL.setText(toSet);
		tcisURL.setCaretPosition(0);
	}

	public void setStatus(int i) {
		status.setSelectedIndex(i);
	}

	public void setAuthor(String toSet) {
		author.setText(toSet);
	}
	
	public void setJiraKey(String toSet) {
		jiraKey.setText(toSet);
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
		toReturn = Tools.appendToNewLine(toReturn, jiraKey.getText());

		return toReturn;
	}
}