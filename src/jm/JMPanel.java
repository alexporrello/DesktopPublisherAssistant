package jm;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class JMPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = -2360363423927535856L;

	public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
		return 10;
	}

	public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
		return visibleRect.width;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
}