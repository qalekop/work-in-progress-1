package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Word;
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

/**
 * Created by akopylov on 02/02/16.
 */
@RunWith(JUnit4.class)
public class WordsTest {

    @Test
    public void testWordsBuilder() {
        List<Word> result = new ArrayList<>(2);
        List<Cell> field = buildTestField(CellState.OCCUPIED);
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.COLUMN, 2));
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.ROW, 1));
        assertEquals(2, result.size());
        for (Word word : result) System.out.println(word);
    }

    @Test
    public void testTraceability() {
        List<Cell> field = buildTestField(CellState.ACCEPTED);
        Point[] p = new Point[3];
        p[0] = new Point(0, 1); p[1] = new Point(0, 2); p[2] = new Point(5, 1);
        int index = WordUtils.translateIndex(p[0].y, p[0].x, DimensionEnum.COLUMN);
        field.get(index).setLetter('X'); field.get(index).setState(CellState.OCCUPIED);
        index = WordUtils.translateIndex(p[1].y, p[1].x, DimensionEnum.COLUMN);
        field.get(index).setLetter('X'); field.get(index).setState(CellState.OCCUPIED);
        index = WordUtils.translateIndex(p[2].y, p[2].x, DimensionEnum.COLUMN);
        field.get(index).setLetter('X'); field.get(index).setState(CellState.OCCUPIED);

        assertTrue("Oops!", WordUtils.isTraceable(p[1], p[1], field));
        assertFalse("Oops!", WordUtils.isTraceable(p[2], p[2], field));
    }

    private List<Cell> buildTestField(CellState state) {
        int size = Configuration.FIELD_SIZE * Configuration.FIELD_SIZE;
        List<Cell> result = new ArrayList<>(size);
        for (int i=0; i<size; i++) result.add(new Cell());
        result.get(2).setLetter('Р'); result.get(2).setState(state);
        result.get(8).setLetter('С'); result.get(8).setState(state);
        result.get(9).setLetter('О'); result.get(9).setState(state);
        result.get(10).setLetter('К'); result.get(10).setState(state);
        result.get(16).setLetter('Г'); result.get(16).setState(state);

        return result;
    }
}
