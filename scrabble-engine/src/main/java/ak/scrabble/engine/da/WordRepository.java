package ak.scrabble.engine.da;

import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.Word;

import java.util.List;

/**
 * Created by akopylov on 07/07/16.
 */
public interface WordRepository {
    /**
     * Adds a word along with associated data (who, when, why)
     * to the stop list.
     * @param word to be specified later
     */
    void addToBlackList(Word word);

    /**
     * Adds a word along with associated data (who, when, definition)
     * to the user-defined dictionary.
     * @param word to be specified later
     */
    void addToWhiteList(Word word);

    /**
     * Returns list of 'word' objects (to be specified later)
     * acording to a given specification (which includes the list of dictionaries
     * to look up - i.e., Black list means a check for banned words).
     * @param specification what and ehere to search
     * @return Collection of objects, possibly empty
     */
    List<Word> find(SearchSpec specification);
}
