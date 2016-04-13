package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.google.inject.Inject;

public class BoggleFrame extends JFrame {

	public static void main(String[] args) {
		new BoggleFrame(1).setVisible(true);
	}

	private static final long serialVersionUID = 1L;
	private BoggleThread thread;
	private final Logic logic;
	private Timer timer;

	private final Font letterFont;

	private final Container container;
	private final JPanel boardPanel;
	private final JPanel leftPanel;
	private final JPanel topPanel;
	private final JPanel rightPanel;
	private final JPanel scorePanel;
	private final JTextField wordTextField;
	private final JTextArea wordListArea;
	private final JLabel[][] boggleBoard;
	private final JLabel timerLabel;
	private final JLabel status;
	private final JLabel imageLabel;
	private final JLabel score1, score2;
	private final JButton resetBoard;
	private final JButton rotateBoard;
	private final JButton pauseButton;
	private final Stack<Cell> cellsStack;

	private final JLabel correctLabel;
	private final ArrayList<String> words;
	private final String[][] copy;
	private int interval = 181;
	private int turn = 1, players = 1;
	private int total1, total2, total = 0;
	private boolean paused;

	private final ImageIcon blankImage;
	private final ImageIcon checkImage;
	private final ImageIcon xImage;
	private final JLabel pauseLabel;

	private final Border boardClickedBorder, boardEnteredBorder,
			boardExitedBorder, rotateEnteredBorder, rotateExitedBorder;

	@Inject
	public BoggleFrame(int players) {
		setTitle("BOGGLE");
		setSize(600, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(new ImageIcon(getClass().getResource("/frameLogo.jpg"))
				.getImage());

		container = getContentPane();
		boardPanel = new JPanel();
		topPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		scorePanel = new JPanel();
		wordListArea = new JTextArea();
		resetBoard = new JButton("Reset Board!");
		rotateBoard = new JButton("ROTATE");
		pauseButton = new JButton("PAUSE");
		imageLabel = new JLabel(new ImageIcon(getClass().getResource(
				"/boggle.png")));
		timerLabel = new JLabel();
		score1 = new JLabel("Score 1: " + total);
		score2 = new JLabel("Score 2: " + total);
		status = new JLabel();
		boggleBoard = new JLabel[4][4];
		wordTextField = new JTextField();
		correctLabel = new JLabel();
		pauseLabel = new JLabel("GAME PAUSED", JLabel.CENTER);
		cellsStack = new Stack<Cell>();
		logic = new Logic();
		logic.fillBoard();

		blankImage = new ImageIcon(new ImageIcon(getClass().getResource(
				"/blank.png")).getImage().getScaledInstance(60, 60,
				Image.SCALE_SMOOTH));
		checkImage = new ImageIcon(new ImageIcon(getClass().getResource(
				"/check.jpg")).getImage().getScaledInstance(60, 60,
				Image.SCALE_SMOOTH));
		xImage = new ImageIcon(new ImageIcon(getClass().getResource("/x.jpg"))
				.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col] = new JLabel();
				boardPanel.add(boggleBoard[row][col]);

			}
		}

		letterFont = (new Font("Calibri", Font.BOLD, 50));

		// boggleIcon = new
		// ImageIcon(getClass().getResource("/boggleMessage.png"));

		boardClickedBorder = new LineBorder(Color.GREEN, 10, true);
		boardEnteredBorder = BorderFactory.createMatteBorder(12, 12, 8, 8,
				Color.blue);
		boardExitedBorder = new LineBorder(Color.blue, 10, true);
		rotateEnteredBorder = BorderFactory.createMatteBorder(3, 3, 0, 0,
				Color.blue);
		rotateExitedBorder = new LineBorder(Color.BLUE, 1, true);

		words = new ArrayList<String>();
		copy = new String[4][4];
		paused = false;
		turn = 1;
		this.players = players;
		format();
		addToPanels();
		addActionListeners();
		addTimer();
		resetBoard();
	}

	private void setWindowListener() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				wordTextField.requestFocus();
			}
		});
	}

	private void addActionListeners() {

		setWindowListener();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventPostProcessor(new KeyEventPostProcessor() {

					public boolean postProcessKeyEvent(KeyEvent event) {

						if (event.getKeyCode() == KeyEvent.VK_ENTER) {
							checkWord();
						} else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
							if (!cellsStack.isEmpty()
									&& ((cellsStack.size() - 1) == wordTextField
											.getText().length())) {
								Cell cell = cellsStack.pop();
								boggleBoard[cell.getRow()][cell.getCol()]
										.setBorder(new LineBorder(Color.BLUE,
												10, true));
								cell.setIsClicked(false);
							}
						}
						return false;
					}
				});

		resetBoard.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				resetBoard();
			}
		});

		rotateBoard.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				rotateMatrixRight();
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		});

		wordTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				checkWord();
			}
		});

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				final int i = row;
				final int j = col;

				boggleBoard[row][col].addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent arg0) {

						if (!cellsStack.contains(logic.getCell(i, j))) {
							wordTextField.setText(wordTextField.getText()
									+ logic.getValueOfCell(i, j));
							cellsStack.push(logic.getCell(i, j));
							boggleBoard[i][j].setBorder(boardClickedBorder);
							logic.setIsClicked(i, j, true);

							// play click sound
							try {

								AudioInputStream audioInputStream = AudioSystem

								.getAudioInputStream(new File(getClass()
										.getResource("/click.wav").getFile()));

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

					public void mouseEntered(MouseEvent arg0) {

						if (logic.getIsClicked(i, j)) {
							boggleBoard[i][j].setBorder(boardClickedBorder);
						} else {

							boggleBoard[i][j].setBorder(boardEnteredBorder);
						}
					}

					public void mouseExited(MouseEvent arg0) {

						if (!logic.getIsClicked(i, j)) {
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

	public void addScore(int amt) {

		// it is only likely that the word will be from 3-8
		switch (amt) {
		case 3:
			total += 1;
			break;
		case 4:
			total += 2;
			break;
		case 5:
			total += 3;
			break;
		case 6:
			total += 4;
			break;
		case 7:
			total += 5;
			break;
		case 8:
			total += 6;
			break;
		}
		if (turn == 1) {
			score1.setText("Score 1: " + total);
		} else {
			score2.setText("Score 2: " + total);
		}
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

		topPanel.add(imageLabel, BorderLayout.NORTH);
		topPanel.add(timerLabel, BorderLayout.SOUTH);
		topPanel.add(scorePanel, BorderLayout.WEST);
		topPanel.add(status, BorderLayout.EAST);

		rightPanel.add(pauseButton, BorderLayout.NORTH);
		rightPanel.add(boardPanel, BorderLayout.CENTER);
		rightPanel.add(rotateBoard, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		panel.add(correctLabel);
		panel.add(wordTextField);
		leftPanel.add(panel, BorderLayout.SOUTH);
		leftPanel.add(wordListArea, BorderLayout.CENTER);
		leftPanel.add(resetBoard, BorderLayout.NORTH);

		container.add(rightPanel, BorderLayout.CENTER);
		container.add(topPanel, BorderLayout.NORTH);
		container.add(leftPanel, BorderLayout.WEST);
	}

	public void appendWord(String word) {
		words.add(word);
		wordListArea.append(word.toUpperCase() + "\n");
		wordTextField.setText("");
	}

	private int checkTimer() {
		if (interval == 0) {
			endRound();
			return 0;
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
		timer.stop();
		setStatus(2);
		if (players == 2) {
			if (turn == 1) {
				setStatus(6);
				turn = 2;
				total1 = total;
				total = 0;
				score1.setText("Score: ???");
				wordListArea.setText("");
				words.clear();
				JOptionPane.showMessageDialog(null, "Press enter to begin",
						"Player 2", JOptionPane.PLAIN_MESSAGE);
				interval = 181;
				timer.start();
				return;
			} else {
				total2 = total;
				score1.setText("Score: " + total1);
				score2.setText("Score: " + total2);
				if (total1 > total2) {
					setStatus(3);

				} else if (total1 < total2) {
					setStatus(4);
				} else {
					setStatus(5);
				}
			}
		}
		wordTextField.setEnabled(false);
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

				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10,
						true));

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

		scorePanel.setBackground(Color.blue);
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));

		Font font = new Font("Berlin Sans FB", Font.PLAIN, 35);
		Font fontTwo = new Font("Berlin Sans FB", Font.PLAIN, 30);

		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(font);
		timerLabel.setForeground(Color.WHITE);

		correctLabel.setIcon(blankImage);

		wordTextField.setOpaque(true);
		wordTextField.setBackground(new Color(204, 204, 255));
		wordTextField.setForeground(Color.BLUE);
		wordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		wordTextField.setPreferredSize(new Dimension(50, 40));
		wordTextField.setFont(font);
		wordTextField.setFocusable(true);

		score1.setFont(font);
		score1.setForeground(Color.WHITE);
		score2.setFont(font);
		score2.setForeground(Color.WHITE);

		status.setFont(font);
		status.setForeground(Color.WHITE);
		status.setText("hhhel");

		wordListArea.setBackground(Color.WHITE);
		wordListArea.setForeground(Color.BLACK);
		wordListArea.setFont(fontTwo);
		wordListArea.setEditable(false);
		wordListArea.setPreferredSize(new Dimension(200, 50));

		resetBoard.setBackground(new Color(204, 204, 255));
		resetBoard.setForeground(Color.BLUE);
		resetBoard.setFont(font);
		resetBoard.setBorder(null);
		resetBoard.setBorderPainted(false);
		resetBoard.setFocusPainted(false);
		resetBoard.setRolloverEnabled(false);

		rotateBoard.setBackground(new Color(204, 204, 255));
		rotateBoard.setForeground(Color.BLUE);
		rotateBoard.setFont(font);
		rotateBoard.setBorder(null);
		rotateBoard.setBorderPainted(false);
		rotateBoard.setFocusPainted(false);
		rotateBoard.setRolloverEnabled(false);

		pauseButton.setBackground(new Color(204, 204, 255));
		pauseButton.setForeground(Color.BLUE);
		pauseButton.setFont(fontTwo);
		pauseButton.setBorder(null);
		pauseButton.setBorderPainted(false);
		pauseButton.setFocusPainted(false);
		pauseButton.setRolloverEnabled(false);

		pauseLabel.setBackground(Color.BLACK);
		pauseLabel.setOpaque(true);
		pauseLabel.setForeground(Color.WHITE);
		pauseLabel.setFont(new Font("Calibri", Font.BOLD, 60));
	}

	public void resetBoard() {
		wordTextField.setEnabled(true);
		wordTextField.setText("");
		wordListArea.setText("");
		words.clear();
		score1.setText("Score 1: 0");
		if (players == 1) {
			setStatus(1);
			score2.setVisible(false);
		} else {
			score2.setText("Score 2: 0");
			setStatus(7);
		}
		total = 0;
		turn = 1;
		interval = 181;
		fillBoard();
		timer.start();
	}

	public void resetCells() {
		cellsStack.clear();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				boggleBoard[row][col].setBorder(new LineBorder(Color.BLUE, 10,
						true));
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
		this.players = players;
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