package ak.scrabble.engine.model;

/**
 * Created by akopylov on 01/02/16.
 */
public enum CellState {
    AVAILABLE,      // free and available for moves
    UNAVAILABLE,    // free but unavailable due to game rules
    OCCUPIED,       // occupied
    OTHER           // impossible state to initialize word search
}
