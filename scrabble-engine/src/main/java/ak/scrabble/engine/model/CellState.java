package ak.scrabble.engine.model;

/**
 * Created by akopylov on 25/04/16.
 */
public enum CellState {
    AVAILABLE,  // available
    OCCUPIED,   // occupied, but not yet accepted
    RESTRICTED, // free cell not allowed for next moves due to game rules
    REJECTED,   // letter is rejected
    HUMAN,      // human move is accepted
    MACHINE;    // machine move

    public boolean free() {
        return this == AVAILABLE || this == RESTRICTED;
    }
    public boolean accepted() {
        return this == HUMAN || this == MACHINE;
    }
}
