package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellAvailability;
import ak.scrabble.engine.utils.ScrabbleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 15/02/16.
 */
@Service
public class GameService {
    public List<Cell> getGame(final String user) {
        // todo implement me - retrieve user state from a db
        List<Cell> result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row<Configuration.FIELD_SIZE; row++) {
                Cell cell = new Cell(row, col);
                cell.setBonus(ScrabbleUtils.bonusForCell(row, col));
                result.add(cell);
            }
        }
        // for the first move - make central cell obligatory
        result.get((Configuration.FIELD_SIZE / 2) * Configuration.FIELD_SIZE + (Configuration.FIELD_SIZE / 2))
                .setAvailability(CellAvailability.ALLOWED);
        return result;
    }

    public boolean processHumanMove(final String user, final List<Cell> cells) {
        List<String> newWords = new ArrayList<>();
        String s;
        // 1. rows
        for (int row=0; row<Configuration.FIELD_SIZE; row++) {
            final int dim = row;
            s = cells.stream()
                    .filter(cell -> cell.getRow() == dim)
                    .sorted((cell1, cell2) -> Integer.valueOf(cell1.getCol()).compareTo(cell2.getCol()))
                    .map(cell -> cell.getLetter() == (char) 0 ? " " : String.valueOf(cell.getLetter()))
                    .reduce("", String::concat);
            if (StringUtils.isNotBlank(s)) newWords.add(s.trim());
        }
        // 2. cols
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            final int dim = col;
            s = cells.stream()
                    .filter(cell -> cell.getCol() == dim)
                    .sorted((cell1, cell2) -> Integer.valueOf(cell1.getRow()).compareTo(cell2.getRow()))
                    .map(cell -> cell.getLetter() == (char) 0 ? " " : String.valueOf(cell.getLetter()))
                    .reduce("", String::concat);
            if (StringUtils.isNotBlank(s)) newWords.add(s.trim());
        }

        // todo implement me
        newWords.stream().forEach(System.out::println);
        return true;
    }
}
