package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Player;
import ak.scrabble.engine.model.Rack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akopylov on 14/12/15.
 */
@Service
public class RackService {

    @Autowired
    private BagService bagService;

    /**
     *
     * @param user User name
     * @param existingLetters Letters to keep when shuffling
     * @return rack
     */
    public Rack getRack(final String user, final String existingLetters) {
        // todo implement me
        List<String> bag = bagService.getBag(user); // returns new bag (new game) or existing one (game in progress)
        Rack result = new Rack(Configuration.RACK_SIZE);
        List<String> letters = bagService.refillRack(bag, (byte) existingLetters.length());
        for (int i=0; i< existingLetters.length(); i++) {
            letters.add(existingLetters.substring(i, i+1));
        }
        for (String letter : letters) {
            result.addPair(letter, Configuration.getScore(letter));
        }
        return result;
    }

}
