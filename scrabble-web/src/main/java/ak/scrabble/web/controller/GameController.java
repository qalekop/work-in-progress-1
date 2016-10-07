package ak.scrabble.web.controller;

import ak.scrabble.engine.model.Cell;
import ak.scrabble.engine.model.ImmutableResponseSuccess;
import ak.scrabble.engine.model.MoveResponse;
import ak.scrabble.engine.model.ResponseError;
import ak.scrabble.engine.model.ResponseSuccess;
import ak.scrabble.engine.service.GameService;
import ak.scrabble.web.security.SecurityModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Collections;
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
    GameService gameService;

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL, method = RequestMethod.GET)
    public String scrabble(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("name", request.getRemoteUser());
        return SecurityModel.SECURE_URI + GAME_URL;
    }

    /**
     * Returns JSON object describing game state (i.e., field cells).
     */
    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/game"
            , method = RequestMethod.GET)
    public ResponseEntity<String> getField(Principal user) throws JsonProcessingException, SQLException {
        LOG.info("getting game for " + user.getName());
        List<Cell> field = gameService.getGame(user.getName()).cells();
        ResponseSuccess response = ImmutableResponseSuccess.builder()
                .cells(field)
                .score(0) // todo return saved score if any
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(mapper.writer().writeValueAsString(response), headers, HttpStatus.OK);
    }

    /**
     * Accepts human's move, verifies it and sends appropriate response to the client.
     */
    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/move"
            , method = RequestMethod.POST
            , headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity<String> makeMove(@RequestBody List<Cell> cells, Principal user) throws JsonProcessingException {
        MoveResponse moveResponse = gameService.processHumanMove(user.getName(), cells);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(mapper.writer().writeValueAsString(moveResponse), headers, HttpStatus.OK);
    }
}
