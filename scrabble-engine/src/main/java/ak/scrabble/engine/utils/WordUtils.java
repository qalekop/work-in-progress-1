package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.*;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * Created by akopylov on 01/02/16.
 */
public class WordUtils {

    public static Set<Pattern> getPatternsForDimension(final List<Cell> field, final DimensionEnum dimension, final int index) {
        String slice = field.stream()
                .filter(cell -> dimension == DimensionEnum.ROW ? cell.getRow() == index : cell.getCol() == index)
                .map(cell -> cell.getState().free() ? " " : String.valueOf(cell.getLetter()))
                .reduce("", (a, b) -> a + b);
        Set<Pattern> patterns = new HashSet<>();
        List<String> source = Arrays.asList(StringUtils.splitByCharacterType(slice));
        int maxLength = 0;
        maxLength = Stream.of(StringUtils.splitByCharacterType(slice))
                .filter(StringUtils::isBlank)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .get();

        patterns.addAll(buildPatterns(source, maxLength, dimension, index));
        patterns.addAll(buildPatterns(source, maxLength + 1, dimension, index));
        return patterns;
    }

    public static List<Word> getWordsForDimension(final List<Cell> field, final DimensionEnum dimension, final int index) {
        if (index < 0 || index > Configuration.FIELD_SIZE) throw new IllegalArgumentException("invalid index: " + index);

        List<Word> result = new ArrayList<>();
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
                    if (!cell.getState().free()) {
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
                if (!cell.getState().free()) {
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

    private static List<Pattern> buildPatterns(List<String> source, int minLength, DimensionEnum dimension, int dimIndex) {
        List<Pattern> result = new ArrayList<>();
        String pattern = "";
        int blankLength;
        String s;
        Iterator<String> iterator = source.iterator();
        while (iterator.hasNext()) {
            s = iterator.next();
            if (StringUtils.isNoneBlank(s)) {
                pattern += s;
                if (iterator.hasNext()) {
                    s = iterator.next();
                } else {
                    result.add(new Pattern.PatternBuilder().withPattern(pattern)
                            .forDimension(dimension)
                            .atIndex(dimIndex)
                            .build());
                    pattern = "";
                }
            }
            blankLength = s.length();
            if (StringUtils.isBlank(pattern)) {
                pattern += (".{0," + blankLength + "}");
            } else if (!iterator.hasNext()) {
                pattern += ".{0," + blankLength + "}";
                result.add(new Pattern.PatternBuilder().withPattern(pattern)
                        .forDimension(dimension)
                        .atIndex(dimIndex)
                        .build());
            } else if (blankLength >= minLength) {
                if (blankLength == 1) {
                    result.add(new Pattern.PatternBuilder().withPattern(pattern)
                            .forDimension(dimension)
                            .atIndex(dimIndex)
                            .build());
                    pattern = "";
                } else {
                    pattern += (".{0," + (blankLength - 1) + "}");
                    result.add(new Pattern.PatternBuilder().withPattern(pattern)
                            .forDimension(dimension)
                            .atIndex(dimIndex)
                            .build());
                    pattern = ".{0," + (blankLength - 1) + "}";
                }
            } else {
                pattern += ".{" + blankLength + "}";
            }
        }
        return result;
    }
}
