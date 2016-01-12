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

    public List<String> getBag(final String user) {
        // todo Ask db
        List<String> bag = new ArrayList<>();
        Configuration.getAllLetters().stream().forEach(letter -> {
            int quantity = Configuration.getQuantity(letter);
            for (int i = 0; i < quantity; i++) bag.add(letter);
        });
        // todo persist bag
        return bag;
    }

    public List<String> refillRack(List<String> bag, byte count) {
        int size = Configuration.RACK_SIZE - count;
        List<String> result = new ArrayList<>(size);
        Optional<String> letter;
        for (int i=0; i<size; i++) {
            LOG.info("bag size before = " + bag.size());
            letter = popRandom(bag);
            LOG.info("bag size after = " + bag.size());
            if (letter.isPresent()) result.add(letter.get());
        }
        // todo persist bag
        return result;
    }

    private Optional<String> popRandom(List<String> bag) {
        if (CollectionUtils.isEmpty(bag)) return Optional.empty();
        return Optional.of(bag.remove((int) (Math.random() * bag.size())));
    }
}
