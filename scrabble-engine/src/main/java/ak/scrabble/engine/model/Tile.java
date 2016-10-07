package ak.scrabble.engine.model;

/**
 * Created by akopylov on 28/09/2016.
 */
public class Tile {
    private char letter;
    private byte score;

    public Tile(char letter, byte score) {
        this.letter = letter;
        this.score = score;
    }
    //<editor-fold desc="GnS"

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    //</editor-fold>
}
