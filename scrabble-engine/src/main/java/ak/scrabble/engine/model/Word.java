package ak.scrabble.engine.model;

import org.immutables.value.Value;

/**
 * Created by akopylov on 01/02/16.
 */
@Value.Immutable
public interface Word {
    //todo enhance later with other properties

    String word();

    @Value.Default
    default int score() {
        return 0;
    }
}
