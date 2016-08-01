package ak.scrabble.engine.model;

import org.apache.commons.lang3.StringUtils;
import org.immutables.value.Value;

import java.util.List;

/**
 * Created by akopylov on 07/07/16.
 */
@Value.Immutable
public interface SearchSpec {
    /**
     * List of dictionaries to look up
     */
    List<DictFlavor> dictionaries();

    /**
     * Search pattern, may be regexp
     */
    Pattern pattern();

    /**
     * <code>true</code> means regext pattern matching, <code>false</code> - 'equals' matching
     */
    boolean regexp();

    /**
     * available letters
     */
    default String rack() { return StringUtils.EMPTY; };
}
