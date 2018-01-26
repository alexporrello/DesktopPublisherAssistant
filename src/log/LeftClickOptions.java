package log;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class LeftClickOptions extends JPopupMenu {
	private static final long serialVersionUID = -4929695537157281490L;

	public JMenuItem openH  = new JMenuItem("Open in Current Window");
	public JMenuItem open   = new JMenuItem("Open in New Window");
	public JMenuItem delete = new JMenuItem("Delete Ticket");

	public LeftClickOptions(String ticketURL){
		add(setupMenuItem(openH));

		addSeparator();

		add(setupMenuItem(delete));
	}
	
	public JMenuItem setupMenuItem(JMenuItem tosetup) {
		tosetup.setBorder(BorderFactory.createEmptyBorder(-4,-5,-2,-5));
		
		return tosetup;
	}
}
