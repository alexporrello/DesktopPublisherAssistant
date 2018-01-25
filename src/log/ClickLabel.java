package log;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * A label that, when clicked, cycles through a set of options. (Basically, a fancy
 * version of a JComboBox<E>.
 * 
 * @author Alexander Porrello
 * @param <E> the type of item that will be displayed.
 */
public class ClickLabel<E> extends JLabel {
	private static final long serialVersionUID = 6596554469604925403L;

	/** The items that are to be displayed **/
	private E[] items;

	/** The item that is currently displayed **/
	private int itemDisplayed = 0;

	public ClickLabel(E[] items) {
		this.items = items;
		
		setText(items[itemDisplayed] + "");
		addMouseListener();
		setOpaque(true);
		
	}
	
	/**
	 * 
	 * @param items
	 * @param layout
	 */
	public ClickLabel(E[] items, int layout) {		
		this.items = items;
		
		setText(items[itemDisplayed] + "");
		setHorizontalAlignment(layout);
		addMouseListener();
		setOpaque(true);
	}
	
	private void addMouseListener() {
		addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(itemDisplayed < items.length-1) {
					setSelectedIndex(itemDisplayed + 1);
				} else {
					setSelectedIndex(0);
				}
				
				System.out.println(getSize());
			}
		});
	}

	/**
	 * Sets the selected index to a given value.
	 * @param i the index to be set as selected.
	 */
	public void setSelectedIndex(int i) {
		if(i < items.length && i >= 0) {
			itemDisplayed = i;
			setText(items[itemDisplayed] + "");
		} else {
			throw new ArrayIndexOutOfBoundsException(i);
		}
	} 

	/**
	 * @return The currently selected index.
	 */
	public int getSelectedIndex() {
		return itemDisplayed;
	}
}