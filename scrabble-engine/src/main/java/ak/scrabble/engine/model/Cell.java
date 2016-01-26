package ak.scrabble.engine.model;

/**
 * Created by akopylov on 22/01/16.
 */
public class Cell {
    private int row;
    private int col;
    private char letter;

    //<editor-fold desc="Getters-'n'-Setters">
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
    //</editor-fold>
}
