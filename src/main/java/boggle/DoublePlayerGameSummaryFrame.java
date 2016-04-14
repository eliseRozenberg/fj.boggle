package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.google.inject.Singleton;

@Singleton
public class DoublePlayerGameSummaryFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BoggleFrame boggleFrame;
	private List<String> sameWords;
	private Container container;

	private JPanel leftPanel, middlePanel, bottomPanel, rightPanel;
	private JList<String> firstList, sameWordsList, secondList;
	private JLabel playerOneLabel, playerOneScoreLabel, playerTwoLabel, playerTwoScoreLabel, sameWordsLabel,
			winnerLabel;
	private JScrollPane listOnePane, listTwoPane, sameWordsPane;

	private int playerOneScore, playerTwoScore;

	public DoublePlayerGameSummaryFrame(BoggleFrame boggleFrame) {

		setSize(800, 600);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Game Summary");

		this.boggleFrame = boggleFrame;
		container = new Container();

		firstList = new JList<String>();
		secondList = new JList<String>();
		sameWordsList = new JList<String>();

		listOnePane = new JScrollPane(firstList);
		listTwoPane = new JScrollPane(secondList);
		sameWordsPane = new JScrollPane(sameWordsList);

		sameWords = new ArrayList<String>();

		leftPanel = new JPanel();
		rightPanel = new JPanel();
		middlePanel = new JPanel();
		bottomPanel = new JPanel();

		playerOneLabel = new JLabel("PLAYER ONE");
		playerTwoLabel = new JLabel("PLAYER TWO");
		sameWordsLabel = new JLabel("SAME WORDS");
		playerOneScoreLabel = new JLabel();
		winnerLabel = new JLabel();

		format();
		add();
	}

	public void format() {
		container.setLayout(new GridLayout(1, 3));

		firstList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		firstList.setVisibleRowCount(-1);
		firstList.setFont(new Font("Calibri", Font.PLAIN, 20));
		firstList.setBackground(Color.BLUE);
		firstList.setForeground(Color.WHITE);
		firstList.setLayoutOrientation(JList.VERTICAL_WRAP);

		secondList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		secondList.setVisibleRowCount(-1);
		secondList.setBackground(Color.BLUE);
		secondList.setForeground(Color.WHITE);
		secondList.setFont(new Font("Calibri", Font.PLAIN, 20));
		secondList.setLayoutOrientation(JList.VERTICAL_WRAP);

		sameWordsList.setLayoutOrientation(JList.VERTICAL_WRAP);
		sameWordsList.setVisibleRowCount(-1);
		sameWordsList.setFont(new Font("Calibri", Font.PLAIN, 20));

		leftPanel.setBackground(Color.BLUE);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		rightPanel.setBackground(Color.BLUE);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		middlePanel.setBackground(Color.WHITE);
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

		playerOneLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		playerOneLabel.setForeground(Color.WHITE);
		playerOneLabel.setFont(new Font("Calibri", Font.BOLD, 30));

		playerTwoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		playerTwoLabel.setForeground(Color.WHITE);
		playerTwoLabel.setFont(new Font("Calibri", Font.BOLD, 30));

		sameWordsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		sameWordsLabel.setForeground(Color.RED);
		sameWordsLabel.setFont(new Font("Calibri", Font.BOLD, 30));

		playerOneScoreLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		playerOneScoreLabel.setBackground(Color.BLUE);
		playerOneScoreLabel.setForeground(Color.WHITE);
		playerOneScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		playerTwoScoreLabel = new JLabel();
		playerTwoScoreLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		playerTwoScoreLabel.setBackground(Color.BLUE);
		playerTwoScoreLabel.setForeground(Color.WHITE);
		playerTwoScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		winnerLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		winnerLabel.setBackground(Color.WHITE);
		winnerLabel.setForeground(Color.RED);
		winnerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.setBackground(Color.BLACK);

		listOnePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listTwoPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sameWordsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

	}

	public void add() {

		leftPanel.add(playerOneLabel);
		leftPanel.add(listOnePane);

		rightPanel.add(playerTwoLabel);
		rightPanel.add(listTwoPane);

		middlePanel.add(sameWordsLabel);
		middlePanel.add(sameWordsPane);

		container.add(leftPanel);
		container.add(middlePanel);
		container.add(rightPanel);

		bottomPanel.add(playerOneScoreLabel);
		bottomPanel.add(winnerLabel);
		bottomPanel.add(playerTwoScoreLabel);

		add(container, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

	}

	public void updateLists(List<String> playerOneWords, List<String> playerTwoWords) {
		playCheerSound();
		sameWords.clear();
		playerOneScore = 0;
		playerTwoScore = 0;
		Iterator<String> iterator = playerOneWords.iterator();
		while (iterator.hasNext()) {
			String word = iterator.next();
			if (playerTwoWords.contains(word)) {
				sameWords.add(word);
			}
		}
		for (String word : sameWords) {
			playerOneWords.remove(word);
			playerTwoWords.remove(word);
		}
		for (int i = 0; i < playerOneWords.size(); i++) {
			String word = playerOneWords.get(i);
			int score = boggleFrame.addScore(word.length());
			playerOneScore += score;
			playerOneWords.set(i, score + "   " + word.toUpperCase());
		}
		for (int i = 0; i < playerTwoWords.size(); i++) {
			String word = playerTwoWords.get(i);
			int score = boggleFrame.addScore(word.length());
			playerTwoScore += score;
			playerTwoWords.set(i, score + "   " + word.toUpperCase());
		}
		for (int i = 0; i < sameWords.size(); i++) {
			sameWords.set(i, sameWords.get(i).toUpperCase());
		}

		firstList.setListData(playerOneWords.toArray(new String[0]));
		secondList.setListData(playerTwoWords.toArray(new String[0]));

		sameWordsList.setListData(sameWords.toArray(new String[0]));
		playerOneScoreLabel.setText("SCORE: " + playerOneScore);
		playerTwoScoreLabel.setText("SCORE: " + playerTwoScore);
		if (playerOneScore > playerTwoScore) {
			winnerLabel.setText("PLAYER 1 WINS");
		} else if (playerOneScore < playerTwoScore) {
			winnerLabel.setText("PLAYER 2 WINS");
		} else {
			winnerLabel.setText("GAME IS TIED");
		}
	}

	public int getScore1() {
		return playerOneScore;
	}

	public int getScore2() {
		return playerTwoScore;

	}

	public void playCheerSound() {
		try {
			InputStream in = StartFrame.class.getResourceAsStream("/cheering.wav");
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
}