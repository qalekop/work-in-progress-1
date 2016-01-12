package ak.scrabble.web.controller;

import ak.scrabble.conf.Configuration;
import ak.scrabble.engine.model.Player;
import ak.scrabble.engine.model.Rack;
import ak.scrabble.engine.service.RackService;
import ak.scrabble.web.security.SecurityModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.security.Principal;

/**
 * Created by akopylov on 09.10.2015.
 */
@Controller
public class GameController {
    public static final String GAME_URL = "/game";
    private static final String LETTERS_FIELD = "letters";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RackService rackService;

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL, method = RequestMethod.GET)
    public String scrabble(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("name", request.getRemoteUser());
        return SecurityModel.SECURE_URI + GAME_URL;
    }

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/rack", method = RequestMethod.POST)
    @ResponseBody
    public String getRack(@RequestBody MultiValueMap<String, String> letters, Principal user) throws JsonProcessingException {
        // get list of letters and convert 'em to a JSON object

        final String name = user.getName();
        final String existingLetters = letters.getFirst(LETTERS_FIELD);
        Rack rack = rackService.getRack(name, StringUtils.isEmpty(existingLetters) ? "" : existingLetters);

        return mapper.writer().writeValueAsString(rack.getLetters());
    }
}
