package ui;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JTextLink extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextLink(String setText) {
		super(setText, " > ");
	}

	@Override
	public void addMouseReleasedListener() {
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(link.contains(arg0.getPoint()) && label.getText().startsWith("https")) {
					try {
						Desktop.getDesktop().browse(new URI(label.getText()));
					} catch (IOException | URISyntaxException e) {
						System.err.println("URL could not be opened: " + label.getText());
					}
				}
			}
		});
	}
}