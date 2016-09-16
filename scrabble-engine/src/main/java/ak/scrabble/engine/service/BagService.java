package ak.scrabble.engine.service;

import ak.scrabble.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by akopylov on 17/12/15.
 */
@Service
public class BagService {

    private static final Logger LOG = LoggerFactory.getLogger(BagService.class);

    public List<Character> initBag() {
        List<Character> bag = new ArrayList<>();
        Configuration.getAllLetters().stream().forEach(letter -> {
            int quantity = Configuration.getQuantity(letter);
            for (int i = 0; i < quantity; i++) bag.add(letter);
        });
        return bag;
    }
}
