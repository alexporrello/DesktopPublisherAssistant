package ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import log.LogDialog;
import ticket.Ticket;
import ui.DesktopPublisherAssistant.MainWindow;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 3492580614560658292L;

	MainWindow mainWindow;

	public MenuBar(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		add(new FileMenu());
		add(new EditMenu());
		add(new ToolsMenu());
	}
	
	public class EditMenu extends JMenu {
		private static final long serialVersionUID = 6570741290262673385L;
		
		JMenuItem clearAll = new JMenuItem("Clear All");
		
		public EditMenu() {
			super("Edit");
			
			add(clearAll());
		}
		
		public JMenuItem clearAll() {
			clearAll.addActionListener(e -> {
				mainWindow.clearAll();
			});
			clearAll.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_X, (
							java.awt.event.InputEvent.SHIFT_DOWN_MASK | 
							(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
			
			return clearAll;
		}
	}

	public class ToolsMenu extends JMenu {
		private static final long serialVersionUID = 255591321997575423L;

		JMenuItem copyEmail = new JMenuItem("Copy Doc Pro Publishing Group Email");
		
		/** Generates the email to be sent out to Doc Pro Publishing Group to approve PPMs. **/
		JMenuItem copyEmailBody = new JMenuItem("Generate Doc Pro Publishing Group Email Body");		
				
		public ToolsMenu() {
			super("Tools");
			
			add(copyEmail());
			add(copyEmailBody());
			
			addSeparator();
			
			JMenuItem copyTemplate = new JMenuItem("Copy Print Template Perforce Path");
			copyTemplate.addActionListener(e -> {
				StringSelection stringSelection = new StringSelection("//TechComm/Templates/Print Templates");
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			});
			add(copyTemplate);
		}
		
		/**
		 * Sets up the {@link #copyEmail} menu item.
		 * @return {@link #copyEmail} set up.
		 */
		public JMenuItem copyEmail() {
			copyEmail.addActionListener(e -> {
				StringSelection stringSelection = new StringSelection("Doc.Pro.Publishing.Group@ni.com");
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			});
			copyEmail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			return copyEmail;
		}

		/**
		 * Sets up the {@link #copyEmail} menu item.
		 * @return {@link #copyEmail} set up.
		 */
		public JMenuItem copyEmailBody() {
			copyEmailBody.addActionListener(e -> {
				mainWindow.copyEmailBody();
			});
			copyEmailBody.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_E, (
							java.awt.event.InputEvent.SHIFT_DOWN_MASK | 
							(Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
			return copyEmailBody;
		}
	}
	
	public class FileMenu extends JMenu {
		private static final long serialVersionUID = -754503798159404299L;


		/** Opens the log file that shows all the logs **/
		JMenuItem openLog   = new JMenuItem("View Log");
		
		/** Saves the currently open project to a .log file **/
		JMenuItem saveToLog = new JMenuItem("Save Ticket");
		
		/** Opens a ticket project and loads it into the application **/
		JMenuItem open = new JMenuItem("Open Ticket");
		
		FileMenu() {
			super("File");
			
			add(openLog());

			addSeparator();
			
			add(open());
			add(save());
		}

		/**
		 * Sets the {@link #saveToLog} menu item.
		 * @return {@link #saveToLog} set up.
		 */
		public JMenuItem save() {
			saveToLog.addActionListener(e -> {
				try {
					Ticket.writeChangesToLog(mainWindow);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			saveToLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			return saveToLog;
		}

		/**
		 * Sets up the {@link #openLog} menu item.
		 * @return {@link #openLog} set up.
		 */
		public JMenuItem openLog() {
			openLog.addActionListener(e -> {
				new LogDialog().setVisible(true);
			});
			openLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			return openLog;
		}
		
		/**
		 * Sets up the {@link #open} menu item.
		 * @return {@link #open} set up.
		 */
		public JMenuItem open() {
			open.addActionListener(e -> {
				try {
					Ticket.readLogFile(openFile(), mainWindow);
				} catch (NoSuchFileException e1) {
					System.out.println("No file was selected.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
						
			return open;
		}
		
		/**
		 * Opens a new JFileChooser so the user can open a file.
		 * @return the URL of the selected file as a string
		 * @throws NoSuchFileException if the user does not choose a file.
		 */
		private String openFile() throws NoSuchFileException {
			JFileChooser jfc = new JFileChooser(Ticket.TICKET_URL);
			
			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				return selectedFile.getAbsolutePath();
			}
			
			throw new NoSuchFileException("No file was selected.");
		}
	}
}
