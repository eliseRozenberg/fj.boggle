package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartGameGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton singleButton, doubleButton, rules, highScore;
	private final JPanel optionsPanel;
	private int player;
	private BoggleFrame boggle;

	public StartGameGui() throws IOException {

		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		PlayGamePanel panel = new PlayGamePanel();
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setBackground(Color.blue);
		container.add(panel, BorderLayout.CENTER);

		singleButton = new JButton("Single Player");
		singleButton.setPreferredSize(new Dimension(50, 40));
		singleButton.setBackground(Color.BLUE);
		singleButton.setForeground(Color.YELLOW);
		singleButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		doubleButton = new JButton("Double Player");
		doubleButton.setPreferredSize(new Dimension(50, 40));
		doubleButton.setBackground(Color.BLUE);
		doubleButton.setForeground(Color.YELLOW);
		doubleButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		rules = new JButton("Rules");
		rules.setPreferredSize(new Dimension(50, 40));
		rules.setBackground(Color.BLUE);
		rules.setForeground(Color.YELLOW);
		rules.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		highScore = new JButton("High Scores");
		highScore.setPreferredSize(new Dimension(50, 40));
		highScore.setBackground(Color.BLUE);
		highScore.setForeground(Color.YELLOW);
		highScore.setFont(new Font("Berlin Sans FB", Font.PLAIN, 35));

		optionsPanel.add(singleButton);
		optionsPanel.add(doubleButton);
		optionsPanel.add(rules);
		optionsPanel.add(highScore);
		container.add(optionsPanel, BorderLayout.SOUTH);

		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg"))
				.getImage());

		singleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				boggle = new BoggleFrame(1);
				boggle.setVisible(true);

			}
		});

		doubleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				boggle = new BoggleFrame(2);
				// boggle.setPlayer(2);
				boggle.setVisible(true);

			}
		});
	}

	public static void main(String[] args) throws IOException {
		new StartGameGui().setVisible(true);
	}
}