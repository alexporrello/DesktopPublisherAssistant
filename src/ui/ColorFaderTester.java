package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import jm.JMButton;
import jm.JMColor;

public class ColorFaderTester extends JFrame {
	private static final long serialVersionUID = -705447297687688732L;

	Panel p = new Panel();

	public ColorFaderTester() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 535));
		setIconImages(Tools.imageIcon());
		setLayout(new BorderLayout());
		setLocationByPlatform(true);
		add(p, BorderLayout.CENTER);

		JMButton button = new JMButton("Click Me!");
		button.addActionListner(e -> {
			p.timer.start();
		});
		add(button, BorderLayout.NORTH);

		setVisible(true);
		
		//p.timer.start();
	}

	public class Panel extends JPanel {
		private static final long serialVersionUID = 2203473761436651087L;

		Color colorA = JMColor.PRESS_COLOR;
		Color colorB = JMColor.DEFAULT_BACKGROUND;

		Timer timer;

		public Panel() {
			timer = new Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					repaint();
					
					int[] a = {colorA.getRed(), colorA.getGreen(), colorA.getBlue()};
					int[] b = {colorB.getRed(), colorB.getGreen(), colorB.getBlue()};

					for(int i = 0; i < 3; i++) {
						if(a[i] > b[i]) {
							a[i] = a[i]-1;
						} else if(a[i] < b[i]) {
							a[i] = a[i]+1;
						}
					}
					
					colorA = new Color(a[0], a[1], a[2]);

					if(!(a[0] + "" + a[1] + "" + a[2]).equals(b[0] + "" + b[1] + "" + b[2])) {
						colorA = new Color(a[0], a[1], a[2]);
					} else {
						timer.stop();
					}
				}    
			});
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(colorA);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
