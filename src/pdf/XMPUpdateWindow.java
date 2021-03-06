package pdf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import com.itextpdf.text.DocumentException;

import jm.JMButton;
import jm.JMColor;
import jm.JMLabel;
import ui.Tools;

public class XMPUpdateWindow extends JPanel {
	private static final long serialVersionUID = -6561591979054929701L;

	/** The path to the PDF File **/
	File pdfFile;

	/** The path to the XMP File **/
	File xmpFile;

	/** The path to the PDF output folder **/
	File outFile;

	/** The label on which the PDF Path will be displayed **/
	JMLabel pdf = new JMLabel("Locate PDF File...", SwingConstants.LEFT);

	/** The label on which the XMP Path will be displayed **/
	JMLabel xmp = new JMLabel("Locate XMP File...", SwingConstants.LEFT);

	/** The label on which the XMP Path will be displayed **/
	JMLabel out = new JMLabel("Locate Output Folder...", SwingConstants.LEFT);

	/** The button that kicks off the action **/
	JMButton go = new JMButton("Update");

	/** When selected, the initial view properties of the PDF are updated along with the tags **/
	JRadioButton rb = new JRadioButton("Update PDF Initial View Properties");

	Boolean showOutput = false;

	public XMPUpdateWindow() {
		setBackground(JMColor.DEFAULT_BACKGROUND);
		setPreferredSize(new Dimension(300, 50));
		setLayout(new BorderLayout());
		
		add(setUpDropArea(), BorderLayout.CENTER);
		add(setUpLowerPanel(), BorderLayout.SOUTH);

		rb.setOpaque(false);
		rb.setFocusPainted(false);
		rb.setSelected(true);
		rb.setForeground(JMColor.DEFAULT_FONT_COLOR);
	}

	public XMPUpdateWindow(Boolean showOutput) {
		this.showOutput = showOutput;

		setPreferredSize(new Dimension(300, 50));
		setLayout(new BorderLayout());

		add(setUpDropArea(), BorderLayout.CENTER);
		add(setUpLowerPanel(), BorderLayout.SOUTH);

		rb.setFocusPainted(false);
		rb.setSelected(true);
	}

	/**
	 * Sets up the area where files can be dropped.
	 * @return a setup JLabel to be added to the gui.
	 */
	public JLabel setUpDropArea() {
		JLabel display = new JLabel("Drag and Drop Your PDF And/Or Your XMP Here") {
			private static final long serialVersionUID = -5684161364815869904L;

			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(JMColor.DEFAULT_BACKGROUND);
				g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 5, 5);

				g2.setColor(Color.LIGHT_GRAY);
				g2.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 5, 5);

				super.paintComponent(g);
			}
		};
		display.setHorizontalAlignment(SwingConstants.HORIZONTAL);
		display.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
		display.setPreferredSize(new Dimension(300, 75));
		display.setForeground(JMColor.DEFAULT_FONT_COLOR);
		setDropTarget(new DropTarget(display, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent e) {
				try {
					Transferable t = e.getTransferable();
					int d = e.getDropAction();
					e.acceptDrop(d);

					String url = t.getTransferData(DataFlavor.javaFileListFlavor).toString();
					url = url.substring(1, url.length()-1);

					e.dropComplete(true);

					String[] urls;

					if(url.contains(",")) {						
						url = url.replace(".pdf, ", ".pdf###");
						url = url.replace(".xmp, ", ".xmp###");

						urls = url.split("###");
					} else {
						urls = url.split(", ");
					}

					for(String s : urls) {
						isURLValid(s);
					}
				} catch (UnsupportedFlavorException | IOException e1) {
					e1.printStackTrace();
				}
			}
		}));

		return display;
	}

	private void isURLValid(String url) {
		if(url.endsWith(".xmp")) {
			xmpFile = new File(url);

			xmp.setText("XMP:  " + url);
			xmp.setForeground(JMColor.DEFAULT_FONT_COLOR);
			xmp.setToolTipText(url);
		} else if(url.endsWith(".pdf")) {
			pdfFile = new File(url);

			pdf.setText("PDF:  " + url);
			pdf.setForeground(JMColor.DEFAULT_FONT_COLOR);
			pdf.setToolTipText(url);
		} else if(showOutput) {
			outFile = new File(url);

			out.setText("Output: " + url);
			out.setForeground(JMColor.DEFAULT_FONT_COLOR);
			out.setToolTipText(url);
		}

		enableGoButton();
	}

	/**
	 * Sets up the JLabels that display the paths and the action button.
	 * @return a setup JPanel to be added to the project.
	 */
	private JPanel setUpLowerPanel() {
		JPanel toReturn = new JPanel();
		toReturn.setOpaque(false);
		
		toReturn.setLayout(new GridBagLayout());
		toReturn.add(pdf, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 5, 5));	
		toReturn.add(xmp, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 5));	

		if(showOutput) {
			toReturn.add(out, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 5, 5));
		}

		toReturn.add(rb,  new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 5, 5));
		toReturn.add(go,  new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 5, 5));	

		setupGoButton();
		setupJLabel(pdf, true, false);
		setupJLabel(xmp, false, false);
		setupJLabel(out, false, true);

		pdf.setForeground(JMColor.DISABLED_FONT_COLOR);
		xmp.setForeground(JMColor.DISABLED_FONT_COLOR);
		out.setForeground(JMColor.DISABLED_FONT_COLOR);

		go.setButtonEnabled(false);

		return toReturn;
	}

	/**
	 * Sets up an individual JLabel.
	 * @param label the label to set up
	 * @param isPDF true if the selection will be for PDF; else, false.
	 */
	private void setupJLabel(JMLabel label, boolean isPDF, boolean isOUT) {
		String dialogName;

		if(isPDF) {
			dialogName = "Locate PDF File";
		} else if(isOUT) {
			dialogName = "Locate Output Folder";
		} else {
			dialogName = "Locate XMP File";
		}

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if(!isOUT) {
						isURLValid(Tools.loadFile(dialogName, FileSystemView.getFileSystemView().getHomeDirectory()));
					} else {
						isURLValid(Tools.loadDirectory(dialogName, FileSystemView.getFileSystemView().getHomeDirectory()));
					}
				} catch (NoSuchFileException e) {
					System.err.println("The user did not open a file.");
				}
			}
		});	
	}

	/**
	 * Sets up the button that kicks off the action.
	 */
	private void setupGoButton() {
		go.addActionListner(e -> {
			try {
				if(!showOutput) {
					XMPUpdater.updatePDFXMP(pdfFile.getAbsolutePath(), xmpFile.getAbsolutePath());
					
					if(rb.isSelected()) {
						PDFPropertiesUpdater.updateOpenProperties(pdfFile.getAbsolutePath());
					}
				} else {
					XMPUpdater.updatePDFXMP(pdfFile.getAbsolutePath(), outFile.getAbsolutePath() + "//" + pdfFile.getName(),
							xmpFile.getAbsolutePath());
				}

				
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		});
	}

	/**
	 * Checks if the go button should be enabled to disabled.
	 */
	private void enableGoButton() {
		if((xmpFile != null && pdfFile !=null) && (xmpFile.exists() && pdfFile.exists())) {			
			if(showOutput) {
				go.setButtonEnabled(outFile != null && xmpFile.exists());
			} else {
				go.setButtonEnabled(true);
			}
		}
	}

	/**
	 * Resets the window to its default state.
	 */
	public void reset() {
		xmpFile = null;
		pdfFile = null;

		pdf.setText("Locate PDF File...");
		xmp.setText("Locate XMP File...");
		out.setText("Locate Output Folder...");

		enableGoButton();
	}
}
