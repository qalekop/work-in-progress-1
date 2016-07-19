package ak.scrabble.engine.model;

import org.immutables.value.Value;

import java.util.List;

/**
 * Created by akopylov on 07/07/16.
 */
@Value.Immutable
public interface SearchSpec {
    /**
     * List of dictionaries to look up
     * @return
     */
    List<DictFlavor> dictionaries();

    /**
     * Search pattern, may be regexp
     * @return
     */
    String pattern();

    /**
     * <code>true</code> means regext pattern matching, <code>false</code> - 'equals' matching
     * @return
     */
    boolean regexp();
}
