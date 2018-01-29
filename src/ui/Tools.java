package ui;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Tools {

	/** The URL where the current version number is stored **/
	public static final String ONLINE_VERSION_URL = "https://raw.githubusercontent.com/alexporrello/DesktopPublisherAssistant/master/resources/version-history.txt";

	/**
	 * Checks for an update by comparing version-history.txt in the local .jar to the version
	 * history maintained on GitHub.
	 * @return true if there is an update; else, false.
	 */
	public static Boolean doesUpdateExist() {
		try {
			Scanner scanner = new Scanner(new URL(Tools.ONLINE_VERSION_URL).openStream());
			String onlineVersion = scanner.nextLine();
			scanner.close();

			String topLine = new BufferedReader(new InputStreamReader(
					Tools.class.getClassLoader().getResourceAsStream("version-history.txt"))).readLine();
			
			return !onlineVersion.equals(topLine);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Sets a JDialog's position to be exactly centered in its parent JFrame's position.
	 * @param parent the JDialog's parent JFrame.
	 * @param dialog the JDialg to be centered.
	 * @param setVisible determines if the JDialog be set visible after its location is updated.
	 */
	public static void setDialogLocationFromParent(JFrame parent, JDialog dialog, Boolean setVisible) {
		int parentX = parent.getX();
		int parentY = parent.getY();

		int parentW = parent.getWidth();
		int parentH = parent.getHeight();

		int dialogW = dialog.getWidth();
		int dialogH = dialog.getHeight();

		dialog.setLocation(parentX + ((parentW/2)-(dialogW/2)), parentY + ((parentH/2)-(dialogH/2)));
		dialog.setVisible(true);
	}

	/**
	 * Creates a GridBagConstraint given the following parameters:
	 * @param x the constraint's x position
	 * @param y the constraint's y position
	 * @param weightx the constraint's x weight
	 * @param insetRight the constraint's right inset
	 * @return a GridBagConstraint object
	 */
	public static GridBagConstraints createGBC(int x, int y, Double weightx, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.fill  = GridBagConstraints.BOTH;
		gbc.weightx = weightx;
		gbc.weighty = 0.0;
		gbc.insets  = insets;
		gbc.anchor = GridBagConstraints.NORTH;

		return gbc;
	}

	/**
	 * Checks if a given JTextField has any inputted text.
	 * @param text the JTextField to check 
	 * @return true if the field is empty; else, false.
	 */
	public static boolean isEmpty(JTextField text) {
		return (text.getText().length() == 0);
	}

	/**
	 * Appends a new line of text with a return to an original line of text.
	 * @param original the text to which the new line will be appended
	 * @param newLine the line that will be appended to the original text
	 * @return a new String to which the new line has been appended
	 */
	public static String appendToNewLine(String original, String newLine) {
		return original + "\n" + newLine;
	}

	/** Creates the image icons that will be displayed on the app's taskbar **/
	public static ArrayList<Image> imageIcon() {
		ArrayList<Image> icons = new ArrayList<Image>();

		icons.add(loadImage("NI-Icon_128x128.png"));
		icons.add(loadImage("NI-Icon_64x64.png"));
		icons.add(loadImage("NI-Icon_48x48.png"));
		icons.add(loadImage("NI-Icon_32x32.png"));
		icons.add(loadImage("NI-Icon_24x24.png"));
		icons.add(loadImage("NI-Icon_16x16.png"));

		return icons;
	}

	private static Image loadImage(String url) {		
		return new ImageIcon(Tools.class.getClassLoader().getResource(url)).getImage();
	}

	public static void writeToFile(String url, String content) {
		try {
			FileWriter     fw = new FileWriter(url);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Loads a plain text file and returns an ArrayList<String> of its content.
	 * @param url the URL of the plain text file.
	 * @return an ArrayList<String>, each of which is a line in the plain text file.
	 */
	public static ArrayList<String> loadPlainTextFile(String url) {
		ArrayList<String> toReturn = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(url), "UTF-8"));

			String thisLine;

			while ((thisLine = br.readLine()) != null) {
				toReturn.add(thisLine);
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return toReturn;
	}

	/**
	 * Opens a new JFileChooser so the user can open a file.
	 * @return the URL of the selected file as a string
	 * @throws NoSuchFileException if the user does not choose a file.
	 */
	public static String loadFile(String name, File openToURL) throws NoSuchFileException {
		JFileChooser jfc = new JFileChooser(openToURL);

		jfc.setDialogTitle(name);

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}

		throw new NoSuchFileException("No file was selected.");
	}

	/**
	 * Gets text that was copied by the user.
	 * @return a string of the copied text.
	 */
	public static String getCopiedText() {
		try {
			return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);	 
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			System.err.println("The current contents of the clipboard could not be auto-pasted.");
		}

		return "";
	}
	
	/**
	 * Copies a resource from inside the jar outside of the jar.
	 * @param resourceName the name of the file in the jar.
	 * @param output the location to which the file will be copied.
	 * @throws IOException
	 */
	public static void copyOutResource(String resourceName, String output) throws IOException {
		if(!new File(output).exists()) {
			new File(output).mkdirs();
		}
		
		Files.copy(
				Tools.class.getClassLoader().getResourceAsStream(resourceName), 
				new File(output).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
