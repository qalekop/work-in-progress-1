package ak.scrabble.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akopylov on 14/12/15.
 */
public class Rack {
    private final int length;
    private List<String> letters;


    public Rack(int length) {
        this.length = length;
        this.letters = new ArrayList<>(length);
    }

    //<editor-fold desc="Getters-'n'-Setters">

    public List<String> getLetters() {
        return letters;
    }

    public void setLetters(List<String> letters) {
        this.letters = letters;
    }
    //</editor-fold>
}
