package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Rack;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by akopylov on 14/12/15.
 */
@Service
public class RackService {

    /**
     *
     * @param bag Bag with letters
     * @param existingLetters Letters to keep when shuffling
     * @return rack
     */
    public Rack getRack(List<Character> bag, final String existingLetters) {
        Rack result = new Rack(Configuration.RACK_SIZE);
        List<Character> letters = refillRack(bag, (byte) existingLetters.length());
        for (int i=0; i< existingLetters.length(); i++) {
            letters.add(existingLetters.charAt(i));
        }
        letters.forEach(letter -> result.addPair(letter, Configuration.getScore(letter)));
        return result;
    }
    public List<Character> refillRack(List<Character> bag, byte count) {
        int size = Configuration.RACK_SIZE - count;
        List<Character> result = new ArrayList<>(size);
        Optional<Character> letter;
        for (int i=0; i<size; i++) {
            letter = popRandom(bag);
            if (letter.isPresent()) result.add(letter.get());
        }
        return result;
    }

    private Optional<Character> popRandom(List<Character> bag) {
        if (CollectionUtils.isEmpty(bag)) return Optional.empty();
        return Optional.of(bag.remove((int) (Math.random() * bag.size())));
    }

}
