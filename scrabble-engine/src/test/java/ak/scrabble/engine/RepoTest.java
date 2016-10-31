package ak.scrabble.engine;

import ak.scrabble.engine.da.GameDAO;
import ak.scrabble.engine.da.WordRepository;
import ak.scrabble.engine.model.Bonus;
import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.DictFlavor;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.ImmutableGame;
import ak.scrabble.engine.model.ImmutableSearchSpec;
import ak.scrabble.engine.model.Pattern;
import ak.scrabble.engine.model.SearchSpec;
import ak.scrabble.engine.rules.RulesService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ak.scrabble.engine.da.GameDAO.Mode.*;

/**
 * Created by akopylov on 06.11.2015.
 */
public class RepoTest extends AbstractDBTest {

    private static final String USER_FAILURE = "scrabble";
    private static final String USER_SCARBBLE = "scrabble";
    private static final String USER_SUCCESS = "mbricker";

    @Autowired
    private GameDAO gameDAO;
    @Autowired
    private RulesService rulesService;
    @Autowired
    private WordRepository wordRepo;

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
                assertEquals(count, expectedCount);
            }
        }
    }

    @Test
    public void testGame() {
        assertFalse("Oops!", gameDAO.savedStateExists(USER_FAILURE));
    }

    @Test
    public void testGameDAO() throws SQLException {
        final int two = 2;
        List<Cell> result = new ArrayList<>(2);
        for (int i=0; i<two; i++) {
            Cell cell = new Cell(i, i);
            cell.setBonus(Bonus.NONE);
            cell.setLetter(i == 0 ? 'А' : 'Я');
            result.add(cell);
        }
        Game game = ImmutableGame.builder()
                .cells(result)
                .scoreHuman(two).scoreMachine(two)
                .build();

        gameDAO.persistGame(USER_SUCCESS, game, CREATE);
        assertTrue(gameDAO.savedStateExists(USER_SUCCESS));

        assertTrue(gameDAO.getGame(USER_SUCCESS).cells().size() == two);

        gameDAO.removeGame(USER_SUCCESS);
        assertFalse(gameDAO.savedStateExists(USER_SUCCESS));
    }

    @Test
    public void testGameForScrabble() {
        Game game = gameDAO.getGame(USER_SCARBBLE);
        assertTrue(game.bag().size() == 3);
    }

    @Test
    public void testRulesService() {
        final String P_UNIQUE = "абака";
        final String P_MULTIPLE = "^абр.+$";
        final String P_WRONG = "cабака";

        assertTrue(rulesService.valid(P_UNIQUE));
        assertFalse(rulesService.valid(P_WRONG));

        SearchSpec spec = ImmutableSearchSpec.builder()
                .pattern(new Pattern.PatternBuilder().withPattern(P_MULTIPLE).build())
                .regexp(true)
                .dictionaries(CollectionUtils.arrayToList(new DictFlavor[]{DictFlavor.USHAKOV, DictFlavor.WHITE}))
                .build();
        assertTrue(wordRepo.find(spec).size() == 7);
    }
}
