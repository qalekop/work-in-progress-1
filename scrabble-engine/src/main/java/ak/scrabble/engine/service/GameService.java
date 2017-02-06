package ak.scrabble.engine.service;

import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.Tile;
import ak.scrabble.engine.model.WordProposal;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by akopylov on 08/11/2016.
 */
public interface GameService {

    /**
     * Returns game state for that particular user.
     * @param user
     * @return
     * @throws SQLException
     */
    Game getGame(final String user) throws SQLException;

    /**
     * Persist the new game state
     * @throws SQLException
     */
    void updateGame(final String user, final List<Character> bag, final List<Tile> humanRack) throws SQLException;

    /**
     * Processes the human move (verifies it and in case of success, saves the new game state.
     * @param user
     * @param newCells
     * @return
     * @throws SQLException
     */
    MoveResponse processHumanMove(final String user, final List<Cell> newCells) throws SQLException;

    /**
     * Shuffles human's rack, updates the game state and persists it.
     * @param user
     * @param rest Letters to be kept, combined into one string,
     * @param shuffle Letters to be shuffled, combined into one string.
     * @return
     */
    MoveResponse processShuffle(final String user, final String rest, final String shuffle) throws SQLException;

    /**
     * Verifies a move.
     * @param cells
     * @return
     */
    MoveResponse verifyMove(List<Cell> cells);

    List<WordProposal> findProposals(final List<Cell> field, String rack);

    /**
     * Handles machine move (in case of success sivet the new game state)
     * @param user
     * @throws SQLException
     */
    void processMachineMove(String user) throws SQLException;
}
