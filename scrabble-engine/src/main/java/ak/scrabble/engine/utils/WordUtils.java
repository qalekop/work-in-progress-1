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
            int pos = translateIndex(i, index, dimension);
            cell = field.get(pos);
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

    public static boolean isTraceable(Point start, Point origin, List<Cell> cells) {
        if (cells.get(translateIndex(start.x, start.y, DimensionEnum.ROW)).getState() != CellState.OCCUPIED) {
            throw new IllegalStateException(String.format("Unexpected state: cell[%d, %d] = %s",
                    start.x, start.y, cells.get(translateIndex(start.x, start.y, DimensionEnum.ROW)).getState()));
        }
        if (cells.get(translateIndex(origin.x, origin.y, DimensionEnum.ROW)).getState() == CellState.ACCEPTED) {
            return true;
        }
        Stack<Point> neighbors = new Stack<>();
        // up
        if (start.y > 0) {
            if (push(new Point(start.x, start.y - 1), origin, cells, neighbors)) return true;
        }
        // right
        if (start.x < (Configuration.FIELD_SIZE - 1)) {
            if (push(new Point(start.x + 1, start.y), origin, cells, neighbors)) return true;
        }
        // bottom
        if (start.y < (Configuration.FIELD_SIZE - 1)) {
            if (push(new Point(start.x, start.y + 1), origin, cells, neighbors)) return true;
        }
        // left
        if (start.x > 0) {
            if (push(new Point(start.x - 1, start.y), origin, cells, neighbors)) return true;
        }

        while (!neighbors.empty()) {
            Point p = neighbors.pop();
            if (cells.get(translateIndex(p.x, p.y, DimensionEnum.ROW)).getState() == CellState.ACCEPTED) return true;
            if (isTraceable(p, start, cells)) return true;
        }
        return false;
    }

    private static boolean push(Point cell, Point origin, List<Cell> cells, Stack<Point> stack) {
        if (!cell.equals(origin)) {
            Cell c = cells.get(translateIndex(cell.x, cell.y, DimensionEnum.ROW));
            if (c.getState() == CellState.OCCUPIED) {
                stack.push(cell);
            } else if (c.getState() == CellState.ACCEPTED) {
                return true;
            }
        }
        return false;
    }

    public static int translateIndex(final int row, final int column, final DimensionEnum dim) {
        switch (dim) {
            case COLUMN: return row * Configuration.FIELD_SIZE + column;
            case ROW: return column * Configuration.FIELD_SIZE + row;
        }
        throw new IllegalArgumentException("wrong dimension" + dim);
    }
}
