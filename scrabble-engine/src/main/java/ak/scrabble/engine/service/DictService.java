package ak.scrabble.engine.service;

import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.ImmutableWordProposal;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 01/08/16.
 */
@Service
public class DictService {

    private final WordRepository wordRepo;

    @Autowired
    public DictService(WordRepository wordRepo) {
        Assert.notNull(wordRepo, "WordRepository mustn't be null");
        this.wordRepo = wordRepo;
    }


    public List<WordProposal> findPossibleWords(final SearchSpec spec) {
        char[] availableChars = (spec.rack() + spec.pattern().getLetters()).toLowerCase().toCharArray();
        Arrays.sort(availableChars);
        return wordRepo.find(spec).stream()
                .filter(candidate -> valid(new String(availableChars), candidate.toLowerCase()))
                .map(candidate -> ImmutableWordProposal.builder().word(candidate).pattern(spec.pattern()).build())
                .collect(Collectors.toList());
    }

    private boolean valid(String availableChars, String candidate) {
        char[] chars = candidate.toCharArray();
        Arrays.sort(chars);
        int index = -1;
        for (char c : chars) {
            index = availableChars.indexOf(c, index + 1);
            if (index < 0) return false;
        }
        return true;
    }
}
