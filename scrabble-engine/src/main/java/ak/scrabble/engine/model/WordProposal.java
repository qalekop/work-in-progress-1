package ak.scrabble.engine.model;

import org.immutables.value.Value;

/**
 * Created by akopylov on 01/08/16.
 */
@Value.Immutable
public interface WordProposal {
    String word();
    Pattern pattern();


}
