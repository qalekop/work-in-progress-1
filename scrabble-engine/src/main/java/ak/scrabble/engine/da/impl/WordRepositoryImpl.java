package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.DictFlavor;
import ak.scrabble.engine.model.ImmutableWord;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.model.Word;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by akopylov on 07/07/16.
 */
@Repository
public class WordRepositoryImpl extends BaseDAO implements WordRepository {

    private static final String P_WORD = "word";

    private static final String S_EQUALS = "select word from dict where word = :" + P_WORD;
    // + " and flavor in (:" + P_FLAVOR + ")";
    private static final String S_LIKE = "select word from dict where LOWER(word) like :" + P_WORD;
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
    public List<Word> find(SearchSpec specification) {
        // todo implement me
        if (specification.dictionaries().contains(DictFlavor.BLACK)) return Collections.emptyList();

        String searchPattern = specification.pattern();
        return jdbc.query(searchPattern.matches(".*[_%]+.*") ? S_LIKE : S_EQUALS,
                new MapSqlParameterSource(P_WORD, searchPattern),
                (resultSet, i) -> {
                    return ImmutableWord.builder().word(resultSet.getString("word")).build();
                });
    }
}
