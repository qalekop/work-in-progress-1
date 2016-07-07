package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by akopylov on 05/07/16.
 */
@Repository
public class GameDAOImpl extends BaseDAO implements GameDAO  {

    private static final String P_USER = "user_name";
    private static final String P_FIELD = "field";
    private static final String P_HUMAN = "score_human";
    private static final String P_MACHINE = "score_machine";

    private static final String S_EXISTS = "select 1 from sc_game where user_name = :" + P_USER;
    private static final String S_CREATE = "insert into sc_game (user_name, field, score_human, score_machine)"
            + " values (:" + P_USER
            + ", XMLPARSE(CONTENT :" + P_FIELD + ")"
            + ", :" + P_HUMAN
            + ", :" + P_MACHINE
            + ")";
    private static final String S_READ = "select field, score_human, score_machine from sc_game where user_name = :" + P_USER;
    private static final String S_UPDATE = "update sc_game set field = XMLPARSE(CONTENT :" + P_FIELD + ")"
            + ", score_human = :" + P_HUMAN
            + ", score_machine = :" + P_MACHINE
            + " where user_name = :" + P_USER;
    private static final String S_DELETE = "delete from sc_game where user_name = :" + P_USER;

    @Override
    public boolean savedStateExists(String user) {
        return jdbc.queryForRowSet(S_EXISTS, new MapSqlParameterSource(P_USER, user)).next();
    }

    @Override
    public Game getGame(String user) {
        return jdbc.queryForObject(S_READ,
                new MapSqlParameterSource(P_USER, user),
                (RowMapper<Game>) (resultSet, i) -> ImmutableGame.builder()
                        .cells((Iterable<? extends Cell>) new XStream().fromXML(resultSet.getString(P_FIELD)))
                        .score(new ImmutablePair<>(resultSet.getInt(P_HUMAN), resultSet.getInt(P_MACHINE)))
                        .build());
    }

    @Override
    public void persistGame(String user, Game game, boolean create) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(P_USER, user);
        params.addValue(P_FIELD, new XStream().toXML(game.cells()));
        params.addValue(P_HUMAN, game.score().getLeft());
        params.addValue(P_MACHINE, game.score().getRight());
        int rowCount = jdbc.update(create ? S_CREATE : S_UPDATE, params);
        LOG.trace("game state persisted for user={}, rowCount={}", user, rowCount);
    }

    @Override
    public void removeGame(String user) {
        jdbc.update(S_DELETE, new MapSqlParameterSource(P_USER, user));
    }
}
