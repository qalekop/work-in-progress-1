package ak.scrabble.engine.model;

import org.immutables.value.Value;

import java.util.List;

/**
 * Created by akopylov on 01/02/16.
 */
@Value.Immutable
public interface Word {
    //todo enhance later with other properties

    /** well, the word itself */
    String word();
    /** only newly added tiles, to enable the possible roll-back */
    List<Cell> cells();
    Player player();

    @Value.Default
    default int score() {
        return 0;
    }
}
