package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellAvailability;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Word;
import ak.scrabble.engine.utils.WordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

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
        List<Cell> field = buildTestField();
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.COLUMN, 2));
        result.addAll(WordUtils.getWordsForDimension(field, DimensionEnum.ROW, 1));
        assertEquals(2, result.size());
        for (Word word : result) System.out.println(word);
    }

    private List<Cell> buildTestField() {
        int size = Configuration.FIELD_SIZE * Configuration.FIELD_SIZE;
        List<Cell> result = new ArrayList<>(size);
        for (int i=0; i<size; i++) result.add(new Cell());
        result.get(2).setLetter('Р'); result.get(2).setAvailability(CellAvailability.OCCUPIED);
        result.get(8).setLetter('С'); result.get(8).setAvailability(CellAvailability.OCCUPIED);
        result.get(9).setLetter('О'); result.get(9).setAvailability(CellAvailability.OCCUPIED);
        result.get(10).setLetter('К'); result.get(10).setAvailability(CellAvailability.OCCUPIED);
        result.get(16).setLetter('Г'); result.get(16).setAvailability(CellAvailability.OCCUPIED);

        return result;
    }
}
