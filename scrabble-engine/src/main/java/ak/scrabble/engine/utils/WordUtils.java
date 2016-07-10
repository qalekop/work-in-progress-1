package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.*;

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
        Cell cell = dimension == DimensionEnum.COLUMN
                ? ScrabbleUtils.getByCoords(0, index, field)
                : ScrabbleUtils.getByCoords(index, 0, field);
        boolean prevState = cell.getState().free();
        boolean state;
        Word word;
        StringBuilder sb = new StringBuilder();
        List<Cell> cells = new ArrayList<>(Configuration.FIELD_SIZE);
        for (int i=0; i<Configuration.FIELD_SIZE; i++) {
            cell = dimension == DimensionEnum.COLUMN
                    ? ScrabbleUtils.getByCoords(i, index, field)
                    : ScrabbleUtils.getByCoords(index, i, field);
            state = cell.getState().free();
            if (state != prevState) {
                if (!state) {
                    // start of a new word
                    sb.append(cell.getLetter());
                    if (cell.getState() == CellState.OCCUPIED) {
                        cells.add(cell);
                    }
                } else {
                    // end of a new word or single letter from another dimension
                    if (sb.length() > 1) {
                        word = ImmutableWord.builder().word(sb.toString()).cells(cells).player(Player.HUMAN).build();
                        result.add(word);
                    }
                    sb = new StringBuilder();
                    cells.clear();
                }
                prevState = state;
            } else if (!state) {
                sb.append(cell.getLetter());
                if (cell.getState() == CellState.OCCUPIED) {
                    cells.add(cell);
                }
            }
        }
        if (sb.length() > 1) {
            word = ImmutableWord.builder().word(sb.toString()).cells(cells).player(Player.HUMAN).build();
            result.add(word);
        }
        return result;
    }

}
