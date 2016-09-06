package ak.scrabble.web.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by akopylov on 02/09/16.
 */
@ServerEndpoint(value="/scrabble/game/feedback/")
public class GameEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(GameEndpoint.class);
    private String user;

    @OnOpen
    public void onOpen(Session session) {
        Principal p = session.getUserPrincipal();
        if (p == null) {
            throw new IllegalStateException("No auth user");
        }
        user = p.getName();
        LOG.info("websocket session opened: " + user);
    }

    @OnMessage
    public String onMessage(String message /* ? */) throws IOException {
        // long operation
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // do nothing
        }
        return "Hello, " + user + ": " + message;
    }
}
