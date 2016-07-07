package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Word;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akopylov on 02/02/16.
 */
@RunWith(JUnit4.class)
public class WordsTest {

    @Test
    public void testWordsBuilder() {
        List<Word> result = new ArrayList<>(2);
        List<Cell> field = buildTestField(CellState.OCCUPIED);
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.COLUMN, 3));
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.ROW, 3));
        assertEquals(2, result.size());
        for (Word word : result) System.out.println(word);
    }

    @Test
    public void testTraceability() {
        List<Cell> field = buildTestField(CellState.ACCEPTED);
        Point[] p = new Point[3];
        p[0] = new Point(1, 3); p[1] = new Point(1, 4); p[2] = new Point(1, 1);

        Cell c = ScrabbleUtils.getByCoords(p[0].x, p[0].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(p[1].x, p[1].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(p[2].x, p[2].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);

        assertTrue("Oops!",
                field.stream().filter(cell -> cell.getState() == CellState.OCCUPIED).collect(Collectors.toList()).size() == 3);

        assertTrue("Oops!", ScrabbleUtils.isTraceable(p[1], p[1], field));
        assertFalse("Oops!", ScrabbleUtils.isTraceable(p[2], p[2], field));
    }

    private List<Cell> buildTestField(CellState state) {
        int size = Configuration.FIELD_SIZE * Configuration.FIELD_SIZE;
        List<Cell> result = new ArrayList<>(size);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row < Configuration.FIELD_SIZE; row++)
                result.add(new Cell(col, row));
        }

        Cell c = ScrabbleUtils.getByCoords(3, 2, result);
        c.setLetter('Р'); c.setState(state);
        c = ScrabbleUtils.getByCoords(2, 3, result);
        c.setLetter('С'); c.setState(state);
        c = ScrabbleUtils.getByCoords(3, 3, result);
        c.setLetter('О'); c.setState(state);
        c = ScrabbleUtils.getByCoords(4, 3, result);
        c.setLetter('К'); c.setState(state);
        c = ScrabbleUtils.getByCoords(3, 4, result);
        c.setLetter('Г'); c.setState(state);

        return result;
    }
}
