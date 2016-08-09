package ak.scrabble.engine.model;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

/**
 * Created by akopylov on 01/08/16.
 */
public class Pattern {

    private static final String GROUP_CONTENT = "CONTENT";
    private static final java.util.regex.Pattern STRIP_PATTERN = java.util.regex.Pattern.compile("[^А-Я]*(?<"
            + GROUP_CONTENT + ">[А-Я]+).*");

    /** search pattern itself */
    private String pattern;
    /** pattern allocation - along ROW or COL */
    private DimensionEnum dimension;
    /** pattern's ROW or COL index */
    private int index;

    //<editor-fold desc="Getters-'n'-Setters">
    public String getPattern() {
        return pattern;
    }

    private void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public DimensionEnum getDimension() {
        return dimension;
    }

    private void setDimension(DimensionEnum dimension) {
        this.dimension = dimension;
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    public String getLetters() {
        return pattern.replaceAll("[^А-Я]", StringUtils.EMPTY);
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pattern pattern1 = (Pattern) o;

        if (!pattern.equals(pattern1.pattern)) return false;
        return dimension == pattern1.dimension;

    }

    @Override
    public int hashCode() {
        int result = pattern.hashCode();
        result = 31 * result + dimension.hashCode();
        return result;
    }

    public String getFirstContent() {
        Matcher m = STRIP_PATTERN.matcher(pattern);
        if (!m.matches()) {
            throw new IllegalStateException("internal pattern mismatch:" + pattern);
        }
        return m.group(GROUP_CONTENT);
    }

    private String getWordMatchingPattern() {
        Matcher m = STRIP_PATTERN.matcher(pattern);
        if (!m.matches()) {
            throw new IllegalStateException("internal pattern mismatch:" + pattern);
        }
        String content = m.group(GROUP_CONTENT);
        return pattern.replaceFirst(content, m.replaceFirst("(?<" + GROUP_CONTENT + ">" + content + ")"));
    }

    public int getWordIndex(String word) {
        Matcher m = java.util.regex.Pattern.compile(getWordMatchingPattern()).matcher(word);
        if (m.matches()) {
            return m.start(Pattern.GROUP_CONTENT);
        } else {
            throw new IllegalStateException("internal pattern mismatch:" + pattern + " for word:" + word);
        }
    }

    public static class PatternBuilder {

        private static final String PREFIX = "^";
        private static final String SUFFIX = "$";

        private String pattern;
        private DimensionEnum dimension;
        private int index;

        public PatternBuilder withPattern(String pattern) {
            this.pattern = PREFIX + pattern + SUFFIX;
            return this;
        }

        public PatternBuilder withWord(String word) {
            this.pattern = word;
            return this;
        }

        public PatternBuilder forDimension(DimensionEnum dimension) {
            this.dimension = dimension;
            return this;
        }

        public PatternBuilder atIndex(int index) {
            this.index = index;
            return this;
        }

        public Pattern build() {
            Pattern result = new Pattern();
            result.setPattern(this.pattern);
            result.setDimension(this.dimension);
            result.setIndex(this.index);
            return result;
        }
    }
}
