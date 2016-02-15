package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Bonus;

/**
 * Created by akopylov on 15/02/16.
 */
public class ScrabbleUtils {

    public static Bonus bonusForCell(int cellNumber) {
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
}
