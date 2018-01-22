package ui;

import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class Tools {

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
}
