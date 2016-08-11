package ak.scrabble.engine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

/**
 * Server response with machine move.
 * Created by akopylov on 29/06/16.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableResponseSuccess.class)
public interface ResponseSuccess extends MoveResponse {

    List<Cell> cells();
    int score();

    @Value.Derived
    default boolean success() {
        return true;
    }
}
