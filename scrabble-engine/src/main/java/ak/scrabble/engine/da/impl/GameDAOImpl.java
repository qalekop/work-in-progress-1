package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import ak.scrabble.engine.model.Tile;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by akopylov on 05/07/16.
 */
@Repository
public class GameDAOImpl extends BaseDAO implements GameDAO  {

    private static final String P_USER = "user_name";
    private static final String P_FIELD = "field";
    private static final String SCORE_HUMAN = "score_human";
    private static final String SCORE_MACHINE = "score_machine";
    private static final String RACK_HUMAN = "rack_human";
    private static final String RACK_MACHINE = "rack_machine";
    private static final String P_BAG = "bag";

    private static final String S_EXISTS = "select 1 from sc_game where user_name = :" + P_USER;
    private static final String S_CREATE = "insert into sc_game (user_name, field, score_human, score_machine, rack_human, rack_machine, bag)"
            + " values (:" + P_USER
            + ", XMLPARSE(CONTENT :" + P_FIELD + ")"
            + ", :" + SCORE_HUMAN
            + ", :" + SCORE_MACHINE
            + ", XMLPARSE(CONTENT :" + RACK_HUMAN + ")"
            + ", XMLPARSE(CONTENT :" + RACK_MACHINE + ")"
            + ", :" + P_BAG
            + ")";
    private static final String S_READ = "select field, score_human, score_machine, rack_human, rack_machine, bag" +
            " from sc_game where user_name = :" + P_USER;
    private static final String S_UPDATE = "update sc_game set field = XMLPARSE(CONTENT :" + P_FIELD + ")"
            + ", score_human = :" + SCORE_HUMAN
            + ", score_machine = :" + SCORE_MACHINE
            + ", rack_human = XMLPARSE(CONTENT :" + RACK_HUMAN + ")"
            + ", rack_machine = XMLPARSE(CONTENT :" + RACK_MACHINE + ")"
            + ", bag = :" + P_BAG
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
                        .scoreHuman(resultSet.getInt(SCORE_HUMAN))
                        .scoreMachine(resultSet.getInt(SCORE_MACHINE))
                        .rackHuman((Iterable<? extends Tile>) new XStream().fromXML(resultSet.getString(RACK_HUMAN)))
                        .rackMachine((Iterable<? extends Tile>) new XStream().fromXML(resultSet.getString(RACK_MACHINE)))
                        .bag(toCharArray(resultSet.getString(P_BAG)))
                        .build());
    }

    @Override
    public void persistGame(String user, Game game, boolean create) throws SQLException {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(P_USER, user)
                .addValue(P_FIELD, new XStream().toXML(game.cells()))
                .addValue(SCORE_HUMAN, game.scoreHuman())
                .addValue(SCORE_MACHINE, game.scoreMachine())
                .addValue(RACK_HUMAN, new XStream().toXML(game.rackHuman()))
                .addValue(RACK_MACHINE, new XStream().toXML(game.rackMachine()))
                .addValue(P_BAG, game.bag().stream().map(String::valueOf).reduce("", (a, b) -> (a + b)))
                ;
        int rowCount = jdbc.update(create ? S_CREATE : S_UPDATE, params);
        LOG.trace("game state persisted for user={}, rowCount={}", user, rowCount);
    }

    @Override
    public void removeGame(String user) {
        jdbc.update(S_DELETE, new MapSqlParameterSource(P_USER, user));
    }

    //<editor-fold desc="private util methods>

    private List<Character> toCharArray(String string) {
        if (StringUtils.isBlank(string)) {
            return Collections.emptyList();
        }
        List<Character> result = new ArrayList<>(string.length());
        for (char c : string.toCharArray()) {
            result.add(c);
        }
        return result;
    }
    //</editor-fold>
}
