package ak.scrabble.web.endpoint;

import ak.scrabble.engine.model.Game;
import ak.scrabble.engine.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.EncodeException;
import java.security.Principal;

/**
 * Created by akopylov on 27/09/2016.
 */
@Component
public class ScrabbleWSHandler extends TextWebSocketHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    GameService gameService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Principal principal = session.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("not authenticated");
        }
        Game game = gameService.getGame(principal.getName());
        String response;
        try {
            response = mapper.writer().writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new EncodeException(game, "Oops!", e);
        }
        session.sendMessage(new TextMessage(response));
    }
}
