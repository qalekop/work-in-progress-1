package ak.scrabble.engine.model;

import java.util.Optional;

/**
 * Created by akopylov on 22/01/16.
 */
public class Cell {
    private int row;
    private int col;
    private char letter;
    /** <code>false</code> for the newly added letter (possible to be reverted), <code>true</code> after move is accepted. */
    private boolean accepted;
    /** to distinguish between Human and Machine players. Reasonable only for state == Occupied. */
    private Player player;
    private Bonus bonus = Bonus.NONE;
    private CellState state = CellState.AVAILABLE;

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

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public Optional<Player> getPlayer() {
        return state == CellState.OCCUPIED ? Optional.ofNullable(player) : Optional.empty();
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
//</editor-fold>
}
