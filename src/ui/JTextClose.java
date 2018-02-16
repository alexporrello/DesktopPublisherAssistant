package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTextClose extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextClose(String text) {
		super(text, " × ", false);
	}
	
	@Override
	public void addMouseReleasedListener() {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(button.contains(arg0.getPoint())) {
					setVisible(false);
				}
			}
		});
	}
}