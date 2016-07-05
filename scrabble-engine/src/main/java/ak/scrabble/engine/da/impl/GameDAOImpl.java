package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.da.exception.InternalDataAccessException;
import ak.scrabble.engine.model.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * Created by akopylov on 05/07/16.
 */
@Repository
public class GameDAOImpl extends BaseDAO implements GameDAO  {

    private static final String P_USER = "user";
    private static final String P_FIELD = "field";
    private static final String P_HUMAN = "score_human";
    private static final String P_MACHINE = "score_machine";

    private static final String S_EXISTS = "select 1 from sc_game where user_name = :" + P_USER;
    private static final String S_INSERT = "insert into sc_game (user_name, field, score_human, score_machine)"
            + " values (:" + P_USER
            + ", CAST (:" + P_FIELD + " as jsonb)"
            + ", :" + P_HUMAN
            + ", :" + P_MACHINE
            + ")";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean savedStateExists(String user) {
        return jdbc.queryForRowSet(S_EXISTS, new MapSqlParameterSource(P_USER, user)).next();
    }

    @Override
    public Game getGame(String user) {
        return null;
    }

    @Override
    public void persistGame(String user, Game game) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(P_USER, user);
            params.addValue(P_FIELD, mapper.writer().writeValueAsString(game.cells()));
            params.addValue(P_HUMAN, game.score().getLeft());
            params.addValue(P_MACHINE, game.score().getRight());
            int rowCount = jdbc.update(S_INSERT, params);
            LOG.trace("game state persisted for user={}, rowCount={}", user, rowCount);
        } catch (JsonProcessingException e) {
            throw new InternalDataAccessException("json exception", e);
        }
    }

    @Override
    public void removeGame(String user) {

    }
}
