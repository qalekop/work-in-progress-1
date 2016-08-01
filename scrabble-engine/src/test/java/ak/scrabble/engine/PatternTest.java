package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.conf.ScrabbleDbConf;
import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.*;
import ak.scrabble.engine.service.DictService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by akopylov on 15/07/16.
 */
@ContextConfiguration(classes={ScrabbleDbConf.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PatternTest {

    @Autowired
    private WordRepository wordRepo;

    @Autowired
    private DictService dictService;


    private List<Cell> buildTestField() {
        List<Cell> result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row < Configuration.FIELD_SIZE; row++)
                result.add(new Cell(col, row));
        }
        Cell c;
        c = ScrabbleUtils.getByCoords(1, 0, result); c.setLetter('В'); c.setState(CellState.HUMAN);
        c = ScrabbleUtils.getByCoords(4, 0, result); c.setLetter('Т'); c.setState(CellState.HUMAN);
        c = ScrabbleUtils.getByCoords(5, 0, result); c.setLetter('О'); c.setState(CellState.HUMAN);
        return result;
    }

    @Test
    public void testPattern() {
        Set<Pattern> patterns = WordUtils.getPatternsForDimension(buildTestField(), DimensionEnum.ROW, 0);
        assertTrue(patterns.size() == 3);

        Set<String> wordsFound = new HashSet<>();
        for (Pattern pattern : patterns) {
            SearchSpec spec = ImmutableSearchSpec.builder()
                    .pattern(pattern)
                    .regexp(true)
                    .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                    .build();
            wordsFound.addAll(wordRepo.find(spec));
        }
        assertTrue(wordsFound.size() == 31);
        assertTrue(wordsFound.contains("авиатор"));
    }

    @Test
    public void testPatternsWithRack() {
        Set<Pattern> patterns = WordUtils.getPatternsForDimension(buildTestField(), DimensionEnum.ROW, 0);
        assertTrue(patterns.size() == 3);

        final String rack = "аиар";
        final String aviator = "авиатор";
        Set<WordProposal> proposals = new HashSet<>();
        for (Pattern pattern : patterns) {
            SearchSpec spec = ImmutableSearchSpec.builder()
                    .pattern(pattern)
                    .regexp(true)
                    .rack(rack)
                    .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                    .build();
            proposals.addAll(dictService.findPossibleWords(spec));
        }
        assertTrue(proposals.size() == 3);
        assertTrue(proposals.stream().filter(candidate -> candidate.word().equalsIgnoreCase(aviator)).findAny().isPresent());
    }

}
