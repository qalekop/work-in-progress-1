package ak.scrabble.engine.da;

import ak.scrabble.engine.model.Game;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

/**
 * Created by akopylov on 05/07/16.
 */
public interface GameDAO {
    boolean savedStateExists(String user) throws DataAccessException;
    Game getGame(String user);
    void persistGame(String user, Game game, boolean create) throws SQLException;
    void removeGame(String user);
}
