package ak.scrabble.engine.service;

import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;

import java.util.List;

/**
 * Created by akopylov on 08/11/2016.
 */
public interface DictService {

    List<WordProposal> findPossibleWords(final SearchSpec spec);
}
