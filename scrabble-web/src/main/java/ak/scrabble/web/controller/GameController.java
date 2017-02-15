package ak.scrabble.web.controller;

import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.ImmutableResponseSuccess;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.ResponseSuccess;
import ak.scrabble.engine.service.DictService;
import ak.scrabble.engine.service.GameService;
import ak.scrabble.web.model.GameState;
import ak.scrabble.web.security.SecurityModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by akopylov on 09.10.2015.
 */
@Controller
public class GameController {

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    public static final String GAME_URL = "/game";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GameService gameService;

    @Autowired
    private DictService dictService;

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL, method = RequestMethod.GET)
    public String scrabble(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("name", request.getRemoteUser());
        return SecurityModel.SECURE_URI + GAME_URL;
    }

    /**
     * Returns JSON object describing game state.
     */
    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/game"
            , method = RequestMethod.GET)
    public ResponseEntity<String> getField(Principal user) throws JsonProcessingException, SQLException {
        LOG.info("getting game for " + user.getName());
        List<Cell> field = gameService.getGame(user.getName()).cells();
        ResponseSuccess response = ImmutableResponseSuccess.builder()
                .cells(field)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(mapper.writer().writeValueAsString(response), headers, HttpStatus.OK);
    }

    /**
     * Accepts human's move, verifies it and sends appropriate response to the client - or shuffles human's rack
     */
    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/move"
            , method = RequestMethod.POST
            , headers = {"Content-type=application/json; charset=utf-8"})
    @ResponseBody
    public ResponseEntity<String> makeMove(@RequestBody GameState gameState, Principal user)
            throws JsonProcessingException, SQLException {

        final List<Cell> cells = gameState.getCells();
        final String existingLetters = gameState.getRest();
        final String shuffle = gameState.getShuffle();
        MoveResponse moveResponse = StringUtils.isEmpty(shuffle)
                ? gameService.processHumanMove(user.getName(), cells)
                : gameService.processShuffle(user.getName(), existingLetters, shuffle);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(mapper.writer().writeValueAsString(moveResponse), headers, HttpStatus.OK);
    }

    @RequestMapping(value = SecurityModel.UNSECURE_URI
        , method = RequestMethod.GET)
    public ResponseEntity<String> lookup(@RequestParam("word") String word) {
        // todo check if empty
        Optional<String> description = dictService.lookup(word);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return description.isPresent()
                ? new ResponseEntity<>(description.get(), headers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
