package ak.scrabble.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by akopylov on 14/12/15.
 */
public class Configuration {

    public static final int RACK_SIZE = 7;
    public static final int FIELD_SIZE = 7;

    /** letter's score */
    private static final Map<Character, Byte> letterScores = new HashMap<>();
    /** initial bag filling */
    private static final Map<Character, Byte> letters = new HashMap<>();
    /** 'quality' of a letter - a hint while shuffling */
    private static final Map<Character, Byte> letterQualities = new HashMap<>();
    static {
        letters.put('А', (byte)8);
        letters.put('Б', (byte)2);
        letters.put('В', (byte)4);
        letters.put('Г', (byte)2);
        letters.put('Д', (byte)2);
        letters.put('Е', (byte)9);
        letters.put('Ж', (byte)1);
        letters.put('З', (byte)2);
        letters.put('И', (byte)5);
        letters.put('Й', (byte)1);
        letters.put('К', (byte)4);
        letters.put('Л', (byte)4);
        letters.put('М', (byte)3);
        letters.put('Н', (byte)5);
        letters.put('О', (byte)10);
        letters.put('П', (byte)4);
        letters.put('Р', (byte)5);
        letters.put('С', (byte)5);
        letters.put('Т', (byte)5);
        letters.put('У', (byte)4);
        letters.put('Ф', (byte)1);
        letters.put('Х', (byte)1);
        letters.put('Ц', (byte)1);
        letters.put('Ч', (byte)1);
        letters.put('Ш', (byte)1);
        letters.put('Щ', (byte)1);
        letters.put('Ъ', (byte)1);
        letters.put('Ы', (byte)2);
        letters.put('Ь', (byte)2);
        letters.put('Э', (byte)1);
        letters.put('Ю', (byte)1);
        letters.put('Я', (byte)2);
        letters.put('*', (byte)2);

        letterScores.put('А', (byte)1);
        letterScores.put('Б', (byte)3);
        letterScores.put('В', (byte)1);
        letterScores.put('Г', (byte)3);
        letterScores.put('Д', (byte)2);
        letterScores.put('Е', (byte)1);
        letterScores.put('Ж', (byte)5);
        letterScores.put('З', (byte)5);
        letterScores.put('И', (byte)1);
        letterScores.put('Й', (byte)4);
        letterScores.put('К', (byte)2);
        letterScores.put('Л', (byte)2);
        letterScores.put('М', (byte)2);
        letterScores.put('Н', (byte)1);
        letterScores.put('О', (byte)1);
        letterScores.put('П', (byte)2);
        letterScores.put('Р', (byte)1);
        letterScores.put('С', (byte)1);
        letterScores.put('Т', (byte)1);
        letterScores.put('У', (byte)2);
        letterScores.put('Ф', (byte)8);
        letterScores.put('Х', (byte)5);
        letterScores.put('Ц', (byte)5);
        letterScores.put('Ч', (byte)5);
        letterScores.put('Ш', (byte)8);
        letterScores.put('Щ', (byte)10);
        letterScores.put('Ъ', (byte)15);
        letterScores.put('Ы', (byte)4);
        letterScores.put('Ь', (byte)3);
        letterScores.put('Э', (byte)8);
        letterScores.put('Ю', (byte)8);
        letterScores.put('Я', (byte)3);

        letterQualities.put('А', (byte)10);
        letterQualities.put('Б', (byte)1);
        letterQualities.put('В', (byte)4);
        letterQualities.put('Г', (byte)1);
        letterQualities.put('Д', (byte)1);
        letterQualities.put('Е', (byte)10);
        letterQualities.put('Ж', (byte)1);
        letterQualities.put('З', (byte)1);
        letterQualities.put('И', (byte)10);
        letterQualities.put('Й', (byte)1);
        letterQualities.put('К', (byte)4);
        letterQualities.put('Л', (byte)4);
        letterQualities.put('М', (byte)4);
        letterQualities.put('Н', (byte)4);
        letterQualities.put('О', (byte)10);
        letterQualities.put('П', (byte)4);
        letterQualities.put('Р', (byte)4);
        letterQualities.put('С', (byte)4);
        letterQualities.put('Т', (byte)1);
        letterQualities.put('У', (byte)4);
        letterQualities.put('Ф', (byte)3);
        letterQualities.put('Х', (byte)3);
        letterQualities.put('Ц', (byte)1);
        letterQualities.put('Ч', (byte)4);
        letterQualities.put('Ш', (byte)1);
        letterQualities.put('Щ', (byte)1);
        letterQualities.put('Ъ', (byte)1);
        letterQualities.put('Ы', (byte)1);
        letterQualities.put('Ь', (byte)1);
        letterQualities.put('Э', (byte)3);
        letterQualities.put('Ю', (byte)3);
        letterQualities.put('Я', (byte)3);
        letterQualities.put('*', (byte)100);
    }

    public static byte getScore(final Character letter) {
        Byte result = letterScores.get(letter);
        return result != null ? result : 0;
    }

    public static byte getQuantity(final Character letter) {
        Byte result = letters.get(letter);
        return result != null ? result : 0;
    }

    public static byte getQuality(final Character letter) {
        Byte result = letterQualities.get(letter);
        return result != null ? result : 0;
    }

    public static Set<Character> getAllLetters() {
        return letters.keySet();
    }
 }

