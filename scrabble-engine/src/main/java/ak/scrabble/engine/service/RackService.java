package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Tile;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
    public List<Tile> getRack(List<Character> bag, final String existingLetters) {
        List<Tile> result = new ArrayList<>(Configuration.RACK_SIZE);
        List<Character> letters = refillRack(bag, (byte) existingLetters.length());
        for (int i=0; i< existingLetters.length(); i++) {
            letters.add(existingLetters.charAt(i));
        }
        letters.forEach(letter -> result.add(new Tile(letter, Configuration.getScore(letter))));
        return result;
    }

    /**
     *
     * @param bag Bag with letters
     * @param usedLetters Removed letters
     * @param rack inital rack
     * @return rack
     */
    public List<Tile> getRack(List<Character> bag, List<Tile> rack, List<String> usedLetters) {
        List<Tile> result = new ArrayList<>(Configuration.RACK_SIZE);
        Collection<String> cRack = new ArrayList<>(Configuration.RACK_SIZE);
        for (Tile t : rack) {
            cRack.add(String.valueOf(t.getLetter()));
        }
        Collection<String> existingLetters = CollectionUtils.disjunction(cRack, usedLetters);
        List<Character> letters = refillRack(bag, (byte) existingLetters.size());
        for (String s : existingLetters) {
            letters.add(s.charAt(0));
        }
        letters.forEach(letter -> result.add(new Tile(letter, Configuration.getScore(letter))));
        return result;
    }

    private List<Character> refillRack(List<Character> bag, byte count) {
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
