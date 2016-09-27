package ak.scrabble.engine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.Value;

import java.util.List;

/** Value object that represent the current game state (list of cells, score - to be continues).
 * To be used when persisting/retrieving moves.
 * Created by akopylov on 05/07/16.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableGame.class)
public interface Game {
    List<Cell> cells();
    Pair<Integer, Integer> score();
    Pair<Rack, Rack> rack();
    List<Character> bag();
}
