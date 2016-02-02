package ak.scrabble.engine.model;

/**
 * Created by akopylov on 01/02/16.
 */
public class Word {
    //todo enhance later with other properties
    private String word;

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                '}';
    }

    //<editor-fold desc="Getters-'n'-Setters>
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    //</editor-fold>
}
