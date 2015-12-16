package ak.scrabble.engine;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by akopylov on 06.11.2015.
 */
public class RepoTest extends AbstractDBTest {

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
}
