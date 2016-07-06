package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import ak.scrabble.engine.model.ImmutableResponseError;
import ak.scrabble.engine.model.ImmutableResponseSuccess;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.Player;
import ak.scrabble.engine.utils.ScrabbleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
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

    public List<Cell> getGame(final String user) {
        List<Cell> result;
        if (!gameDAO.savedStateExists(user)) {
            LOG.debug("No prev. state found for user={}", user);
            result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
            for (int col=0; col<Configuration.FIELD_SIZE; col++) {
                for (int row=0; row<Configuration.FIELD_SIZE; row++) {
                    Cell cell = new Cell(row, col);
                    cell.setBonus(ScrabbleUtils.bonusForCell(row, col));
                    result.add(cell);
                }
            }
            Cell c = result.stream().filter(cell -> cell.getRow() == 3 && cell.getCol() == 3).findFirst().get();
            c.setState(CellState.ACCEPTED);
            c.setLetter('А');
            c.setPlayer(Player.MACHINE);
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
        // 0. merge new cells with existing ones
        List<Cell> savedCells = gameDAO.getGame(user).cells();
        for (Cell cell : cells) {
            Cell existingCell = ScrabbleUtils.getByCoords(cell.getCol(), cell.getRow(), savedCells);
            if (existingCell.getState() != CellState.AVAILABLE) {
                LOG.debug("Occupied cell: {} {}", cell.getCol(), cell.getRow());
                return ImmutableResponseError.builder()
                        .cells(Stream.of(cell).collect(Collectors.toList()))
                        .message("Misplaced tile")
                        .build();
            }
            existingCell = cell; // ???
            existingCell.setState(CellState.OCCUPIED);
        };

        // 0.5 verify if no hanging tiles
        for (Cell cell : cells) {
            Point p = new Point(cell.getCol(), cell.getRow());
            if (!ScrabbleUtils.isTraceable(p, p, savedCells)) {
                LOG.debug("Hanging cell: {} {}", cell.getCol(), cell.getRow());
                return ImmutableResponseError.builder()
                        .cells(Stream.of(cell).collect(Collectors.toList()))
                        .message("Misplaced tile")
                        .build();
            }
        }

        List<String> newWords = new ArrayList<>();
        String s;
        // 1. rows
        for (int row=0; row<Configuration.FIELD_SIZE; row++) {
            final int dim = row;
            s = savedCells.stream()
                    .filter(cell -> cell.getRow() == dim)
                    .sorted((cell1, cell2) -> Integer.valueOf(cell1.getCol()).compareTo(cell2.getCol()))
                    .map(cell -> cell.getLetter() == (char) 0 ? " " : String.valueOf(cell.getLetter()))
                    .reduce("", String::concat);
            if (StringUtils.isNotBlank(s)) newWords.add(s.trim());
        }
        // 2. cols
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            final int dim = col;
            s = savedCells.stream()
                    .filter(cell -> cell.getCol() == dim)
                    .sorted((cell1, cell2) -> Integer.valueOf(cell1.getRow()).compareTo(cell2.getRow()))
                    .map(cell -> cell.getLetter() == (char) 0 ? " " : String.valueOf(cell.getLetter()))
                    .reduce("", String::concat);
            if (StringUtils.isNotBlank(s)) newWords.add(s.trim());
        }

        // todo implement me
        newWords.stream().forEach(System.out::println);
        return ImmutableResponseSuccess.builder()
                .cells(savedCells)
                .build();
    }
}
