package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StartFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StartPanel startPanel;
	private HighScoreFrame highScoreFrame;
	private BoggleFrame boggleFrame;
	private RulesFrame rulesFrame;
	private Container container;
	private JLabel singleButton, doubleButton, rulesButton, highScoreButton;
	private Color colorExited, colorEntered;
	private Font font1, font2;

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

	public void addFormat() {
		container = getContentPane();
		container.setLayout(new BorderLayout());

		rulesFrame = new RulesFrame(this);
		startPanel = new StartPanel();
		boggleFrame = new BoggleFrame(this, 1);
		highScoreFrame = new HighScoreFrame(this);

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

		highScoreButton = new JLabel("           High Scores");
		highScoreButton.setPreferredSize(new Dimension(200, 40));
		highScoreButton.setForeground(colorExited);
		highScoreButton.setFont(font1);

		startPanel.add(Box.createRigidArea(new Dimension(00, 370)));
		startPanel.add(singleButton);
		startPanel.add(doubleButton);
		startPanel.add(rulesButton);
		startPanel.add(highScoreButton);
		container.add(startPanel, BorderLayout.CENTER);

	}

	public void playClickSound() {
		try {

			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(getClass().getResource("/click.wav").getFile()));
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

	public void addListeners() {
		singleButton.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				boggleFrame.setPlayer(1);
				boggleFrame.resetBoard();
				boggleFrame.setVisible(true);

			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseExited(MouseEvent e) {
				singleButton.setForeground(colorExited);
				singleButton.setFont(font1);
				singleButton.setText("          Single Player");
			}

			public void mouseEntered(MouseEvent e) {
				playClickSound();
				singleButton.setForeground(colorEntered);
				singleButton.setFont(font2);
				singleButton.setText("         Single Player");

			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		doubleButton.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				boggleFrame.setPlayer(2);
				boggleFrame.resetBoard();
				boggleFrame.setVisible(true);	
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseExited(MouseEvent e) {
				doubleButton.setForeground(colorExited);
				doubleButton.setFont(font1);
				doubleButton.setText("        Double Player");
			}

			public void mouseEntered(MouseEvent e) {
				playClickSound();
				doubleButton.setForeground(colorEntered);
				doubleButton.setFont(font2);
				doubleButton.setText("       Double Player");
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		rulesButton.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				rulesFrame.setVisible(true);
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseExited(MouseEvent e) {
				rulesButton.setForeground(colorExited);
				rulesButton.setFont(font1);
				rulesButton.setText("                Rules");
			}

			public void mouseEntered(MouseEvent e) {
				playClickSound();
				rulesButton.setForeground(colorEntered);
				rulesButton.setFont(font2);
				rulesButton.setText("               Rules");
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		highScoreButton.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				highScoreFrame.setVisible(true);
			}

			public void mousePressed(MouseEvent e) {
				playClickSound();
			}

			public void mouseExited(MouseEvent e) {
				highScoreButton.setForeground(colorExited);
				highScoreButton.setFont(font1);
				highScoreButton.setText("           High Scores");
			}

			public void mouseEntered(MouseEvent e) {
				playClickSound();
				highScoreButton.setForeground(colorEntered);
				highScoreButton.setFont(font2);
				highScoreButton.setText("          High Scores");
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new StartFrame().setVisible(true);
	}
}