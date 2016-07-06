package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.Word;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by akopylov on 01/02/16.
 */
public class WordUtils {
    public static List<Word> getWordsForDimension(final List<Cell> field, final DimensionEnum dimension, final int index) {
        if (index < 0 || index > Configuration.FIELD_SIZE) throw new IllegalArgumentException("invalid index: " + index);

        List<Word> result = new ArrayList<>(Configuration.FIELD_SIZE);
        CellState prevState = CellState.AVAILABLE;
        CellState state;
        Cell cell;
        Word word;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<Configuration.FIELD_SIZE; i++) {
            cell = dimension == DimensionEnum.COLUMN
                    ? ScrabbleUtils.getByCoords(i, index, field)
                    : ScrabbleUtils.getByCoords(index, i, field);
            state = cell.getState();
            if (state != prevState) {
                if (state == CellState.OCCUPIED) {
                    // start of a new word
                    sb.append(cell.getLetter());
                } else {
                    // end of a new word
                    word = new Word();
                    word.setWord(sb.toString());
                    sb = new StringBuilder();
                    result.add(word);
                }
                prevState = state;
            } else if (state == CellState.OCCUPIED) {
                sb.append(cell.getLetter());
            }
        }
        return result;
    }

}
