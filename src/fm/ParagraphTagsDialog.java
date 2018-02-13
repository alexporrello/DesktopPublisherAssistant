package fm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import jm.JMPanel;
import ui.Tools;

public class ParagraphTagsDialog extends JDialog {
	private static final long serialVersionUID = -2454385043003246573L;
	
	ArrayList<String> manual;
	ArrayList<String> shortDoc;
	ArrayList<String> courseManual;
	
	JMPanel tagPanel = new JMPanel();
	
	JComboBox<ParagraphTag> tagType = new JComboBox<ParagraphTag>(ParagraphTag.TAG_TYPES);
	
	public ParagraphTagsDialog() {
		loadTags();
		
		add(tagType, BorderLayout.NORTH);		
		tagType.addActionListener(e -> {
			createUI((ParagraphTag) tagType.getSelectedItem());
		});
		
		tagPanel.setLayout(new GridBagLayout());
		createUI(ParagraphTag.MANUAL);
		
		JScrollPane scroll = new JScrollPane(tagPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new JLabel().getBorder());
		add(scroll, BorderLayout.CENTER);
		
		setIconImages(Tools.imageIcon());
		setSize(new Dimension(500, 200));
		setLocationByPlatform(true);
		setTitle("Paragraph Tags");
	}
	
	private void createUI(ParagraphTag tagType) {
		tagPanel.removeAll();
		
		ArrayList<String> toShow;
		
		if(tagType == ParagraphTag.MANUAL) {
			toShow = manual;
		} else if(tagType == ParagraphTag.SHORT_DOC) {
			toShow = shortDoc;
		} else {
			toShow = courseManual;
		}
		
		int y = 0;
		
		for(String s : toShow) {
			String[] s2 = s.split("##");
			
			JLabel toAdd1 = new JLabel(s2[0].replace("*", "."));
			toAdd1.setOpaque(true);
			
			JLabel toAdd2 = new JLabel(s2[1]);
			toAdd2.setOpaque(true);
			
			if(s.contains("***")) {
				toAdd1.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 10));
			} else if(s.contains("**")) {
				toAdd1.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 10));
			} else if(s.contains("*")) {
				toAdd1.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
			} else {
				toAdd1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			}
			
			if(y%2 == 0) {
				toAdd1.setBackground(Color.WHITE);
				toAdd2.setBackground(Color.WHITE);
			}
			
			tagPanel.add(toAdd1, Tools.createGBC(0, y, 1.0, new Insets(0, 0, 0, 0)));
			tagPanel.add(toAdd2, Tools.createGBC(1, y, 1.0, new Insets(0, 0, 0, 0)));
			y++;
		}
		
		tagPanel.revalidate();
		tagPanel.repaint();
	}
	
	private void loadTags() {
		manual       = Tools.readInPlainTextFile(getClass().getResource("../paragraph-tags_manual.txt").getFile());
		shortDoc     = Tools.readInPlainTextFile(getClass().getResource("../paragraph-tags_short-doc.txt").getFile());
		courseManual = Tools.readInPlainTextFile(getClass().getResource("../paragraph-tags_course-manual.txt").getFile());
	}
}
