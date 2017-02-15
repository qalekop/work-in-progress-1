package ak.scrabble.engine.service.impl;

import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;
import ak.scrabble.engine.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 01/08/16.
 */
@Service
public class DictServiceImpl implements DictService {

    private final WordRepository wordRepo;

    @Autowired
    public DictServiceImpl(WordRepository wordRepo) {
        Assert.notNull(wordRepo, "WordRepository mustn't be null");
        this.wordRepo = wordRepo;
    }


    @Override
    public List<WordProposal> findPossibleWords(final SearchSpec spec) {
        char[] availableChars = (spec.rack() + spec.pattern().getLetters()).toLowerCase().toCharArray();
        Arrays.sort(availableChars);
        final String word = spec.regexp() ? spec.pattern().getFirstContent() : "";
        return wordRepo.find(spec).stream()
                .filter(candidate -> (StringUtils.isNotBlank(spec.rack())
                        && valid(new String(availableChars), candidate.toLowerCase())))
                .filter(candidate -> !candidate.equalsIgnoreCase(word))
                .map(candidate -> new WordProposal(candidate, spec.pattern()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> lookup(String word) {
        try {
            return Optional.ofNullable(wordRepo.lookup(word));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
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
