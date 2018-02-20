package ui;

public class JTextClose extends JTextButton {
	private static final long serialVersionUID = -2231714098377893220L;

	public JTextClose(String text) {
		super(text, " × ", false);
		
		
	}

	@Override
	public void buttonAction() {
		setVisible(false);
	}
}