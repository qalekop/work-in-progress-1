package ak.scrabble.engine;

import ak.scrabble.engine.da.GameDAO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by akopylov on 06.11.2015.
 */
public class RepoTest extends AbstractDBTest {

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
        assertFalse("Oops!", gameDAO.savedStateExists("scrabble"));
    }
}
