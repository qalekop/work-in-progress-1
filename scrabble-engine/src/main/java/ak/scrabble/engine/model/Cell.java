package ak.scrabble.engine.model;

/**
 * Created by akopylov on 22/01/16.
 */
public class Cell {
    private int row;
    private int col;
    private char letter;
    /** false for the newly added letter (possible to be reverted), true after move is accepted */
    private boolean accepted;
    private CellStateEnum state = CellStateEnum.AVAILABLE;

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

    public boolean isAccepted() {
        return accepted;
    }

    public CellStateEnum getState() {
        return state;
    }

    public void setState(CellStateEnum state) {
        this.state = state;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
    //</editor-fold>
}
