package boggle;

public class Cell {

	private int row;
	private int col;
	private boolean inWord;
	private boolean isVisited;
	private String value;
	private boolean isClicked;

	public Cell(int row, int col, String value) {
		this.row = row;
		this.col = col;
		this.value = value;
		this.isVisited = false;
		this.inWord = false;
		this.isClicked = false;
	}

	public void setIsClicked(boolean clicked) {
		this.isClicked = clicked;
	}

	public void setInWord(boolean inWord) {
		this.inWord = inWord;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isInWord() {
		return inWord;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public String getValue() {
		return value;
	}

}
