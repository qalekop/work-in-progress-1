package ak.scrabble.engine.service;

import ak.scrabble.engine.Configuration;
import ak.scrabble.engine.model.Rack;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by akopylov on 14/12/15.
 */
@Service
public class RackService {

    public Rack getRack(final String user) {
        // todo implement me
        Rack result = new Rack(Configuration.RACK_SIZE);
        result.setLetters(Arrays.asList("A", "B", "C", "D", "E", "F", "G"));
        return result;
    }
}
