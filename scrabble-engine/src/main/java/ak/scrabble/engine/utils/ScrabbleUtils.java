package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Bonus;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Created by akopylov on 15/02/16.
 */
public class ScrabbleUtils {

    private static Bonus bonusForCell(int cellNumber) {
        assert(7 == Configuration.FIELD_SIZE);

        switch (cellNumber) {
            case 0:
            case 3:
            case 6:
            case 21:
            case 27:
            case 42:
            case 45:
            case 48: return Bonus.WORD_3X;

            case 8:
            case 12:
            case 36:
            case 40: return Bonus.WORD_2X;

            case 10:
            case 22:
            case 26:
            case 38: return Bonus.LETTER_3X;

            case 16:
            case 18:
            case 30:
            case 32: return Bonus.LETTER_2X;

            default: return Bonus.NONE;
        }
    }

    public static Bonus bonusForCell(int row, int column) {
        return bonusForCell(column + Configuration.FIELD_SIZE * row);
    }

    public static Cell getByCoords(int col, int row, List<Cell> cells) {
        Optional<Cell> cell = cells.stream()
                .filter(_cell -> _cell.getCol() == col && _cell.getRow() == row)
                .findFirst();
        if (cell.isPresent()) {
            return cell.get();
        } else {
            throw new IllegalArgumentException("Wrong coords: col=" + col + "; row=" + row);
        }
    }

    public static boolean isTraceable(Point start, Point origin, List<Cell> cells) {
        if (getByCoords(start.x, start.y, cells).getState() != CellState.OCCUPIED) {
            throw new IllegalStateException(String.format("Unexpected state: cell[%d, %d] = %s",
                    start.x, start.y, getByCoords(start.x, start.y, cells).getState()));
        }
        if (getByCoords(start.x, start.y, cells).getState() == CellState.ACCEPTED) {
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
            if (getByCoords(p.x, p.y, cells).getState() == CellState.ACCEPTED) return true;
            if (isTraceable(p, start, cells)) return true;
        }
        return false;
    }

    private static boolean push(Point cell, Point origin, List<Cell> cells, Stack<Point> stack) {
        if (!cell.equals(origin)) {
            Cell c = getByCoords(cell.x, cell.y, cells);
            if (c.getState() == CellState.OCCUPIED) {
                stack.push(cell);
            } else if (c.getState() == CellState.ACCEPTED) {
                return true;
            }
        }
        return false;
    }
}
