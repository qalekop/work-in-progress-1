package ak.scrabble.web.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by akopylov on 02/09/16.
 */
@ServerEndpoint(value="/scrabble/game/feedback/{user}")
public class GameEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(GameEndpoint.class);
    private static final String USER = "user";

    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        LOG.info("websocket session opened for user=" + user);
        session.getUserProperties().put(USER, user);
    }

    @OnMessage
    public void onMessage(Session session, String message /* ? */) throws IOException {
        String user = (String) session.getUserProperties().get(USER);
        session.getBasicRemote().sendText("Hello, " + user + ": " + message);
    }
}
