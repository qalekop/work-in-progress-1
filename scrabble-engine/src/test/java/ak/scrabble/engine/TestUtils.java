package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.utils.ScrabbleUtils;

import java.util.List;

/**
 * Created by akopylov on 08/08/16.
 */
public class TestUtils {

    public static void printField(List<Cell> field) {
        for (int col = 0; col < Configuration.FIELD_SIZE; col++) {
            for (int row = 0; row < Configuration.FIELD_SIZE; row++) {
                Cell cell = ScrabbleUtils.getByCoords(row, col, field);
                System.out.print(cell.getState() == CellState.AVAILABLE ? "." : cell.getLetter());
            }
            System.out.println();
        }
    }
}
