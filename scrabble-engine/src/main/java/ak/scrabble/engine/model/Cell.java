package ak.scrabble.engine.model;

/**
 * Created by akopylov on 22/01/16.
 */
public class Cell {
    private int row;
    private int col;
    private char letter;
    /** <code>false</code> for the newly added letter (possible to be reverted), <code>true</code> after move is accepted. */
    private boolean accepted;
    /** to distinguish between Human and Machine players. Only for state == Occupied. */
    private Player player;
    private Bonus bonus = Bonus.NONE;
    private CellState state = CellState.AVAILABLE;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Cell() {
    }

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

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    //</editor-fold>
}
