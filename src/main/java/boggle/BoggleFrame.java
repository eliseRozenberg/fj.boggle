package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BoggleFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final StartFrame startFrame;
	private DoublePlayerGameSummaryFrame gameSummaryFrame;
	private final Logic logic;
	private BoggleThread thread;
	private Timer timer;
	private final DocumentFilter filter;
	private final AbstractDocument document;

	private final Container container;
	private final JPanel boardPanel, iconPanel, leftPanel, topPanel, rightPanel, bottomPanel, scorePanel;
	private final JTextField wordTextField;
	private final JTextArea wordListArea;
	private final JScrollPane areaScrollPane;
	private final JLabel[][] boggleBoard;
	private final JLabel timerLabel, pauseLabel, status, imageLabel, score1, score2, correctLabel;
	private final JButton resetBoard, rotateBoard, pauseButton, menuButton;
	private final ImageIcon boggleIcon, blankImage, checkImage, xImage;

	private Border boardClickedBorder, boardEnteredBorder, boardExitedBorder;
	private Font letterFont, font1, font2;

	private Stack<Cell> cellsStack;
	private ArrayList<String> words, words1;
	private String[][] copy;
	private boolean paused, roundOver;
	private int interval, turn, playersCount, total1, total2, total;

	@Inject
	public BoggleFrame(StartFrame frame) {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg")).getImage());

		startFrame = frame;
		filter = new UppercaseDocumentFilter();
		logic = new Logic();
		logic.fillBoard();

		container = getContentPane();
		boardPanel = new JPanel();
		topPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		bottomPanel = new JPanel();
		scorePanel = new JPanel();
		iconPanel = new JPanel();
		wordTextField = new JTextField();
		document = (AbstractDocument) wordTextField.getDocument();
		wordListArea = new JTextArea();
		areaScrollPane = new JScrollPane(wordListArea);
		resetBoard = new JButton("Reset Board!");
		rotateBoard = new JButton("ROTATE");
		menuButton = new JButton("MENU");
		pauseButton = new JButton("PAUSE");
		boggleBoard = new JLabel[4][4];
		imageLabel = new JLabel(new ImageIcon(getClass().getResource("/boggle.png")));
		timerLabel = new JLabel();
		score1 = new JLabel("Score 1: " + total);
		score2 = new JLabel("Score 2: " + total);
		status = new JLabel();
		correctLabel = new JLabel();
		pauseLabel = new JLabel("GAME PAUSED", JLabel.CENTER);
		gameSummaryFrame = new DoublePlayerGameSummaryFrame(this);

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);

			}
		}

		boggleIcon = new ImageIcon(getClass().getResource("/boggleMessage.png"));
		blankImage = new ImageIcon(new ImageIcon(getClass().getResource("/blank.png")).getImage().getScaledInstance(60,
				60, Image.SCALE_SMOOTH));
		checkImage = new ImageIcon(new ImageIcon(getClass().getResource("/check.jpg")).getImage().getScaledInstance(60,
				60, Image.SCALE_SMOOTH));
		xImage = new ImageIcon(new ImageIcon(getClass().getResource("/x.jpg")).getImage().getScaledInstance(60, 60,
				Image.SCALE_SMOOTH));

		letterFont = (new Font("Calibri", Font.BOLD, 50));
		font1 = new Font("Berlin Sans FB", Font.PLAIN, 35);
		font2 = new Font("Berlin Sans FB", Font.PLAIN, 30);

		boardClickedBorder = new LineBorder(Color.GREEN, 10, true);
		boardEnteredBorder = BorderFactory.createMatteBorder(12, 12, 8, 8, Color.blue);
		boardExitedBorder = new LineBorder(Color.blue, 10, true);

		cellsStack = new Stack<Cell>();
		words = new ArrayList<String>();
		copy = new String[4][4];
		roundOver = false;
		paused = false;
		playersCount = 1;
		interval = 181;
		total = 0;
		turn = 1;

		format();
		addToPanels();
		addListeners();
		addTimer();
	}

	private void addListeners() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				wordTextField.requestFocus();
			}
		});
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() {
			public boolean postProcessKeyEvent(KeyEvent event) {
				if ((event.getKeyCode() == KeyEvent.VK_ENTER) && (!roundOver)) {
					checkWord();
				} else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (!cellsStack.isEmpty() && ((cellsStack.size() - 1) == wordTextField.getText().length())) {
						Cell cell = cellsStack.pop();
						boggleBoard[cell.getRow()][cell.getCol()].setBorder(new LineBorder(Color.BLUE, 10, true));
						cell.setIsClicked(false);
					}
				}
				return false;
			}
		});

		resetBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetBoard();
				wordTextField.requestFocus();
			}
		});

		rotateBoard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rotateMatrixRight();
				wordTextField.requestFocus();
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				wordTextField.requestFocus();
				pause();
			}
		});
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
				startFrame.setVisible(true);
				if (paused) {
					pause();
				}
				timer.stop();
			}
		});
		wordTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkWord();
				wordTextField.requestFocus();
			}
		});

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				final int i = row;
				final int j = col;

				boggleBoard[row][col].addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent arg0) {

						if (!cellsStack.contains(logic.getCell(i, j))) {
							wordTextField.setText(wordTextField.getText() + logic.getValueOfCell(i, j));
							cellsStack.push(logic.getCell(i, j));
							boggleBoard[i][j].setBorder(boardClickedBorder);
							logic.setIsClicked(i, j, true);

							// play click sound
							getAudio("/click.wav");
						}
					}

					public void mouseEntered(MouseEvent arg0) {
						if (logic.getIsClicked(i, j) || !boggleBoard[i][j].isEnabled()) {
							boggleBoard[i][j].setBorder(boardClickedBorder);
						} else {
							boggleBoard[i][j].setBorder(boardEnteredBorder);
						}
					}

					public void mouseExited(MouseEvent arg0) {
						if (!logic.getIsClicked(i, j) || !boggleBoard[i][j].isEnabled()) {
							boggleBoard[i][j].setBorder(boardExitedBorder);
						}
					}

					public void mousePressed(MouseEvent arg0) {
					}

					public void mouseReleased(MouseEvent arg0) {

					}
				});
			}
		}
	}

	public int addScore(int amt) {
		int points = 0;
		// it is only likely that the word will be from 3-8
		switch (amt) {
		case 3:
			points = 1;
			total += 1;
			break;
		case 4:
			points = 2;
			total += 2;
			break;
		case 5:
			points = 3;
			total += 3;
			break;
		case 6:
			points = 4;
			total += 4;
			break;
		case 7:
			points = 5;
			total += 5;
			break;
		case 8:
			points = 6;
			total += 6;
			break;
		}
		if (turn == 1) {
			score1.setText("Score 1: " + total);
		} else {
			score2.setText("Score 2: " + total);
		}
		return points;
	}

	private void addTimer() {
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!paused) {
					timerLabel.setText("Timer: " + String.valueOf(checkTimer()));
				}
			}
		});
	}

	private void addToPanels() {
		scorePanel.add(score1);
		scorePanel.add(score2);

		topPanel.add(imageLabel, BorderLayout.CENTER);
		topPanel.add(timerLabel, BorderLayout.SOUTH);
		topPanel.add(scorePanel, BorderLayout.WEST);
		topPanel.add(status, BorderLayout.EAST);
		topPanel.add(menuButton, BorderLayout.NORTH);

		rightPanel.add(pauseButton, BorderLayout.NORTH);
		rightPanel.add(boardPanel, BorderLayout.CENTER);

		iconPanel.add(correctLabel);
		leftPanel.add(iconPanel, BorderLayout.SOUTH);
		leftPanel.add(areaScrollPane, BorderLayout.CENTER);
		leftPanel.add(resetBoard, BorderLayout.NORTH);

		bottomPanel.add(wordTextField);
		bottomPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		bottomPanel.add(rotateBoard);

		container.add(rightPanel, BorderLayout.CENTER);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);
		container.add(bottomPanel, BorderLayout.SOUTH);
	}

	public void appendWord(String word, int points) {
		words.add(word);
		if (points == 1) {
			wordListArea.append(" " + points + "    " + word.toUpperCase() + "\n");

		} else {
			wordListArea.append(" " + points + "   " + word.toUpperCase() + "\n");

		}
		wordTextField.setText("");
	}

	private int checkTimer() {
		if (interval == 0) {
			getAudio("/wrongAnswerSound.wav");
			endRound();
			return 0;
		}
		if (interval <= 5) {
			Toolkit.getDefaultToolkit().beep();
		}
		return --interval;
	}

	public void checkWord() {
		String word = wordTextField.getText().toLowerCase();
		wordTextField.setText("");
		resetCells();
		if (word.length() == 0) {
			return;
		}
		boolean valid = false;
		if (words.contains(word) || (word.length() < 3)) {
			setWordInvalid();
			return;
		}
		valid = logic.checkWord(word);

		if (valid) {
			thread = new BoggleThread(word, BoggleFrame.this);
			thread.start();
		} else {
			setWordInvalid();
		}
	}

	public void endRound() {
		roundOver = true;
		timer.stop();
		setStatus(2);
		if (playersCount == 2) {
			if (turn == 1) {
				setStatus(6);
				turn = 2;
				total1 = total;
				total = 0;
				score1.setText("Score 1: ???");
				wordListArea.setText("");
				words1 = words;
				words = new ArrayList<String>();

				JOptionPane.showMessageDialog(null, "Press enter to begin", "Player 2", JOptionPane.PLAIN_MESSAGE,
						boggleIcon);
				interval = 15;
				timer.start();

				return;
			} else {
				total2 = total;

				gameSummaryFrame.updateLists(words1, words);
				total1 = gameSummaryFrame.getScore1();
				total2 = gameSummaryFrame.getScore2();
				gameSummaryFrame.setVisible(true);

				score1.setText("Score 1: " + total1);
				score2.setText("Score 2: " + total2);

				if (total1 > total2) {
					setStatus(3);

				} else if (total1 < total2) {
					setStatus(4);
				} else {
					setStatus(5);
				}
				wordListArea.setText("");

			}
		}
		wordTextField.setEnabled(false);
		rotateBoard.setEnabled(false);
		pauseButton.setEnabled(false);

		for (JLabel[] element : boggleBoard) {
			for (JLabel element2 : element) {
				element2.setEnabled(false);
			}
		}
	}

	public void fillBoard() {
		logic.fillBoard();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col].setText(logic.getValueOfCell(row, col));
				boggleBoard[row][col].setHorizontalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setVerticalAlignment(JLabel.CENTER);
				boggleBoard[row][col].setFont(letterFont);
				boggleBoard[row][col].setForeground(Color.BLUE);
				boggleBoard[row][col].setBackground(Color.WHITE);
				boggleBoard[row][col].setOpaque(true);
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10, true));
			}
		}
	}

	private void format() {
		container.setLayout(new BorderLayout());

		boardPanel.setLayout(new GridLayout(4, 4));
		topPanel.setLayout(new BorderLayout());
		topPanel.setBackground(Color.BLUE);
		leftPanel.setLayout(new BorderLayout());
		rightPanel.setLayout(new BorderLayout());
		bottomPanel.setLayout(new FlowLayout());
		scorePanel.setBackground(Color.blue);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
		iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
		iconPanel.setBackground(Color.WHITE);

		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(font1);
		timerLabel.setForeground(Color.WHITE);

		correctLabel.setIcon(blankImage);

		wordTextField.setOpaque(true);
		wordTextField.setBackground(new Color(204, 204, 255));
		wordTextField.setForeground(Color.BLUE);
		wordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		wordTextField.setPreferredSize(new Dimension(50, 40));
		wordTextField.setFont(font1);
		wordTextField.setFocusable(true);
		wordTextField.setColumns(10);
		document.setDocumentFilter(filter);

		score1.setFont(font1);
		score1.setForeground(Color.WHITE);
		score2.setFont(font1);
		score2.setForeground(Color.WHITE);

		status.setFont(font1);
		status.setForeground(Color.WHITE);
		status.setText("hhhel");

		wordListArea.setBackground(Color.WHITE);
		wordListArea.setForeground(Color.BLACK);
		wordListArea.setFont(font2);
		wordListArea.setEditable(false);

		resetBoard.setBackground(new Color(204, 204, 255));
		resetBoard.setForeground(Color.BLUE);
		resetBoard.setFont(font1);
		resetBoard.setBorder(null);
		resetBoard.setBorderPainted(false);
		resetBoard.setFocusPainted(false);
		resetBoard.setRolloverEnabled(false);

		rotateBoard.setBackground(new Color(204, 204, 255));
		rotateBoard.setForeground(Color.BLUE);
		rotateBoard.setFont(font1);
		rotateBoard.setBorder(null);
		rotateBoard.setBorderPainted(false);
		rotateBoard.setFocusPainted(false);
		rotateBoard.setRolloverEnabled(false);

		menuButton.setBackground(new Color(204, 204, 255));
		menuButton.setForeground(Color.BLUE);
		menuButton.setFont(font1);
		menuButton.setBorder(null);
		menuButton.setBorderPainted(false);
		menuButton.setFocusPainted(false);
		menuButton.setRolloverEnabled(false);

		pauseButton.setBackground(new Color(204, 204, 255));
		pauseButton.setForeground(Color.BLUE);
		pauseButton.setFont(font2);
		pauseButton.setBorder(null);
		pauseButton.setBorderPainted(false);
		pauseButton.setFocusPainted(false);
		pauseButton.setRolloverEnabled(false);

		pauseLabel.setBackground(Color.BLACK);
		pauseLabel.setOpaque(true);
		pauseLabel.setForeground(Color.WHITE);
		pauseLabel.setFont(new Font("Calibri", Font.BOLD, 60));
	}

	public void getAudio(String name) {
		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(getClass().getResource(name).getFile()));
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

	public void pause() {
		if (paused) {
			rightPanel.remove(pauseLabel);
			rightPanel.add(boardPanel, BorderLayout.CENTER);
			pauseButton.setText("PAUSE");
			paused = false;
			resetBoard.setEnabled(true);
			wordTextField.setEnabled(true);
			rotateBoard.setEnabled(true);
			repaint();
			return;
		}
		if (!paused) {
			rightPanel.remove(boardPanel);
			rightPanel.add(pauseLabel, BorderLayout.CENTER);
			pauseButton.setText("RESUME");
			paused = true;
			resetBoard.setEnabled(false);
			wordTextField.setEnabled(false);
			rotateBoard.setEnabled(false);
			repaint();
			return;
		}
	}

	public void resetBoard() {
		roundOver = false;
		wordTextField.setText("");
		wordListArea.setText("");
		words.clear();
		score1.setText("Score 1: 0");
		if (playersCount == 1) {
			setStatus(1);
			score2.setVisible(false);
		} else {
			score2.setText("Score 2: 0");
			score2.setVisible(true);
			setStatus(7);
		}
		total = 0;
		turn = 1;
		interval = 15;
		fillBoard();
		wordTextField.setEnabled(true);
		rotateBoard.setEnabled(true);
		pauseButton.setEnabled(true);
		for (JLabel[] element : boggleBoard) {
			for (JLabel element2 : element) {
				element2.setEnabled(true);
			}
		}
		timer.start();
	}

	public void resetCells() {
		cellsStack.clear();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10, true));
				logic.setIsClicked(row, col, false);
			}
		}
	}

	public void rotateMatrixRight() {
		resetCells();
		wordTextField.setText("");
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				copy[c][4 - 1 - r] = logic.getValueOfCell(r, c);
			}
		}
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[i].length; j++) {
				Cell cell = new Cell(i, j, copy[i][j]);
				logic.setCell(i, j, cell);
				boggleBoard[i][j].setText(copy[i][j]);
			}
		}
	}

	public void setPlayer(int players) {
		this.playersCount = players;
	}

	private void setStatus(int num) {
		switch (num) {
		case 1:
			status.setText("Good Luck!");
			break;
		case 2:
			status.setText("Times Up!");
			break;
		case 3:
			status.setText("Player 1 Wins!");
			break;
		case 4:
			status.setText("Player 2 Wins!");
			break;
		case 5:
			status.setText("Tie Game");
			break;
		case 6:
			status.setText("Player 2's Turn");
			break;
		case 7:
			status.setText("Player 1's Turn");
			break;
		}
	}

	public void setWordInvalid() {
		getAudio("/wrongAnswerSound.wav");
		Thread thread = new Thread() {
			@Override
			public void run() {

				correctLabel.setIcon(xImage);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				correctLabel.setIcon(blankImage);
			}
		};
		thread.start();
	}

	public void setWordValid() {
		getAudio("/rightAnswerSound.wav");

		Thread thread = new Thread() {
			@Override
			public void run() {
				correctLabel.setIcon(checkImage);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				correctLabel.setIcon(blankImage);
			}
		};
		thread.start();
	}
}