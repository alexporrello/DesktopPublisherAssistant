package jm;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/** Responsible for the color fading animation **/
public class ColorFader {
	
	Timer fadeTimer;
	Color fadeFrom;
	Color fadeTo;
	Component component;
	
	public void fadeColor(Component component, Color fadeFrom, Color fadeTo, int fadeSpeed, Timer fadeTimer) {
		
		this.component = component;
		this.fadeFrom = fadeFrom;
		this.fadeTo = fadeTo;
		this.fadeTimer = fadeTimer;
		
		createTimer(fadeSpeed);
	}
	
	private void createTimer(int fadeSpeed) {
		if(fadeTimer != null && fadeTimer.isRunning()) {
			fadeTimer.stop();
		}

		fadeTimer = new Timer(fadeSpeed, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				colorFader(fadeFrom, fadeTo);
				component.repaint();
			}    
		});

		fadeTimer.start();
	}

	private void colorFader(Color buttonfadeFrom, Color buttonFadeTo) {		
		int[] buttonA = makeRGBArray(buttonfadeFrom);
		int[] buttonB = makeRGBArray(buttonFadeTo);

		buttonA = fader(buttonA, buttonB);

		fadeFrom = arrayToColor(buttonA);

		if((buttonA[0] + "" + buttonA[1] + "" + buttonA[2]).equals(buttonB[0] + "" + buttonB[1] + "" + buttonB[2])) {
			component.repaint();
			fadeTimer.stop();
		}
	}

	/**
	 * Adds or subtracts 1 so that values of a will equal values of b.
	 * @param a the array whose values are changed
	 * @param b the array whose values are static
	 * @return an updated
	 */
	private int[] fader(int[] a, int[] b) {
		for(int i = 0; i < 3; i++) {
			if(a[i] > b[i]) {
				a[i] = a[i]-1;
			} else if(a[i] < b[i]) {
				a[i] = a[i]+1;
			}
		}

		return a;
	}

	/**
	 * Receives a color and returns the color represented as an array of integers.
	 * @param color the color to be converted
	 * @return an array of size three where...
	 * <ul>
	 * 		<li>0: Red</li>
	 *      <li>1: Green</li>
	 *      <li>2: Blue</li>
	 * </ul>
	 */
	private int[] makeRGBArray(Color color) {
		return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
	}

	/**
	 * Receives an array representation of a color and returns a color.
	 * @param rgb accepts array of integers made in {@link #makeRGBArray(Color)}.
	 * @return the Color represented by rgb.
	 */
	private Color arrayToColor(int[] rgb) {
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
}
