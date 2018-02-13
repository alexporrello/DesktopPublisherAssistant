package jm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import ui.Tools;

public class JMScrollPane extends JScrollPane {
	private static final long serialVersionUID = -5458523733486110315L;

	private Color mousePressedColor = Color.decode("#606060");
	private Color mouseOverColor    = Color.decode("#a6a6a6");
	private Color mouseOffColor     = Color.decode("#cdcdcd");

	private Color scrollThumbColor = Color.decode("#e2e2e2");
	private Color scrollBackground = Color.decode("#f0f0f0");

	private Boolean isMousePressed = false;

	private int drawWidth = Tools.SCROLL_BAR_WIDTH;

	public JMScrollPane(JComponent c) {
		super(c);

		setUpPanel();
	}

	private void setUpPanel() {
		setBorder(BorderFactory.createEmptyBorder());
		setScrollBarWidth(drawWidth);

		setScrollBarUI();
	}

	private void setScrollBarUI() {
		super.getHorizontalScrollBar().setUI(horizontal());
		super.getVerticalScrollBar().setUI(vertical());
	}
	
	private BasicScrollBarUI horizontal() {
		return new BasicScrollBarUI() {
			@Override
			protected JButton createDecreaseButton(int orientation) {
				return makeEmptyButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return makeEmptyButton();
			}

			@Override 
			protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
				g.setColor(scrollBackground);
				g.fillRect(0, 0, getWidth(), getHeight());
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

				int leftBounds  = thumbBounds.x;
				int rightBounds = thumbBounds.x + thumbBounds.width;				

				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseExited(MouseEvent e) {
						if(isMousePressed) {
							scrollThumbColor = mousePressedColor;
						} else {
							scrollThumbColor = mouseOffColor;
						}

						repaint();
					}

					@Override
					public void mousePressed(MouseEvent e) {
						isMousePressed = true;

						if(e.getX() > leftBounds && e.getX() < rightBounds) {
							scrollThumbColor = mousePressedColor;
						}
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						isMousePressed = false;

						if(thumbBounds.contains(e.getPoint())) {
							scrollThumbColor = mouseOverColor;
						} else {
							scrollThumbColor = mouseOffColor;
						}

						repaint();
					}

				});

				c.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						if(e.getY() > leftBounds && e.getY() < rightBounds) {
							if(!isMousePressed) {
								scrollThumbColor = mouseOverColor;
							} 

							repaint();
						}else {
							if(!isMousePressed) {
								scrollThumbColor = mouseOffColor;
							} 
						}
					}
				});

				g.setColor(scrollThumbColor);
				g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, drawWidth);
			}

			@Override
			protected void configureScrollBarColors() {
				super.configureScrollBarColors();
			}
		};
	}

	private BasicScrollBarUI vertical() {
		return new BasicScrollBarUI() {
			@Override
			protected JButton createDecreaseButton(int orientation) {
				return makeEmptyButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return makeEmptyButton();
			}

			@Override 
			protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
				g.setColor(scrollBackground);
				g.fillRect(0, 0, getWidth(), getHeight());
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
				g.setColor(scrollThumbColor);

				int upperBounds = thumbBounds.y;
				int lowerBounds = thumbBounds.y + thumbBounds.height;				

				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseExited(MouseEvent e) {
						if(isMousePressed) {
							scrollThumbColor = mousePressedColor;
						} else {
							scrollThumbColor = mouseOffColor;
						}

						repaint();
					}

					@Override
					public void mousePressed(MouseEvent e) {
						isMousePressed = true;

						if(e.getY() > upperBounds && e.getY() < lowerBounds) {
							scrollThumbColor = mousePressedColor;
						}
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						isMousePressed = false;

						if(thumbBounds.contains(e.getPoint())) {
							scrollThumbColor = mouseOverColor;
						} else {
							scrollThumbColor = mouseOffColor;
						}

						repaint();
					}

				});

				c.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						if(e.getY() > upperBounds && e.getY() < lowerBounds) {
							if(!isMousePressed) {
								scrollThumbColor = mouseOverColor;
							} 

							repaint();
						}else {
							if(!isMousePressed) {
								scrollThumbColor = mouseOffColor;
							} 
						}
					}
				});

				g.fillRect(thumbBounds.x, thumbBounds.y, drawWidth, thumbBounds.height);
			}

			@Override
			protected void configureScrollBarColors() {
				super.configureScrollBarColors();
			}
		};
	}



	/**
	 * Used when the up and down arrows have a desired size of zero.
	 * @return a button whose size is 0 x 0.
	 */
	private JButton makeEmptyButton() {
		JButton jbutton = new JButton();

		jbutton.setPreferredSize(new Dimension(0, 0));
		jbutton.setMinimumSize(new Dimension(0, 0));
		jbutton.setMaximumSize(new Dimension(0, 0));

		return jbutton;
	}

	/**
	 * Sets the width of the scroll bar as visible to the user.
	 * @param drawWidth the desired width of the scroll bar
	 */
	private void setScrollBarWidth(int drawWidth) {
		this.drawWidth = drawWidth;

		this.getVerticalScrollBar().setPreferredSize(new Dimension(this.drawWidth, 0));
		this.getHorizontalScrollBar().setPreferredSize(new Dimension(0, this.drawWidth));

		this.revalidate();
		this.repaint();
	}
}
