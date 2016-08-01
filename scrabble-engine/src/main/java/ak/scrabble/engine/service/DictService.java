package ak.scrabble.engine.service;

import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.ImmutableWordProposal;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 01/08/16.
 */
public class DictService {

    @Autowired
    private WordRepository wordRepo;


    public List<WordProposal> findPossibleWords(final SearchSpec spec) {
        return wordRepo.find(spec).stream()
                .filter(candidate -> valid(spec.rack() + spec.pattern().getLetters(), candidate))
                .map(candidate -> ImmutableWordProposal.builder().word(candidate).pattern(spec.pattern()).build())
                .collect(Collectors.toList());
    }

    private boolean valid(String charSet, String test) {
        char[] chars = test.toCharArray();
        Arrays.sort(chars);
        return charSet.contains(new String(chars));
    }
}
