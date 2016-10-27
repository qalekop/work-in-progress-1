package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.conf.ScrabbleDbConf;
import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DictFlavor;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.ImmutableSearchSpec;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.Pattern;
import ak.scrabble.engine.model.ResponseSuccess;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.WordProposal;
import ak.scrabble.engine.service.DictService;
import ak.scrabble.engine.service.GameService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by akopylov on 15/07/16.
 */
@ContextConfiguration(classes={ScrabbleDbConf.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("unchecked")
public class PatternTest {

    private static final String RACK = "АИАР";
    private static final String AVIATOR = "авиатор";
    private static final int INDEX = 3;

    @Autowired
    private WordRepository wordRepo;

    @Autowired
    private DictService dictService;

    @Autowired
    private GameService gameService;


    private List<Cell>buildTestField(int index) {
        List<Cell> result = buildEmptyField();
        Cell c;
        c = ScrabbleUtils.getByCoords(1, index, result); c.setLetter('В'); c.setState(CellState.HUMAN);
        c = ScrabbleUtils.getByCoords(4, index, result); c.setLetter('Т'); c.setState(CellState.HUMAN);
        c = ScrabbleUtils.getByCoords(5, index, result); c.setLetter('О'); c.setState(CellState.HUMAN);
        return result;
    }

    private List<Cell>buildTestField(int index, char... word) {
        List<Cell> result = buildEmptyField();
        Cell c;
        int ix = index;
        for (char ch : word) {
            c = ScrabbleUtils.getByCoords(ix++, 0, result); c.setLetter(ch); c.setState(CellState.HUMAN);
        }
        return result;
    }

    private List<Cell> buildEmptyField() {
        List<Cell> result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row < Configuration.FIELD_SIZE; row++) {
                Cell cell = new Cell(col, row);
                cell.setBonus(ScrabbleUtils.bonusForCell(row, col));
                result.add(cell);
            }
        }
        return result;
    }

    @Test
    public void testPattern() {
        Set<Pattern> patterns = WordUtils.getPatternsForDimension(buildTestField(INDEX), DimensionEnum.ROW, INDEX);
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
    public void testPredefinedPattern() {
        Set<Pattern> patterns = WordUtils.getPatternsForDimension(buildTestField(0, 'Л', 'О', 'Ж', 'А'), DimensionEnum.ROW, 0);
        assertTrue(patterns.size() == 1);
        SearchSpec spec = ImmutableSearchSpec.builder()
                .pattern(patterns.iterator().next())
                .regexp(true)
                .rack("ЛААТЕЖО")
                .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                .build();
        Collection<WordProposal> proposals = dictService.findPossibleWords(spec);
        assertTrue(CollectionUtils.isEmpty(proposals));
    }

    @Test
    public void testPatternsWithRack() {
        Set<Pattern> patterns = WordUtils.getPatternsForDimension(buildTestField(INDEX), DimensionEnum.ROW, INDEX);
        assertTrue(patterns.size() == 3);

        Set<WordProposal> proposals = new HashSet<>();
        for (Pattern pattern : patterns) {
            SearchSpec spec = ImmutableSearchSpec.builder()
                    .pattern(pattern)
                    .regexp(true)
                    .rack(RACK)
                    .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                    .build();
            proposals.addAll(dictService.findPossibleWords(spec));
        }
        assertTrue(proposals.size() == 3);
        assertTrue(proposals.stream().filter(candidate -> candidate.getWord().equalsIgnoreCase(AVIATOR)).findAny().isPresent());
    }

    @Test
    public void testWordPlacements() {
        List<Cell> field = buildTestField(INDEX);
        final String aviaRack = RACK + "ТО";

        List<WordProposal> proposals = gameService.findProposals(field, aviaRack);
        proposals.forEach(System.out::println);

        String rack = aviaRack;
        int score = 0;
        do {
            WordProposal proposal = proposals.get(0);
            Pair<Integer, String> result = WordUtils.putWord(field, proposal, rack);
            score += result.getLeft();
            rack = result.getRight();
            proposals = gameService.findProposals(field, rack);
        } while (!CollectionUtils.isEmpty(proposals) && StringUtils.isNotBlank(rack));
        TestUtils.printField(field);

        assertTrue(score == 42);
    }

    @Test
    public void testHumanMoveScoring() {
        List<Cell> field = buildTestField(INDEX);
        Cell c;
        c = ScrabbleUtils.getByCoords(0, INDEX, field); c.setLetter('А'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(2, INDEX, field); c.setLetter('И'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(3, INDEX, field); c.setLetter('А'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(6, INDEX, field); c.setLetter('Р'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(6, INDEX + 1, field); c.setLetter('О'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(6, INDEX + 2, field); c.setLetter('Т'); c.setState(CellState.OCCUPIED);

        MoveResponse response = gameService.verifyMove(field);
        assertTrue(response.success() && ((ResponseSuccess) response).score() == 42);
    }

    @Test
    public void testPatternInternals() {
        final String firsContent = "А";
        Pattern pattern = new Pattern.PatternBuilder()
                .withPattern(".{0,1}" + firsContent + ".{0,1}БВ.{0,1}")
                .forDimension(DimensionEnum.COLUMN)
                .atIndex(3)
                .build();
        assertTrue(firsContent.equals(pattern.getFirstContent()));

        final String trata = "ТРАТА";
        pattern = new Pattern.PatternBuilder()
                .withPattern(".{0,3}Т.{0,3}")
                .forDimension(DimensionEnum.COLUMN)
                .atIndex(3)
                .build();
        assertTrue(pattern.getWordIndex(trata) == 3);
    }
 }
