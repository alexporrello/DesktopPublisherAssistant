package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileSystemView;

import jm.JMButton;
import jm.JMTextField;
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

	LogWindow logDialogScroll;

	public MenuBar(MainWindow mainWindow, LogWindow logDialogScroll, XMPUpdateWindow xmpUpdate, JFrame parent) {
		this.logDialogScroll = logDialogScroll;
		this.mainWindow = mainWindow;
		this.xmpUpdate  = xmpUpdate;
		this.parent     = parent;

		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		add(new FileMenu());
		add(new EditMenu());
		add(new ToolsMenu());
		add(new View());
		add(new HelpMenu());
		
		//add(new View(logDialogScroll));
	}

	public class View extends JMenu {
		private static final long serialVersionUID = -4652785373007319130L;

		public View() {
			super("View");

			JCheckBoxMenuItem  showLogDialog = new JCheckBoxMenuItem ("Show Log");
			showLogDialog.setSelected(true);
			showLogDialog.addActionListener( e-> {
				logDialogScroll.setVisible(showLogDialog.isSelected());
				mainWindow.revalidate();
				mainWindow.repaint();
			});

			JCheckBoxMenuItem  showXMPUpdate = new JCheckBoxMenuItem ("Show XMP Updater");
			showXMPUpdate.setSelected(true);
			showXMPUpdate.addActionListener(e -> {
				xmpUpdate.setVisible(showXMPUpdate.isSelected());
				mainWindow.revalidate();
				mainWindow.repaint();
			});
			
			JCheckBoxMenuItem  hideFinishedTickets = new JCheckBoxMenuItem ("Hide Finished Tickets");
			showXMPUpdate.setSelected(false);
			showXMPUpdate.addActionListener(e -> {
				logDialogScroll.hideFinishedTickets(hideFinishedTickets.isSelected());
			});

			add(showLogDialog);
			add(showXMPUpdate);
			addSeparator();
			add(hideFinishedTickets);
		}
	}

	public class HelpMenu extends JMenu {
		private static final long serialVersionUID = 4975463837004809455L;

		public HelpMenu() {
			super("Help");

			JMenuItem update = new JMenuItem("Check for Updates");
			update.addActionListener(e -> {
				Tools.setComponentLocationFromParent(parent, makeUpdateDialog(), true);
			});

			add(update);
		}

		private JDialog makeUpdateDialog() {
			JDialog updateDialog = new JDialog();

			updateDialog.setIconImages(Tools.imageIcon());
			updateDialog.setLayout(new BorderLayout());
			updateDialog.setLocationByPlatform(true);
			updateDialog.setTitle("Update Dialog");
			updateDialog.setModal(true);

			JLabel updateLabel;

			if(Tools.doesUpdateExist()) {
				updateLabel = new JLabel("An Update is Available");

				updateDialog.add(makeUpdateButton(updateLabel), BorderLayout.SOUTH);
			} else {
				updateLabel = new JLabel("No Update is Available");
			}

			updateLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

			updateDialog.add(updateLabel, BorderLayout.CENTER);
			updateDialog.pack();

			return updateDialog;
		}

		private JButton makeUpdateButton(JLabel updateLabel) {
			JButton updateButton = new JButton("Download Update");

			updateButton.setBorder(BorderFactory.createEmptyBorder(7, 15, 7, 15));
			updateButton.setFocusPainted(false);
			updateButton.addActionListener(f -> {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI(
								"https://github.com/alexporrello/DesktopPublisherAssistant/releases"));
					} catch (IOException | URISyntaxException e1) {
						updateLabel.setText("Update Page Could Not Be Opened.");
					}
				}
			});

			return updateButton;
		}
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
			
			//===========================================================================
			
//			JMenu wdt = new JMenu("Working Directory Tools");
//			
//			JMenuItem wd = new JMenuItem("Create Working Directory");
//			
//			JMenuItem fps = new JMenuItem("Copy Print Spec to Working Directory");
//			
//			JMenuItem fdpc = new JMenuItem("Copy Doc Pro Checklist to Working Directory");
//			
//			wdt.add(wd);
//			wdt.add(fps);
//			wdt.add(fdpc);
//			
//			add(wdt);
//			
//			addSeparator();
			
			//ctrl+shift+W
			//ctrl+shift+P
			//ctrl+shift+D

			//===========================================================================

			JMenuItem dpc = new JMenuItem("Copy Doc Pro Checklist");
			dpc.addActionListener(e -> {
				openCopyDialog("DocProChecklist.pdf");
			});
			JMenuItem psd = new JMenuItem("Copy Doc Pro Print Spec Doc");
			psd.addActionListener(e -> {
				openCopyDialog("TCIS-Print-Specification_Template.pdf");
			});

			add(dpc);
			add(psd);
			
			addSeparator();

			//===========================================================================

			JMenuItem sc = new JMenuItem("Copy Silkscreen Checklist");
			sc.addActionListener(e -> {
				openCopyDialog("Silkscreen_Checklist.pdf");
			});
			JMenuItem st = new JMenuItem("Copy Silkscreen Print Spec Doc");
			st.addActionListener(e -> {
				openCopyDialog("Silkscreen-Print-Specification_Template.pdf");
			});

			add(sc);
			add(st);

			//===========================================================================

			addSeparator();

			JMenuItem copyTemplate = new JMenuItem("Copy Print Template Perforce Path to Clipboard");
			copyTemplate.addActionListener(e -> {
				StringSelection stringSelection = new StringSelection("//TechComm/Templates/Print Templates");
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			});
			add(copyTemplate);

			//===========================================================================

			addSeparator();

			JCheckBoxMenuItem clipboardListener = new JCheckBoxMenuItem("Start Clipboard Listener (Beta)");
			clipboardListener.addActionListener(e -> {
				//clipboardListener.setSelected(!clipboardListener.isSelected());

				if(clipboardListener.isSelected()) {
					mainWindow.startClipboardListener();
				} else {
					mainWindow.stopClipboardListener();
				}
			});
			clipboardListener.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_DOWN_MASK |Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			clipboardListener.setSelected(false);
			add(clipboardListener);
		}

		private void openCopyDialog(String resourceName) {		
			new CopyFileToURLDialog(resourceName);
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

		private class CopyFileToURLDialog extends JDialog {
			private static final long serialVersionUID = -2594379393111954934L;

			CopyFileToURLDialog(String resourceName) {
				setLayout(new GridBagLayout());

				JMButton browse = new JMButton("  Browse  ");
				JLabel copyTo = new JLabel("Copy Location:");
				JMTextField copyURL = new JMTextField();
				JMButton goButton = new JMButton("Copy Resource");

				add(copyTo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, new Insets(4, 4, 4, 4), 0, 0));
				add(copyURL, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(4, 0, 4, 4), 0, 0));
				add(browse, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(4, 0, 4, 4), 0, 0));
				add(goButton, new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 4, 4, 4), 0, 0));

				browse.addActionListner(e -> {
					try {
						String text = browse(resourceName);
						copyURL.setText(text);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
				goButton.addActionListner(e -> {
					String url = copyURL.getText();

					if(new File(url).exists()) {
						try {
							Tools.copyOutResource(resourceName, url + "\\" + resourceName);
							this.dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				setTitle("Resource Copying Dialog");
				setSize(300, 100);
				setIconImages(Tools.imageIcon());
				setModal(true);
				Tools.setComponentLocationFromParent(parent, this, true);
			}
		}

		private String browse(String resourceName) throws Exception {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setDialogTitle("Choose a directory to save " + resourceName);
			Tools.setComponentLocationFromParent(parent, jfc, true);

			if(jfc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {				
				if (jfc.getSelectedFile().isDirectory()) {
					return jfc.getSelectedFile().getAbsolutePath();
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		}
	}

	public class FileMenu extends JMenu {
		private static final long serialVersionUID = -754503798159404299L;

		/** Saves the currently open project to a .log file **/
		JMenuItem saveToLog = new JMenuItem("Save Ticket");

		/** Opens a ticket project and loads it into the application **/
		JMenuItem open = new JMenuItem("Open Ticket");

		JMenuItem search = new JMenuItem("Search through tickets...");

		FileMenu() {
			super("File");

			add(open());
			add(save());

			addSeparator();

			add(search());
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
				JFileChooser jfc = new JFileChooser(Ticket.TICKET_URL);
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setDialogTitle("Choose the desired ticket to load it into the application.");

				if(jfc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					try {
						Ticket.readLogFile(jfc.getSelectedFile().getAbsolutePath(), mainWindow);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}

				Tools.setComponentLocationFromParent(parent, jfc, true);
			});
			open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			return open;
		}

		public JMenuItem search() {
			search.addActionListener(e -> {
				logDialogScroll.search.setVisible(!logDialogScroll.search.isVisible());
				logDialogScroll.search.getTextField().requestFocus();
				logDialogScroll.revalidate();
				logDialogScroll.repaint();
			});
			search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			return search;
		}
	}
}
