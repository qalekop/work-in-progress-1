package ak.scrabble.engine.model;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * Created by akopylov on 01/08/16.
 */
public class Pattern {
    /** search pattern itself */
    private String pattern;
    /** pattern allocation - along ROW or COL */
    private DimensionEnum dimension;
    /** pattern's ROW or COL index */
    private int index;

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

    public static class PatternBuilder {
        private String pattern;
        private DimensionEnum dimension;
        private int index;

        public PatternBuilder withPattern(String pattern) {
            this.pattern = pattern;
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
