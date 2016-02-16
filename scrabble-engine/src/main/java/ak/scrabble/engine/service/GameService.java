package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.utils.ScrabbleUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akopylov on 15/02/16.
 */
@Service
public class GameService {
    public List<Cell> getGame(final String user) {
        // todo implement me
        List<Cell> result = new ArrayList<>(Configuration.FIELD_SIZE * Configuration.FIELD_SIZE);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row<Configuration.FIELD_SIZE; row++) {
                Cell cell = new Cell(row, col);
                cell.setBonus(ScrabbleUtils.bonusForCell(row, col));
                result.add(cell);
            }
        }
        return result;
    }
}
