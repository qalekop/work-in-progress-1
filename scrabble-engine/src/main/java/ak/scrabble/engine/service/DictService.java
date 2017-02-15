package ak.scrabble.engine.service;

import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;

import java.util.List;
import java.util.Optional;

/**
 * Created by akopylov on 08/11/2016.
 */
public interface DictService {

    List<WordProposal> findPossibleWords(final SearchSpec spec);

    /**
     * Looks up for a word in "white" dictionaries.
     * @param word word to look for.
     * @return description of the word, if found, or <code>Optional.empty()</code> otherwise.
     */
    Optional<String> lookup(final String word);
}
