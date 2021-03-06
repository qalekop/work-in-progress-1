package ak.scrabble.engine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

/**
 * Server response in case of erroneous human move.
 * Created by akopylov on 29/06/16.
 */
@Value.Immutable
@JsonSerialize(as = ImmutableResponseError.class)
public interface ResponseError extends MoveResponse {
    String message();
    List<Cell> cells();

    @Value.Derived
    default boolean success() {
        return false;
    }
}
