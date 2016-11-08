package ak.scrabble.engine.service;

import java.util.List;

/**
 * Created by akopylov on 08/11/2016.
 */
public interface BagService {

    /**
     * Returns initial bag content.
     * @return
     */
    List<Character> initBag();
}
