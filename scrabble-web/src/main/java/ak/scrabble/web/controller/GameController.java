package ak.scrabble.web.controller;

import ak.scrabble.engine.Configuration;
import ak.scrabble.engine.model.Rack;
import ak.scrabble.web.security.SecurityModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by akopylov on 09.10.2015.
 */
@Controller
public class GameController {
    public static final String GAME_URL = "/game";

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL,
            method = RequestMethod.GET)
    public String scrabble(Model model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("name", request.getRemoteUser());
        return SecurityModel.SECURE_URI + GAME_URL;
    }

    @RequestMapping(value = SecurityModel.SECURE_URI + GAME_URL + "/rack",
            method = RequestMethod.GET)
    public String getRack(SecurityContextHolderAwareRequestWrapper request) throws JsonProcessingException {
        // get list of letters and convert 'em to a JSON object

//        Rack rack;
        final int size = Configuration.RACK_SIZE;
        final String name = request.getRemoteUser();
        final StringBuilder sb = new StringBuilder(StringUtils.repeat("Z", size)).replace(0, size, name);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(sb.toString());
    }
}
