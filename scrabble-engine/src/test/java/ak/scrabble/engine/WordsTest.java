package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.conf.ScrabbleDbConf;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Word;
import ak.scrabble.engine.service.GameService;
import ak.scrabble.engine.utils.ScrabbleUtils;
import ak.scrabble.engine.utils.WordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
@ContextConfiguration(classes={ScrabbleDbConf.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class WordsTest {

    @Autowired
    private GameService gameService;

    @Test
    public void testMove() {
        assertTrue(gameService.verifyMove(buildTestField(CellState.OCCUPIED)));
    }

    @Test
    public void testWordsBuilder() {
        List<Word> result = new ArrayList<>();
        List<Cell> field = buildTestField(CellState.OCCUPIED);
        for (int col=0; col<Configuration.FIELD_SIZE; col++)
            result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.COLUMN, col));
        for (int row=0;row<Configuration.FIELD_SIZE; row++)
            result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.ROW, row));
        for (Word word : result) System.out.println(word);
        assertEquals(3, result.size());
    }

    @Test
    public void testTraceability() {
        List<Cell> field = buildTestField(CellState.ACCEPTED);
        Point[] p = new Point[3];
        p[0] = new Point(3, 1); p[1] = new Point(4, 1); p[2] = new Point(0, 6);

        Cell c = ScrabbleUtils.getByCoords(p[0].x, p[0].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(p[1].x, p[1].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);
        c = ScrabbleUtils.getByCoords(p[2].x, p[2].y, field);
        c.setLetter('X'); c.setState(CellState.OCCUPIED);

        assertTrue(field.stream().filter(cell -> cell.getState() == CellState.OCCUPIED).collect(Collectors.toList()).size() == 3);

        assertTrue(ScrabbleUtils.isTraceable(p[1], p[1], field));
        assertFalse(ScrabbleUtils.isTraceable(p[2], p[2], field));
    }

    private List<Cell> buildTestField(CellState state) {
        int size = Configuration.FIELD_SIZE * Configuration.FIELD_SIZE;
        List<Cell> result = new ArrayList<>(size);
        for (int col=0; col<Configuration.FIELD_SIZE; col++) {
            for (int row=0; row < Configuration.FIELD_SIZE; row++)
                result.add(new Cell(col, row));
        }

        Cell c = ScrabbleUtils.getByCoords(1, 0, result);
        c.setLetter('Р'); c.setState(state);
        c = ScrabbleUtils.getByCoords(0, 1, result);
        c.setLetter('С'); c.setState(state);
        c = ScrabbleUtils.getByCoords(1, 1, result);
        c.setLetter('О'); c.setState(state);
        c = ScrabbleUtils.getByCoords(2, 1, result);
        c.setLetter('К'); c.setState(state);
        c = ScrabbleUtils.getByCoords(1, 2, result);
        c.setLetter('Г'); c.setState(state);
        c = ScrabbleUtils.getByCoords(5, 6, result);
        c.setLetter('У'); c.setState(state);
        c = ScrabbleUtils.getByCoords(6, 6, result);
        c.setLetter('Ж'); c.setState(state);

        c = ScrabbleUtils.getByCoords(0, 0, result);
        c.setState(CellState.RESTRICTED);
        c = ScrabbleUtils.getByCoords(2, 0, result);
        c.setState(CellState.RESTRICTED);
        c = ScrabbleUtils.getByCoords(0, 2, result);
        c.setState(CellState.RESTRICTED);
        c = ScrabbleUtils.getByCoords(2, 2, result);
        c.setState(CellState.RESTRICTED);

        return result;
    }
}
