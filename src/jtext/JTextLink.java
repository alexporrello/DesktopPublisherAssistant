package jtext;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JTextLink extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextLink(String setText) {
		super(setText, " > ");
	}

	@Override
	public void buttonAction() {
		try {
			Desktop.getDesktop().browse(new URI(getTextField().getText()));
		} catch (IOException | URISyntaxException e) {
			System.err.println("URL could not be opened: " + getTextField().getText());
		}
	}
}