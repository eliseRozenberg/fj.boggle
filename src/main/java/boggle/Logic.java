package boggle;

import java.util.Random;

public class Logic {

	private Cell[][] board;
	private int num;
	private String letter;

	// private JTextField wordLabel;

	// public Logic(JTextField wordLabel) {
	// this.wordLabel = wordLabel;
	// }

	public Cell[][] fillBoard() {

		// make random for the vowels

		board = new Cell[4][4];
		Random rand = new Random();

		String[] vowels = new String[4];
		int vowelIndex = 0;
		while (vowelIndex < 4) {
			int vowelNum = rand.nextInt(5 - 1) + 1;
			String vowel = null;
			switch (vowelNum) {
			case 1:
				vowel = "A";
				break;
			case 2:
				vowel = "E";
				break;
			case 3:
				vowel = "I";
				break;
			case 4:
				vowel = "O";
				break;
			case 5:
				vowel = "U";
				break;
			}
			vowels[vowelIndex++] = vowel;

			int randRow = rand.nextInt(4);
			int randCol = rand.nextInt(4);
			board[randRow][randCol] = new Cell(randRow, randCol, vowel);
		}

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null) {
					num = rand.nextInt(91 - 65) + 65;
					if (num == 81) {
						letter = "QU";
					} else {
						letter = String.valueOf((char) num);
					}
					board[i][j] = new Cell(i, j, letter);
				}
			}
		}
		return board;
	}

	public boolean checkWord(String word) {

		int wordIndex = 0;
		boolean found = false;

		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {

				found = checkAround(x, y, word, wordIndex);
				if (found) {
					return true;
				}
			}
		}
		return found;
	}

	public boolean checkAround(int x, int y, String word, int index) {
		boolean isQ = false;
		boolean found = false;
		if (inBounds(x, y)) {

			if (index >= word.length()) {
				return true; // we came to the end of the word
			}

			if (board[x][y].getValue().equalsIgnoreCase("QU")
					&& ((word.charAt(index) == 'q') || (word.charAt(index) == 'Q'))) {
				index++;
				isQ = true;
			}

			if (!String.valueOf(word.charAt(index)).equalsIgnoreCase(
					board[x][y].getValue())
					&& !isQ) {
				return false;
			}

			found = checkAround(x + 1, y, word, index + 1)
					|| checkAround(x, y + 1, word, index + 1)
					|| checkAround(x - 1, y, word, index + 1)
					|| checkAround(x, y - 1, word, index + 1)
					|| checkAround(x + 1, y + 1, word, index + 1)
					|| checkAround(x - 1, y - 1, word, index + 1)
					|| checkAround(x - 1, y + 1, word, index + 1)
					|| checkAround(x + 1, y - 1, word, index + 1);
		}
		return found;

	}

	public boolean inBounds(int i, int j) {
		if ((i >= 0) && (i < 4)) {
			if ((j >= 0) && (j < 4)) {
				return true;
			}
		}
		return false;
	}

	public String getValueOfCell(int row, int column) {
		return board[row][column].getValue();
	}

	public void setCell(int row, int column, Cell cell) {
		board[row][column] = cell;
	}

	public void setIsClicked(int row, int column, boolean clicked) {
		board[row][column].setIsClicked(clicked);
	}

	public boolean getIsClicked(int row, int column) {
		return board[row][column].getIsClicked();
	}

	public Cell getCell(int row, int column) {
		return board[row][column];
	}
}
