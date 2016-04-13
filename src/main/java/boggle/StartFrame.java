package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class StartFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PlayGamePanel playGamePanel;
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

		InputStream in = new FileInputStream(new File(getClass().getResource("/music.wav").getPath()));
		AudioStream music = new AudioStream(in);
		AudioPlayer.player.start(music);

		addFormat();
		addListeners();
	}

	public void addFormat() {
		container = getContentPane();
		container.setLayout(new BorderLayout());

		rulesFrame = new RulesFrame(this);
		playGamePanel = new PlayGamePanel();

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

		playGamePanel.add(Box.createRigidArea(new Dimension(00, 370)));
		playGamePanel.add(singleButton);
		playGamePanel.add(doubleButton);
		playGamePanel.add(rulesButton);
		playGamePanel.add(highScoreButton);
		container.add(playGamePanel, BorderLayout.CENTER);

	}

	public void addListeners() {
		singleButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				boggleFrame = new BoggleFrame(1);
				// boggle.setPlayer(1);
				boggleFrame.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				singleButton.setForeground(colorExited);
				singleButton.setFont(font1);
				singleButton.setText("          Single Player");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				singleButton.setForeground(colorEntered);
				singleButton.setFont(font2);
				singleButton.setText("         Single Player");

			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		doubleButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				boggleFrame = new BoggleFrame(2);
				// boggle.setPlayer(2);
				boggleFrame.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				doubleButton.setForeground(colorExited);
				doubleButton.setFont(font1);
				doubleButton.setText("        Double Player");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				doubleButton.setForeground(colorEntered);
				doubleButton.setFont(font2);
				doubleButton.setText("       Double Player");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		rulesButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				rulesFrame.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rulesButton.setForeground(colorExited);
				rulesButton.setFont(font1);
				rulesButton.setText("                Rules");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				rulesButton.setForeground(colorEntered);
				rulesButton.setFont(font2);
				rulesButton.setText("               Rules");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		highScoreButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// setVisible(false);
				// highScoreFrame.setVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				highScoreButton.setForeground(colorExited);
				highScoreButton.setFont(font1);
				highScoreButton.setText("           High Scores");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				highScoreButton.setForeground(colorEntered);
				highScoreButton.setFont(font2);
				highScoreButton.setText("          High Scores");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new StartFrame().setVisible(true);
	}
}