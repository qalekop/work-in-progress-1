package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.*;
import ak.scrabble.engine.rules.RulesService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 15/02/16.
 */
@Service
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private GameDAO gameDAO;

    @Autowired
    private RulesService rulesService;

    @Autowired
    private DictService dictService;

    @Autowired
    private BagService bagService;

    @Autowired
    private RackService rackService;

    public Game getGame(final String user) throws SQLException {
        Game game;
        List<Cell> cells;
        if (!gameDAO.savedStateExists(user)) {
            LOG.debug("No prev. state found for user={}", user);
            cells = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
            for (int col=0; col<Configuration.FIELD_SIZE; col++) {
                for (int row=0; row<Configuration.FIELD_SIZE; row++) {
                    Cell cell = new Cell(col, row);
                    cell.setBonus(ScrabbleUtils.bonusForCell(col, row));
                    cells.add(cell);
                }
            }
            // todo 3 lines below only for dev purposes !!!
            Cell c = cells.stream().filter(cell -> cell.getRow() == 3 && cell.getCol() == 3).findFirst().get();
            c.setState(CellState.MACHINE);
            c.setLetter('А');

            List<Character> bag = bagService.initBag();
            List<Tile> rackHuman = rackService.getRack(bag, StringUtils.EMPTY);
            List<Tile> rackMachine = rackService.getRack(bag, StringUtils.EMPTY);
            game = ImmutableGame.builder()
                    .cells(cells)
                    .scoreHuman(0).scoreMachine(0)
                    .rackHuman(rackHuman).rackMachine(rackMachine)
                    .bag(bag)
                    .build();
            gameDAO.persistGame(user, game, true);
        } else {
            game = gameDAO.getGame(user);
        }
        return game;
    }

    public MoveResponse processHumanMove(final String user, final List<Cell> cells) {
        // merge new cells with existing ones
        List<Cell> existingCells = gameDAO.getGame(user).cells();
        for (Cell cell : cells) {
            Cell existingCell = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), existingCells);
            if (existingCell.getState() != CellState.AVAILABLE) {
                LOG.debug("Occupied cell: {} {}", cell.getCol(), cell.getRow());
                existingCell.setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .cells(cells)   // todo better to report only the misplaced tile(s)
                        .message("Misplaced tile")
                        .build();
            }
            existingCell.setLetter(cell.getLetter());
            existingCell.setState(CellState.OCCUPIED);
        };

        // verify if no hanging tiles
        for (Cell cell : cells) {
            Point p = new Point(cell.getCol(), cell.getRow());
            if (!ScrabbleUtils.isTraceable(p, p, existingCells)) {
                LOG.debug("Hanging cell: {} {}", cell.getCol(), cell.getRow());
                ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), existingCells).setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .cells(cells)   // todo better to report only the misplaced tile
                        .message("Misplaced tile")
                        .build();
            }
        }
        return verifyMove(existingCells);
    }

    public MoveResponse verifyMove(List<Cell> cells) {
        List<Word> newWords = new ArrayList<>();
        // 1. rows
        for (int row=0; row<Configuration.FIELD_SIZE; row++) {
            newWords.addAll(WordUtils.getWordsForDimension(cells, DimensionEnum.ROW, row));
        }
        // 2. cols
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            newWords.addAll(WordUtils.getWordsForDimension(cells, DimensionEnum.COLUMN, col));
        }
        for (Word word : newWords) {
            String w = word.word();
            if(!rulesService.valid(w)) {
                LOG.debug("Wrong word: {}", w);
                word.cells().forEach(cell -> {
                    Cell c = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), cells);
                    if (c.getState() == CellState.OCCUPIED) {
                        c.setState(CellState.REJECTED);
                    }
                });
                return ImmutableResponseError.builder()
                        .message("Wrong word: " + w)
                        .cells(cells)   // todo better to report only the wrong word
                        .build();
            }
        }
        int score = newWords.stream().map(Word::score).reduce(0, (a, b) -> (a + b));
        // todo implement me - save new words ?
        newWords.stream().forEach(System.out::println);

        // todo List<Cell> below only for demo purposes - to return Machine Move instead !
        return ImmutableResponseSuccess.builder()
                .score(score)
                .cells(cells.stream()
                        .map(cell -> {
                            if (cell.getState() == CellState.OCCUPIED) {
                                cell.setState(CellState.HUMAN);
                            }
                            return cell;
                        }).collect(Collectors.toList())
                ).build();
    }

    public List<WordProposal> findProposals(final List<Cell> field, String rack) {
        List<WordProposal> result = new ArrayList<>();
        // 1. rows
        for (int row=0; row<Configuration.FIELD_SIZE; row++) {
            for (Pattern pattern : WordUtils.getPatternsForDimension(field, DimensionEnum.ROW, row)) {
                SearchSpec spec = ImmutableSearchSpec.builder()
                        .pattern(pattern)
                        .regexp(true)
                        .rack(rack)
                        .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                        .build();
                result.addAll(dictService.findPossibleWords(spec));
            }
        }
        // 2. columns
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (Pattern pattern : WordUtils.getPatternsForDimension(field, DimensionEnum.COLUMN, col)) {
                SearchSpec spec = ImmutableSearchSpec.builder()
                        .pattern(pattern)
                        .regexp(true)
                        .rack(rack)
                        .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                        .build();
                result.addAll(dictService.findPossibleWords(spec));
            }
        }
        result.forEach(proposal -> proposal.setScore(WordUtils.scoreWord(field, proposal)));
        return result.stream()
                .sorted((o1, o2) -> Integer.valueOf(o2.getScore()).compareTo(o1.getScore()))
                .collect(Collectors.toList());
    }
}
