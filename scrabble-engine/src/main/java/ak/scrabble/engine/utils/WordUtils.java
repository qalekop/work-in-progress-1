package ak.scrabble.engine.utils;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Bonus;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.CellState;
import ak.scrabble.engine.model.DimensionEnum;
import ak.scrabble.engine.model.ImmutableWord;
import ak.scrabble.engine.model.Pattern;
import ak.scrabble.engine.model.Player;
import ak.scrabble.engine.model.Word;
import ak.scrabble.engine.model.WordProposal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static ak.scrabble.engine.model.DimensionEnum.COLUMN;
import static ak.scrabble.engine.model.DimensionEnum.ROW;

/**
 * Created by akopylov on 01/02/16.
 */
public class WordUtils {

    public static Set<Pattern> getPatternsForDimension(final List<Cell> field, final DimensionEnum dimension, final int index) {
        String slice = field.stream()
                .filter(cell -> dimension == ROW ? cell.getRow() == index : cell.getCol() == index)
                .map(cell -> cell.getState().free() ? " " : String.valueOf(cell.getLetter()))
                .reduce("", (a, b) -> a + b);
        Set<Pattern> patterns = new HashSet<>();
        List<String> source = Arrays.asList(StringUtils.splitByCharacterType(slice));
        int maxLength = Stream.of(StringUtils.splitByCharacterType(slice))
                .filter(StringUtils::isBlank)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);
        if (maxLength == 0) return Collections.emptySet();

        patterns.addAll(buildPatterns(source, maxLength, dimension, index));
        if (maxLength < (Configuration.FIELD_SIZE - 1)) {
            patterns.addAll(buildPatterns(source, maxLength + 1, dimension, index));
        }
        return patterns;
    }

    public static List<Word> getWordsForDimension(final List<Cell> field, final DimensionEnum dimension, final int index) {
        if (index < 0 || index > Configuration.FIELD_SIZE) throw new IllegalArgumentException("invalid index: " + index);

        List<Word> result = new ArrayList<>();
        Cell cell = dimension == COLUMN
                ? ScrabbleUtils.getByCoords(0, index, field)
                : ScrabbleUtils.getByCoords(index, 0, field);
        boolean prevState = cell.getState().free(), state;
        Word word;
        StringBuilder sb = new StringBuilder();
        List<Cell> cells = new ArrayList<>(Configuration.FIELD_SIZE);
        Bonus wordBonus = Bonus.NONE;
        int newWordScore = 0, existingWordScore = 0;
        for (int i=0; i<Configuration.FIELD_SIZE; i++) {
            cell = dimension == COLUMN
                    ? ScrabbleUtils.getByCoords(i, index, field)
                    : ScrabbleUtils.getByCoords(index, i, field);
            state = cell.getState().free();
            Bonus b = cell.getBonus();
            if (b == Bonus.WORD_2X || b == Bonus.WORD_3X) {
                if (b.compareTo(wordBonus) > 0) wordBonus = b;
            }
            if (state != prevState) {
                if (!state) {
                    // start a new word
                    char letter = cell.getLetter();
                    int score = getLetterMultiplier(b) * ScrabbleUtils.getLetterScore(letter);
                    if (cell.getState() == CellState.OCCUPIED) {
                        newWordScore += score;
                    }
                    existingWordScore += score;

                    sb.append(letter);
                    cells.add(cell);
                } else {
                    // end of a new word or single letter from another dimension
                    if (sb.length() > 1) {
                        int finalScore = wordBonus != Bonus.NONE
                                ? existingWordScore * getWordMultiplier(wordBonus)
                                : newWordScore;
                        word = ImmutableWord.builder()
                                .word(sb.toString())
                                .score(finalScore)
                                .cells(cells)
                                .player(Player.HUMAN).build();
                        result.add(word);
                    }
                    sb = new StringBuilder();
                    cells.clear();
                    newWordScore = existingWordScore = 0;
                }
                prevState = state;
            } else if (!state) {
                char letter = cell.getLetter();
                int score = getLetterMultiplier(b) * ScrabbleUtils.getLetterScore(letter);
                if (cell.getState() == CellState.OCCUPIED) {
                    newWordScore += score;
                }
                existingWordScore += score;

                sb.append(cell.getLetter());
                cells.add(cell);
            }
        }
        if (sb.length() > 1) {
            int finalScore = wordBonus != Bonus.NONE
                    ? existingWordScore * getWordMultiplier(wordBonus)
                    : newWordScore;
            word = ImmutableWord.builder()
                    .word(sb.toString())
                    .score(finalScore)
                    .cells(cells)
                    .player(Player.HUMAN).build();
            result.add(word);
        }
        return result;
    }

    public static int scoreWord(List<Cell> field, WordProposal proposal) {
        return putWord(field, proposal, null).getLeft();
    }

    public static Pair<Integer, String> putWord(List<Cell> field, WordProposal proposal, String rack) {
        Pattern pattern = proposal.getPattern();
        String slice = field.stream()
                .filter(cell -> pattern.getDimension() == ROW ? cell.getRow() == pattern.getIndex() : cell.getCol() == pattern.getIndex())
                .map(cell -> cell.getState().free() ? " " : String.valueOf(cell.getLetter()))
                .reduce("", (a, b) -> a + b);

        String content = pattern.getFirstContent();
        String word = proposal.getWord().toUpperCase();
        int shift = slice.indexOf(content) - proposal.getPattern().getWordIndex(word);
        int x = -1, y = -1;
        Bonus wordBonus = Bonus.NONE;
        int newWordScore = 0, existingWordScore = 0;
        String tempRack = rack;
        for (int i=0; i<word.length(); i++) {
            switch (pattern.getDimension()) {
            case COLUMN:
                x = pattern.getIndex();
                y = shift + i;
                break;
            case ROW:
                x = shift + i;
                y = pattern.getIndex();
                break;
            }
            Cell cell = ScrabbleUtils.getByCoords(x, y, field);
            char letter = word.charAt(i);
            Bonus b = cell.getBonus();
            if (b == Bonus.WORD_2X || b == Bonus.WORD_3X) {
                if (b.compareTo(wordBonus) > 0) wordBonus = b;
            }
            int score = getLetterMultiplier(b) * ScrabbleUtils.getLetterScore(letter);
            if (cell.getState().free()) {
                if (tempRack != null) {
                    tempRack = removeLetter(tempRack, letter);
                    cell.setLetter(letter);
                    cell.setState(CellState.OCCUPIED);
                }
                newWordScore += score;
            }
            existingWordScore += score;
        }
        int finalScore = wordBonus != Bonus.NONE
                ? existingWordScore * getWordMultiplier(wordBonus)
                : newWordScore;
        return new ImmutablePair(finalScore, tempRack);
    }

    private static String removeLetter(String source, char letter) {
        int index = source.indexOf(letter);
        if (index < 0) return source;
        return source.substring(0, index) + source.substring(index + 1);
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

    public static int getWordMultiplier(Bonus bonus) {
        switch (bonus) {
        case WORD_3X:
            return 3;
        case WORD_2X:
            return 2;
        default:
            return 1;
        }
    }

    public static int getLetterMultiplier(Bonus bonus) {
        switch (bonus) {
        case LETTER_3X:
            return 3;
        case LETTER_2X:
            return 2;
        default:
            return 1;
        }
    }
}
