package ak.scrabble.engine.rules;

import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.DictFlavor;
import ak.scrabble.engine.model.ImmutableSearchSpec;
import ak.scrabble.engine.model.SearchSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by akopylov on 07/07/16.
 */
@Service
public class RulesService {

    @Autowired
    WordRepository repo;

    /**
     * Verifies if the provided word:
     * <ul>
     *     <li>Doesn't exist in the black list;</li>
     *     <li>Exists either in default or user dictionary;</li>
     *     <li>Wasn't used earlier (to be imlemented);</li>
     *     <li>Exceeds the minimum length (to be imlemented).</li>
     * </ul>
     * @param word word to look up
     * @return <code>true, if the above condition id met</code>, <code>false</code> otherwise.
     */
    public boolean valid(String word) {
        SearchSpec spec = ImmutableSearchSpec.builder()
                .pattern(word)
                .regexp(false)
                .dictionaries(Stream.of(DictFlavor.BLACK).collect(Collectors.toList()))
                .build();
        if (!CollectionUtils.isEmpty(repo.find(spec))) return false;

        spec = ImmutableSearchSpec.builder()
                .pattern(word)
                .regexp(false)
                .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                .build();
        return !CollectionUtils.isEmpty(repo.find(spec));
        // todo: implement the two last checks
    }
}
