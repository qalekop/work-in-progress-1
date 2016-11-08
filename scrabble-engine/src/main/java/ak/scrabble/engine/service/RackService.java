package ak.scrabble.engine.service;

import ak.scrabble.engine.model.Tile;

import java.util.List;

/**
 * Created by akopylov on 08/11/2016.
 */
public interface RackService {

    /**
     * Return new rack based upon the current bag state, the list of letters to keep and the list of letters to shuffle
     * @param bag Bag with letters
     * @param existingLetters Letters to keep when shuffling
     * @return rack
     */
    List<Tile> getRack(List<Character> bag, final String existingLetters, final String shuffleLetters);

    /**
     *
     * @param bag Bag with letters
     * @param usedLetters Removed letters
     * @param rack inital rack
     * @return rack
     */
    List<Tile> getRack(List<Character> bag, List<Tile> rack, List<String> usedLetters);
}
