package ak.scrabble.engine.model;

import org.immutables.value.Value;

/**
 * Created by akopylov on 01/08/16.
 */
public class WordProposal {
    /** word found*/
    String word;

    /** corresponding pattern */
    Pattern pattern;

    /** calculated score */
    int score;

    public WordProposal(String word, Pattern pattern) {
        this.word = word;
        this.pattern = pattern;
    }

    //<editor-fold desc="Getters-'n'-Setters>

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //</editor-fold>
}
