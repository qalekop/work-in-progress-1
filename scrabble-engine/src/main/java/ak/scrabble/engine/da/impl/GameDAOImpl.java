package ak.scrabble.engine.da.impl;

import ak.scrabble.engine.da.BaseDAO;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Game;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by akopylov on 05/07/16.
 */
@Repository
public class GameDAOImpl extends BaseDAO implements GameDAO  {

    private static final String P_USER = "user";
    private static final String S_EXISTS = "select 1 from sc_game where user = :" + P_USER;

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

    }

    @Override
    public void removeGame(String user) {

    }
}
