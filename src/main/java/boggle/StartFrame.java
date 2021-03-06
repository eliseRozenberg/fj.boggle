package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class StartFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new BoggleModule());
		injector.getInstance(StartFrame.class).setVisible(true);
	}

	private BoggleFrame boggleFrame;
	private Color colorExited, colorEntered;
	private Container container;
	private Font font1, font2;
	private RulesFrame rulesFrame;
	private JLabel singleButton, doubleButton, rulesButton, quitbutton;

	private StartPanel startPanel;

	@Inject
	public StartFrame() throws IOException {

		setTitle("BOGGLE");
		setSize(600, 700);
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addFormat();
		addListeners();
	}

	public void adddLis(String name) {

	}

	public void addFormat() {
		container = getContentPane();
		container.setLayout(new BorderLayout());

		rulesFrame = new RulesFrame(this);
		boggleFrame = new BoggleFrame(this);
		startPanel = new StartPanel();

		colorExited = new Color(255, 255, 255);
		colorEntered = new Color(255, 255, 0);
		font1 = new Font("Berlin Sans FB", Font.PLAIN, 60);
		font2 = new Font("Berlin Sans FB", Font.PLAIN, 63);

		singleButton = new JLabel("          Single Player");
		singleButton.setPreferredSize(new Dimension(225, 40));
		singleButton.setForeground(colorExited);
		singleButton.setFont(font1);

		doubleButton = new JLabel("        Double Player");
		doubleButton.setPreferredSize(new Dimension(225, 40));
		doubleButton.setForeground(colorExited);
		doubleButton.setFont(font1);

		rulesButton = new JLabel("                Rules");
		rulesButton.setPreferredSize(new Dimension(150, 40));
		rulesButton.setForeground(colorExited);
		rulesButton.setFont(font1);

		quitbutton = new JLabel("           Quit Game");
		quitbutton.setPreferredSize(new Dimension(200, 40));
		quitbutton.setForeground(colorExited);
		quitbutton.setFont(font1);

		startPanel.add(Box.createRigidArea(new Dimension(00, 370)));
		startPanel.add(singleButton);
		startPanel.add(doubleButton);
		startPanel.add(rulesButton);
		startPanel.add(quitbutton);
		container.add(startPanel, BorderLayout.CENTER);

	}

	public void addListeners() {
		singleButton.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				entered(singleButton, "         Single Player");
			}

			public void mouseExited(MouseEvent e) {
				exited(singleButton, "          Single Player");

			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseReleased(MouseEvent e) {
				startGame(1);
			}
		});

		doubleButton.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				entered(doubleButton, "       Double Player");
			}

			public void mouseExited(MouseEvent e) {
				exited(doubleButton, "        Double Player");
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseReleased(MouseEvent e) {
				startGame(2);
			}
		});

		rulesButton.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				entered(rulesButton, "               Rules");
			}

			public void mouseExited(MouseEvent e) {
				exited(rulesButton, "                Rules");
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				rulesFrame.setVisible(true);
			}
		});

		quitbutton.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				entered(quitbutton, "          Quit Game");
			}

			public void mouseExited(MouseEvent e) {
				exited(quitbutton, "           Quit Game");
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
	}

	public void entered(JLabel label, String name) {
		playClickSound();
		label.setForeground(colorEntered);
		label.setFont(font2);
		label.setText(name);
	}

	public void exited(JLabel label, String name) {
		label.setForeground(colorExited);
		label.setFont(font1);
		label.setText(name);
	}

	public void playClickSound() {
		try {
			InputStream in = StartFrame.class.getResourceAsStream("/click.wav");
			BufferedInputStream buffer = new BufferedInputStream(in);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buffer);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void startGame(int player) {
		setVisible(false);
		boggleFrame.setPlayer(player);
		boggleFrame.resetBoard();
		boggleFrame.setVisible(true);
	}
}