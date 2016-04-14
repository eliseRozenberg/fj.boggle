package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.google.inject.Singleton;

@Singleton
public class DoublePlayerGameSummaryFrame extends JFrame {

	private BoggleFrame boggleFrame;
	private JList<String> firstList;
	private JPanel leftPanel;
	private JPanel middlePanel;
	private JLabel playerOneLabel;
	private JLabel playerOneScoreLabel;
	private JLabel playerTwoLabel;
	private JLabel playerTwoScoreLabel;
	private JPanel rightPanel;
	private List<String> sameWords;
	private JLabel sameWordsLabel;
	private JList<String> sameWordsList;
	private JList<String> secondList;
	private JLabel winnerLabel;

	public DoublePlayerGameSummaryFrame(BoggleFrame boggleFrame) {

		setSize(800, 600);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		this.boggleFrame = boggleFrame;
		sameWords = new ArrayList<String>();
		setTitle("Game Summary");

		firstList = new JList<String>();
		secondList = new JList<String>();
		sameWordsList = new JList<String>();
		firstList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		firstList.setVisibleRowCount(-1);
		firstList.setFont(new Font("Calibri", Font.PLAIN, 20));
		firstList.setBackground(Color.BLUE);
		firstList.setForeground(Color.WHITE);
		secondList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		secondList.setVisibleRowCount(-1);
		secondList.setBackground(Color.BLUE);
		secondList.setForeground(Color.WHITE);
		secondList.setFont(new Font("Calibri", Font.PLAIN, 20));
		sameWordsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		sameWordsList.setVisibleRowCount(-1);
		sameWordsList.setFont(new Font("Calibri", Font.PLAIN, 20));

		leftPanel = new JPanel();
		rightPanel = new JPanel();
		middlePanel = new JPanel();
		leftPanel.setBackground(Color.BLUE);
		rightPanel.setBackground(Color.BLUE);
		middlePanel.setBackground(Color.WHITE);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		playerOneLabel = new JLabel("PLAYER ONE");
		playerTwoLabel = new JLabel("PLAYER TWO");
		sameWordsLabel = new JLabel("SAME WORDS");
		playerOneLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		playerTwoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		sameWordsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		playerOneLabel.setForeground(Color.WHITE);
		playerTwoLabel.setForeground(Color.WHITE);
		sameWordsLabel.setForeground(Color.RED);
		playerOneLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		playerTwoLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		sameWordsLabel.setFont(new Font("Calibri", Font.BOLD, 30));

		playerOneScoreLabel = new JLabel();
		playerOneScoreLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		playerOneScoreLabel.setBackground(Color.BLUE);
		playerOneScoreLabel.setForeground(Color.WHITE);
		playerOneScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		playerTwoScoreLabel = new JLabel();
		playerTwoScoreLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		playerTwoScoreLabel.setBackground(Color.BLUE);
		playerTwoScoreLabel.setForeground(Color.WHITE);
		playerTwoScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		winnerLabel = new JLabel();
		winnerLabel.setFont(new Font("Calibri", Font.BOLD, 30));
		winnerLabel.setBackground(Color.WHITE);
		winnerLabel.setForeground(Color.RED);
		winnerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		leftPanel.add(playerOneLabel);
		leftPanel.add(firstList);

		rightPanel.add(playerTwoLabel);
		rightPanel.add(secondList);

		middlePanel.add(sameWordsLabel);
		middlePanel.add(sameWordsList);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.setBackground(Color.BLACK);
		bottomPanel.add(playerOneScoreLabel);
		bottomPanel.add(winnerLabel);
		bottomPanel.add(playerTwoScoreLabel);

		Container container = new Container();
		container.setLayout(new GridLayout(1, 3));
		container.add(leftPanel);
		container.add(middlePanel);
		container.add(rightPanel);

		add(container, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public void updateLists(List<String> playerOneWords,
			List<String> playerTwoWords) {
		int playerOneScore = 0;
		int playerTwoScore = 0;
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
}