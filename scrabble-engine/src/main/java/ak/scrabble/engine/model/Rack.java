package ak.scrabble.engine.model;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akopylov on 14/12/15.
 */
public class Rack {
    private final int length;
    private List<Pair> letters;


    public Rack(int length) {
        this.length = length;
        this.letters = new ArrayList<>(length);
    }

    //<editor-fold desc="Getters-'n'-Setters">
    public List<Pair> getLetters() {
        return letters;
    }

    public void setLetters(List<Pair> letters) {
        this.letters = letters;
    }

    public boolean addPair(String letter, byte score) {
        return letters.size() < length && letters.add(new Pair(letter, score));
    }

    public boolean addPair(Pair letter) {
        return letters.size() < length && letters.add(letter);
    }
    //</editor-fold>

    //<editor-fold desc="Internal class(es)">
    private class Pair {
        private String letter;
        private byte score;

        public Pair(String letter, byte score) {
            this.letter = letter;
            this.score = score;
        }

        public String getLetter() {
            return letter;
        }

        public byte getScore() {
            return score;
        }
    }
    //</editor-fold>
}
