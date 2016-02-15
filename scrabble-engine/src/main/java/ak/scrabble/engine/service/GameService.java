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
        final int length = Configuration.FIELD_SIZE * Configuration.FIELD_SIZE;
        List<Cell> result = new ArrayList<>(length);
        for (int i=0; i<length; i++) {
            Cell cell = new Cell();
            cell.setBonus(ScrabbleUtils.bonusForCell(i));
            result.add(cell);
        }
        return result;
    }
}
