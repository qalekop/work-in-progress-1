package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.DictFlavor;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.Word;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by akopylov on 07/07/16.
 */
@Repository
public class WordRepositoryImpl extends BaseDAO implements WordRepository {

    private static final String P_WORD = "word";

    private static final Locale LOCALE_RU_RU = new Locale("ru", "RU");

    private static final String S_EQUALS = "select word from dict where word = :" + P_WORD;
    // + " and flavor in (:" + P_FLAVOR + ")";
    private static final String S_LIKE = "select word from dict where word ~ :" + P_WORD;
    // + " and flavor in (:" + P_FLAVOR + ")";

    private static final String LOOKUP = "select description from dict where word = :" + P_WORD;
    // + " and flavor in (:" + P_FLAVOR + ")";

    @Override
    public void addToBlackList(Word word) {
        // todo implement me
    }

    @Override
    public void addToWhiteList(Word word) {
        // todo implement me
    }

    @Override
    public List<String> find(SearchSpec specification) {
        // todo implement me
        if (specification.dictionaries().contains(DictFlavor.BLACK)) return Collections.emptyList();

        String searchPattern = specification.pattern().getPattern();
        return jdbc.query(specification.regexp() ? S_LIKE : S_EQUALS,
                new MapSqlParameterSource(P_WORD, searchPattern.toLowerCase(LOCALE_RU_RU)),
                (resultSet, i) -> resultSet.getString("word"));
    }

    @Override
    public String lookup(String word) {
        return jdbc.queryForObject(LOOKUP, new MapSqlParameterSource(P_WORD, word), String.class);
    }
}
