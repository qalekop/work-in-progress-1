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
     * Search pattern, incl. '%' and '_'
     * @return
     */
    String pattern();
}
