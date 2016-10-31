package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.*;
import ak.scrabble.engine.rules.RulesService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;
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

import static ak.scrabble.engine.da.GameDAO.Mode.*;


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
            // FIXME 3 lines below only for dev purposes !!!
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
            gameDAO.persistGame(user, game, CREATE);
        } else {
            game = gameDAO.getGame(user);
        }
        return game;
    }

    public MoveResponse processHumanMove(final String user, final List<Cell> newCells) throws SQLException {
        // merge new cells with existing ones
        List<Cell> cells = gameDAO.getGame(user).cells();
        for (Cell cell : newCells) {
            Cell existingCell = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), cells);
            if (existingCell.getState() != CellState.AVAILABLE) {
                LOG.debug("Occupied cell: {} {}", cell.getCol(), cell.getRow());
                existingCell.setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .message("Misplaced tile")
                        .build();
            }
            existingCell.setLetter(cell.getLetter());
            existingCell.setState(CellState.OCCUPIED);
        };

        // verify if no hanging tiles
        for (Cell cell : newCells) {
            Point p = new Point(cell.getCol(), cell.getRow());
            if (!ScrabbleUtils.isTraceable(p, p, cells)) {
                LOG.debug("Hanging cell: {} {}", cell.getCol(), cell.getRow());
                ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), cells).setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .message("Misplaced tile")
                        .build();
            }
        }
        MoveResponse response = verifyMove(cells);
        if (response.success()) {
            // todo save new words in a dedicated (yet non-existing) table
            // 1. update field
            cells.stream()
                    .filter(cell -> cell.getState() == CellState.OCCUPIED)
                    .forEach(cell -> cell.setState(CellState.HUMAN));

            // 2. calculate new human's rack
            Game game = getGame(user);
            List<Character> bag = new ArrayList<>(game.bag());
            List<String> usedLetters = newCells.stream().map(cell -> String.valueOf(cell.getLetter())).collect(Collectors.toList());
            List<Tile> rackHuman = rackService.getRack(bag, game.rackHuman(), usedLetters);

            // 3. save field
            // todo machine's rack? scores?
            game = ImmutableGame.builder()
                    .cells(cells)
                    .scoreHuman(0).scoreMachine(0)
                    .rackHuman(rackHuman).rackMachine(game.rackMachine())
                    .bag(bag)
                    .build();
            gameDAO.persistGame(user, game, UPDATE);
        }
        return response;
    }

    private MoveResponse _verifyMove(List<Cell> cells) {
        return ImmutableResponseSuccess.builder()
                .score(0)
                .build();
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
                return ImmutableResponseError.builder()
                        .message("Wrong word: " + w)
                        .build();
            }
        }
        int score = newWords.stream().map(Word::score).reduce(0, (a, b) -> (a + b));
        return ImmutableResponseSuccess.builder()
                .score(score)
                .build();
    }

    @SuppressWarnings("unchecked")
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

    public int processMachineMove(String user) throws SQLException {
        // 1. get game state. Assuming it exists for the given user
        Game game = gameDAO.getGame(user);
        String rack = game.rackMachine().stream().map(tile -> String.valueOf(tile.getLetter())).reduce("", (a, b) -> a + b);
        List<Cell> field = game.cells();
        List<Character> bag = new ArrayList<>(game.bag());

        // 2. machine move
        List<WordProposal> proposals = findProposals(field, rack);
        // FIXME check if proposals aren't empty
        int score = 0;
        int indexOfNextProposal = 0;
        do {
            WordProposal proposal = proposals.get(indexOfNextProposal);
            Pair<Integer, String> result = WordUtils.putWord(field, proposal, rack);
            MoveResponse response = verifyMove(field);
            if (response.success()) {
                score += result.getLeft();
                rack = result.getRight();
                field.stream()
                        .filter(cell -> cell.getState() == CellState.OCCUPIED)
                        .forEach(cell -> cell.setState(CellState.MACHINE));
                proposals = findProposals(field, rack);
            } else {
                field.stream()
                        .filter(cell -> cell.getState() == CellState.OCCUPIED)
                        .forEach(cell -> cell.setState(CellState.AVAILABLE));
                indexOfNextProposal++;
                if (indexOfNextProposal >= (proposals.size()) - 1) {
                    break;
                }
            }
        } while (!CollectionUtils.isEmpty(proposals) && StringUtils.isNotBlank(rack));

        // 3. refill the machine rack
        List<Tile> newRack = rackService.getRack(bag, rack);

        // 4. build the new game state and persist it
        Game newGameState = ImmutableGame.builder()
                .cells(field)
                .scoreHuman(game.scoreHuman()).scoreMachine(score)
                .rackHuman(game.rackHuman()).rackMachine(newRack)
                .bag(bag)
                .build();
        gameDAO.persistGame(user, newGameState, UPDATE);
        return score; // todo really score?
    }
}
