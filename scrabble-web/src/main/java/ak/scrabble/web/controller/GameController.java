package ak.scrabble.web.controller;

import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.Rack;
import ak.scrabble.engine.service.GameService;
import ak.scrabble.engine.service.RackService;
import ak.scrabble.web.security.SecurityModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.security.Principal;
import java.util.List;

/**
 * Created by akopylov on 09.10.2015.
 */
@Controller
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    public static final String GAME_URL = "/game";
    private static final String LETTERS_FIELD = "letters";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RackService rackService;
    @Autowired
    GameService gameService;

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL, method = RequestMethod.GET)
    public String scrabble(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("name", request.getRemoteUser());
        return SecurityModel.SECURE_URI + GAME_URL;
    }

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/rack", method = RequestMethod.POST)
    @ResponseBody
    /**
     * Updates human's rack (when initing game or after a successful move/shuffle and returns it to the client.
     */
    public String getRack(@RequestBody MultiValueMap<String, String> letters, Principal user) throws JsonProcessingException {
        // get list of letters and convert 'em to a JSON object

        final String name = user.getName();
        final String existingLetters = letters.getFirst(LETTERS_FIELD);
        Rack rack = rackService.getRack(name, StringUtils.isEmpty(existingLetters) ? "" : existingLetters);

        return mapper.writer().writeValueAsString(rack.getLetters());
    }

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/game", method = RequestMethod.GET)
    @ResponseBody
    /**
     * Returns JSON object describing game state (i.e., field cells).
     */
    public String getField(Principal user) throws JsonProcessingException {
        // todo implement me
        // 1. retrieve game state for this particular gamer from the db.
        LOG.info("geting game for " + user.getName());
        List<Cell> field = gameService.getGame(user.getName());
        ///
        String _s = mapper.writer().writeValueAsString(field);
        return _s;
        ///
//        return mapper.writer().writeValueAsString(field);
    }

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/move"
            , method = RequestMethod.POST
            , headers = {"Content-type=application/json"})
    @ResponseBody // todo response with 'success' to wait for the next PUSH or 'error' with a proper message
    /**
     * Accepts human's move, verifies it and, if OK, starts MachineMove process.
     */
    public void makeMove(@RequestBody List<Cell> cells, Principal user) {
        // todo implement me
        LOG.debug("*** " + (CollectionUtils.isEmpty(cells) ? "empty" : cells.size()));
        int  x = 0;
        // get json [{row: ..., col: ..., letter: ..., accepted: false}, ...] and do the job
    }
}
