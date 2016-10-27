package ak.scrabble.engine.model;

/**
 * Created by akopylov on 01/08/16.
 */
public class WordProposal {
    /** word found */
    private String word;

    /** corresponding pattern */
    private Pattern pattern;

    /** calculated score */
    private int score;

    public WordProposal(String word, Pattern pattern) {
        this.word = word;
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "WordProposal{" +
                "word='" + word + '\'' +
                ", score=" + score +
                '}';
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
