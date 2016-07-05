package ak.scrabble.engine;

import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.model.Bonus;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by akopylov on 06.11.2015.
 */
public class RepoTest extends AbstractDBTest {

    private static final String USER_FAILURE = "scrabble";
    private static final String USER_SUCCESS = "mbricker";
    @Autowired
    private GameDAO gameDAO;

    @Test
    public void testDictionary() throws SQLException {
        final String query = "select count(*) from dict";
        final int expectedCount = 30405;

        try (
                Connection c = getDataSource().getConnection();
                PreparedStatement ps = c.prepareStatement(query);
        ) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals("Oops!", count, expectedCount);
            }
        }
    }

    @Test
    public void testGame() {
        assertFalse("Oops!", gameDAO.savedStateExists(USER_FAILURE));
    }

    @Test
    public void testSaveGame() {
        final int two = 2;
        List<Cell> result = new ArrayList<>(2);
        for (int i=0; i<two; i++) {
            Cell cell = new Cell(i, i);
            cell.setBonus(Bonus.NONE);
            cell.setLetter('Z');
            result.add(cell);
        }
        Game game = ImmutableGame.builder()
                .cells(result).score(new ImmutablePair<>(two, two))
                .build();

        gameDAO.persistGame(USER_SUCCESS, game);
        assertTrue("Oops!", gameDAO.savedStateExists(USER_SUCCESS));
    }
}
