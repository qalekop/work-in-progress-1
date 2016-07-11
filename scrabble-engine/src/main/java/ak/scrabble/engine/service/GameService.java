package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import ak.scrabble.engine.model.ImmutableResponseError;
import ak.scrabble.engine.model.ImmutableResponseSuccess;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.Player;
import ak.scrabble.engine.model.Word;
import ak.scrabble.engine.rules.RulesService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Cell> getGame(final String user) {
        List<Cell> result;
        if (!gameDAO.savedStateExists(user)) {
            LOG.debug("No prev. state found for user={}", user);
            result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
            for (int col=0; col<Configuration.FIELD_SIZE; col++) {
                for (int row=0; row<Configuration.FIELD_SIZE; row++) {
                    Cell cell = new Cell(col, row);
                    cell.setBonus(ScrabbleUtils.bonusForCell(col, row));
                    result.add(cell);
                }
            }
            // todo code below only for dev purposes !!!
            Cell c = result.stream().filter(cell -> cell.getRow() == 3 && cell.getCol() == 3).findFirst().get();
            c.setState(CellState.MACHINE);
            c.setLetter('А');
            Game game = ImmutableGame.builder()
                    .cells(result).score(new ImmutablePair<>(0, 0))
                    .build();
            gameDAO.persistGame(user, game, true);
        } else {
            result = gameDAO.getGame(user).cells();
        }
        return result;
    }

    public MoveResponse processHumanMove(final String user, final List<Cell> cells) {
        // merge new cells with existing ones
        List<Cell> savedCells = gameDAO.getGame(user).cells();
        for (Cell cell : cells) {
            Cell existingCell = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), savedCells);
            if (existingCell.getState() != CellState.AVAILABLE) {
                LOG.debug("Occupied cell: {} {}", cell.getCol(), cell.getRow());
                existingCell.setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .cells(savedCells)
                        .message("Misplaced tile")
                        .build();
            }
            existingCell.setLetter(cell.getLetter());
            existingCell.setState(CellState.OCCUPIED);
        };

        // verify if no hanging tiles
        for (Cell cell : cells) {
            Point p = new Point(cell.getCol(), cell.getRow());
            if (!ScrabbleUtils.isTraceable(p, p, savedCells)) {
                LOG.debug("Hanging cell: {} {}", cell.getCol(), cell.getRow());
                ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), savedCells).setState(CellState.REJECTED);
                return ImmutableResponseError.builder()
                        .cells(savedCells)
                        .message("Misplaced tile")
                        .build();
            }
        }

        return verifyMove(savedCells);
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
        for (Word w : newWords) {
            String _w = w.word();
            if(!rulesService.valid(_w)) {
                LOG.debug("Wrong word: {}", _w);
                w.cells().forEach(cell -> {
                    Cell c = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), cells);
                    if (c.getState() == CellState.OCCUPIED) {
                        c.setState(CellState.REJECTED);
                    }
                });
                return ImmutableResponseError.builder()
                        .message("Wrong word: " + _w)
                        .cells(cells)
                        .build();
            }
        }
        // todo implement me - save new words ?
        newWords.stream().forEach(System.out::println);

        return ImmutableResponseSuccess.builder()
                .cells(cells.stream()
                        .map(cell -> {
                            if (cell.getState() == CellState.OCCUPIED) {
                                cell.setState(CellState.HUMAN);
                            }
                            return cell;
                        }).collect(Collectors.toList())
                ).build();
    }
}
