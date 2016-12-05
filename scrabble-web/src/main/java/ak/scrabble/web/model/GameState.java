package ak.scrabble.web.model;

import ak.scrabble.engine.model.Cell;

import java.util.List;

/**
 * Created by akopylov on 16/11/2016.
 */
public class GameState {
    /** collection of cells, representing the human's move. Empty in case of a 'Shuffle' action */
    private List<Cell> cells;
    /** Letters that are left in the rack, combined into a string */
    private String rest;
    /** letters to be shuffled, in case of a 'Shuffle' action. Combined into a string */
    private String shuffle;

    //<editor-fold desc="Getters-'n'-Setters">
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getShuffle() {
        return shuffle;
    }

    public void setShuffle(String shuffle) {
        this.shuffle = shuffle;
    }
    //</editor-fold>
}
