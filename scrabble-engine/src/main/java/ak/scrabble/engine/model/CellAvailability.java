package ak.scrabble.engine.model;

/**
 * Created by akopylov on 25/04/16.
 */
public enum CellAvailability {
    AVAILABLE,  // default state
    ALLOWED,    // free cell adjacent to one occupied. Next move should hit one (or more) ALLOWED cells
    UNALLOWED,  // free cell not allowed for next moves due to game rules
    OCCUPIED,   // occupied
    /*
    add ACCEPTED = OCCUPIED after move has been accepted
    add method isOccupied = OCCUPIED || ACCEPTED, or
    notAllowed = OCCUPIED || !ALLOWED. Anyway, you got the idea
     */
}
