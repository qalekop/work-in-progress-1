package ak.scrabble.engine.model;

/**
 * Created by akopylov on 25/04/16.
 */
public enum CellState {
    AVAILABLE,  // available
    OCCUPIED,   // occupied, but not yet accepted
    RESTRICTED, // free cell not allowed for next moves due to game rules
    ACCEPTED;   // letter is accepted

    public boolean free() {
        return this == AVAILABLE || this == RESTRICTED;
    }
}
