package ak.scrabble.engine.service.impl;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.service.BagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akopylov on 17/12/15.
 */
@Service
public class BagServiceImpl implements BagService {

    private static final Logger LOG = LoggerFactory.getLogger(BagServiceImpl.class);

    @Override
    public List<Character> initBag() {
        List<Character> bag = new ArrayList<>();
        Configuration.getAllLetters().forEach(letter -> {
            int quantity = Configuration.getQuantity(letter);
            for (int i = 0; i < quantity; i++) bag.add(letter);
        });
        return bag;
    }
}
