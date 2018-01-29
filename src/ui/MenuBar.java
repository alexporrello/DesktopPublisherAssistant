package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import log.LogWindow;
import pdf.XMPUpdateWindow;
import ticket.Ticket;
import ui.MainWindow;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 3492580614560658292L;

	MainWindow mainWindow;

	Boolean logDialogVisible = true;
	Boolean xmpUpdateVisible = true;

	XMPUpdateWindow xmpUpdate;
	
	JFrame parent;

	public MenuBar(MainWindow mainWindow, LogWindow logDialogScroll, XMPUpdateWindow xmpUpdate, JFrame parent) {
		this.mainWindow = mainWindow;
		this.xmpUpdate  = xmpUpdate;
		this.parent     = parent;

		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		add(new FileMenu());
		add(new EditMenu());
		add(new ToolsMenu());

		JMenu view = new JMenu("View");

		JCheckBoxMenuItem  showLogDialog = new JCheckBoxMenuItem ("Show Log");
		showLogDialog.setSelected(true);
		showLogDialog.addActionListener( e-> {
			logDialogScroll.setVisible(showLogDialog.isSelected());
			mainWindow.revalidate();
			mainWindow.repaint();
		});
		view.add(showLogDialog);
		JCheckBoxMenuItem  showXMPUpdate = new JCheckBoxMenuItem ("Show XMP Updater");
		showXMPUpdate.setSelected(true);
		showXMPUpdate.addActionListener(e -> {
			xmpUpdate.setVisible(showXMPUpdate.isSelected());
			mainWindow.revalidate();
			mainWindow.repaint();
		});
		view.add(showXMPUpdate);

		//add(view);
		
		addHelpMenu();
	}
	
	public void addHelpMenu() {
		JMenu help = new JMenu("Help");
		
		JMenuItem update = new JMenuItem("Check for Updates");
		update.addActionListener(e -> {
			JDialog updateDialog = new JDialog();

			updateDialog.setIconImages(Tools.imageIcon());
			updateDialog.setLayout(new BorderLayout());
			updateDialog.setLocationByPlatform(true);
			updateDialog.setTitle("Update Dialog");
			updateDialog.setModal(true);
			
			Boolean doesUpdateExist = Tools.doesUpdateExist();
			
			JLabel updateLabel;

			if(doesUpdateExist) {
				updateLabel = new JLabel("An Update is Available");
			} else {
				updateLabel = new JLabel("No Update is Available");
			}

			updateLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

			updateDialog.add(updateLabel, BorderLayout.CENTER);

			if(doesUpdateExist) {
				JButton updateButton = new JButton("Download Update");
				updateButton.setFocusPainted(false);
				updateButton.setBorder(BorderFactory.createEmptyBorder(7, 15, 7, 15));
				updateButton.addActionListener(f -> {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(new URI("https://github.com/alexporrello/DesktopPublisherAssistant/releases"));
						} catch (IOException | URISyntaxException e1) {
							updateLabel.setText("Update Page Could Not Be Opened.");
						}
					}
				});
				updateDialog.add(updateButton, BorderLayout.SOUTH);
			}

			updateDialog.pack();
			Tools.setDialogLocationFromParent(parent, updateDialog, true);
		});
		help.add(update);
		
		add(help);
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
				xmpUpdate.reset();
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

		/** Generates the email to be sent out to Doc Pro Publishing Group to approve PPMs. **/
		JMenuItem copyEmailBody = new JMenuItem("Email Doc Pro Publishing Group");		

		public ToolsMenu() {
			super("Tools");

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
		public JMenuItem copyEmailBody() {
			copyEmailBody.addActionListener(e -> {
				mainWindow.emailDocProPublishingGroup();
			});
			copyEmailBody.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_E, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
			return copyEmailBody;
		}
	}

	public class FileMenu extends JMenu {
		private static final long serialVersionUID = -754503798159404299L;


		/** Saves the currently open project to a .log file **/
		JMenuItem saveToLog = new JMenuItem("Save Ticket");

		/** Opens a ticket project and loads it into the application **/
		JMenuItem open = new JMenuItem("Open Ticket");

		FileMenu() {
			super("File");

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
		 * Sets up the {@link #open} menu item.
		 * @return {@link #open} set up.
		 */
		public JMenuItem open() {
			open.addActionListener(e -> {
				try {
					Ticket.readLogFile(Tools.loadFile("Select Ticket", Ticket.TICKET_URL), mainWindow);
				} catch (NoSuchFileException e1) {
					System.err.println("No file was selected.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			return open;
		}
	}
}
