package boggle;

import java.util.Random;

public class Logic {

	private Cell[][] board;
	private String letter;
	private int num;

	public boolean checkAround(int row, int col, String word, int index) {

		boolean isQ = false;
		boolean found = false;
		if (inBounds(row, col)) {

			if (index >= word.length()) {
				return true; // we came to the end of the word
			}

			if (board[row][col].getValue().equalsIgnoreCase("QU")
					&& ((word.charAt(index) == 'q') || (word.charAt(index) == 'Q'))) {
				index++;
				isQ = true;
			}

			if (!String.valueOf(word.charAt(index)).equalsIgnoreCase(board[row][col].getValue()) && !isQ) {
				return false;
			}
			if (board[row][col].getIsVisited()) {
				return false;
			}
			board[row][col].setIsVisited(true);
			found = checkAround(row + 1, col, word, index + 1) || checkAround(row, col + 1, word, index + 1)
					|| checkAround(row - 1, col, word, index + 1) || checkAround(row, col - 1, word, index + 1)
					|| checkAround(row + 1, col + 1, word, index + 1) || checkAround(row - 1, col - 1, word, index + 1)
					|| checkAround(row - 1, col + 1, word, index + 1) || checkAround(row + 1, col - 1, word, index + 1);
		}
		return found;

	}

	public boolean checkWord(String word) {

		int wordIndex = 0;
		boolean found = false;

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {

				found = checkAround(row, col, word, wordIndex);
				resetVisited();
				if (found) {
					return true;
				}
			}
		}
		return found;
	}

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

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == null) {
					num = rand.nextInt(91 - 65) + 65;
					if (num == 81) {
						letter = "QU";
					} else {
						letter = String.valueOf((char) num);
					}
					board[row][col] = new Cell(row, col, letter);
				}
			}
		}
		return board;
	}

	public boolean inBounds(int row, int col) {
		if ((row >= 0) && (row < 4)) {
			if ((col >= 0) && (col < 4)) {
				return true;
			}
		}
		return false;
	}

	private void resetVisited() {
		for (Cell[] element : board) {
			for (int col = 0; col < element.length; col++) {
				if (element[col].getIsVisited()) {
					element[col].setIsVisited(false);
				}
			}
		}
	}

	public void setCell(int row, int col, Cell cell) {
		board[row][col] = cell;
	}

	public Cell getCell(int row, int col) {
		return board[row][col];
	}

	public String getValueOfCell(int row, int col) {
		return board[row][col].getValue();
	}

	public void setIsClicked(int row, int col, boolean clicked) {
		board[row][col].setIsClicked(clicked);
	}

	public boolean getIsClicked(int row, int col) {
		return board[row][col].getIsClicked();
	}

}
