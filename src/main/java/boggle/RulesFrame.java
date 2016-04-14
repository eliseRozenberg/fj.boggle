package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RulesFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		RulesFrame frame;
		try {
			frame = new RulesFrame(new StartFrame());
			frame.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Border borderMenu, borderRight, borderExited;
	private JPanel buttonPanel;
	private int color;
	private Font font1, font2;
	private ImageIcon menu1, menu2;
	private JLabel menuButton, leftButton, rightButton, circleLabel, pix,
			heading1;

	private ImageIcon[] pictures;
	private int position;

	private StartFrame startFrame;

	@Inject
	public RulesFrame(StartFrame frame) {
		setTitle("HOW TO PLAY!");
		setSize(1000, 1000);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg"))
				.getImage());
		setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startFrame = frame;

		position = 0;
		color = 1;
		pictures = new ImageIcon[10];
		createImages();

		addFormat();
		addListeners();
	}

	private void addFormat() {

		font1 = new Font("Rockwell Extra Bold", Font.PLAIN, 80);
		font2 = new Font("Rockwell Extra Bold", Font.PLAIN, 100);

		borderMenu = BorderFactory.createMatteBorder(0, 0, 8, 0, Color.white);
		borderRight = BorderFactory.createMatteBorder(0, 10, 0, 0, Color.white);
		borderExited = new LineBorder(Color.white, 0, true);

		heading1 = new JLabel("HOW TO PLAY");
		heading1.setFont(font1);
		heading1.setForeground(Color.blue);

		pix = new JLabel(pictures[0]);
		pix.setLayout(new BorderLayout());
		pix.setBackground(Color.white);

		leftButton = new JLabel();
		leftButton.setIcon(new ImageIcon(formatIcon(100, 100, getClass()
				.getResource("/left.png"))));
		leftButton.setPreferredSize(new Dimension(100, 100));
		leftButton.setBackground(Color.white);
		leftButton.setBorder(borderRight);

		rightButton = new JLabel();
		rightButton.setPreferredSize(new Dimension(100, 100));
		rightButton.setIcon(new ImageIcon(formatIcon(100, 100, getClass()
				.getResource("/right.png"))));
		rightButton.setBackground(Color.white);

		circleLabel = new JLabel();
		circleLabel.setIcon(new ImageIcon(formatIcon(65, 65, getClass()
				.getResource("/circle2.jpg"))));
		circleLabel.setPreferredSize(new Dimension(100, 100));
		circleLabel.setBackground(Color.white);

		menu1 = new ImageIcon(formatIcon(180, 140,
				getClass().getResource("/menu.png")));
		menu2 = new ImageIcon(formatIcon(180, 140,
				getClass().getResource("/menu2.JPG")));

		menuButton = new JLabel();
		menuButton.setPreferredSize(new Dimension(180, 150));
		menuButton.setIcon(menu2);
		menuButton.setBackground(Color.white);

		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(menuButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		buttonPanel.add(leftButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(32, 0)));
		buttonPanel.add(circleLabel);
		buttonPanel.add(rightButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(280, 0)));
		pix.add(heading1, BorderLayout.PAGE_START);
		add(buttonPanel, BorderLayout.SOUTH);
		add(pix, BorderLayout.NORTH);

	}

	private void addListeners() {
		menuButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
				menuButton.setBorder(borderMenu);
				menuButton.setIcon(menu1);
				startFrame.playClickSound();
			}

			public void mouseExited(MouseEvent arg0) {
				menuButton.setBorder(borderExited);
				menuButton.setIcon(menu2);
			}

			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {
				setVisible(false);
				position = 0;
				pix.setIcon(pictures[position]);
				startFrame.setVisible(true);
			}
		});

		leftButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
				startFrame.playClickSound();
				leftButton.setBorder(borderExited);
			}

			public void mouseExited(MouseEvent arg0) {
				leftButton.setBorder(borderRight);
			}

			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {
				if (position == 0) {
					position = pictures.length - 1;
				} else {
					--position;
				}
				pix.setIcon(pictures[position]);

			}
		});

		rightButton.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {

			}

			public void mouseEntered(MouseEvent arg0) {
				rightButton.setBorder(borderRight);
				startFrame.playClickSound();
			}

			public void mouseExited(MouseEvent arg0) {
				rightButton.setBorder(borderExited);
			}

			public void mousePressed(MouseEvent arg0) {
				startFrame.playClickSound();
			}

			public void mouseReleased(MouseEvent arg0) {
				if (position == (pictures.length - 1)) {
					position = 0;
				} else {
					++position;
				}
				pix.setIcon(pictures[position]);
			}
		});

		heading1.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
				changeColor();
				heading1.setFont(font2);
			}

			public void mouseExited(MouseEvent arg0) {
				changeColor();
				heading1.setFont(font1);
			}

			public void mousePressed(MouseEvent arg0) {
				startFrame.playClickSound();
			}

			public void mouseReleased(MouseEvent arg0) {
				changeColor();
			}
		});
	}

	public void changeColor() {
		switch (color++) {
		case 1:
			heading1.setForeground(Color.green);
			break;
		case 2:
			heading1.setForeground(Color.yellow);
			break;
		case 3:
			heading1.setForeground(Color.red);
			break;
		case 4:
			heading1.setForeground(Color.blue);
			color = 1;
			break;
		}
	}

	public void createImages() {
		for (int i = 0; i < pictures.length; i++) {
			pictures[i] = new ImageIcon(formatIcon(getWidth(),
					getHeight() - 190,
					getClass().getResource("/pix" + (i + 1) + ".JPG")));
		}
	}

	public Image formatIcon(int width, Integer height, URL image) {
		ImageIcon icon = new ImageIcon(image);
		Image img = icon.getImage().getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		return img;
	}
}
