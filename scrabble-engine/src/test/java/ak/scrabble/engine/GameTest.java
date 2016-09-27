package ak.scrabble.engine;

import ak.scrabble.conf.Configuration;
import ak.scrabble.conf.ScrabbleDbConf;
import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by akopylov on 13/09/16.
 */
@ContextConfiguration(classes={ScrabbleDbConf.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GameTest {

    private static final String USER_SCARBBLE = "scrabble";

    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private GameService gameService;

    @Test
    public void testCreateGame() throws SQLException {
        assertFalse("Oops!", gameDAO.savedStateExists(USER_SCARBBLE));
        Game game = gameService.getGame(USER_SCARBBLE);
        assertTrue(game.rackHuman().size() == Configuration.RACK_SIZE);
        assertTrue(game.rackMachine().size() == Configuration.RACK_SIZE);
        assertTrue(game.bag().size() == 88);
    }
}
