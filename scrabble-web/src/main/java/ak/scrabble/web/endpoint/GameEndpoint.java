package ak.scrabble.web.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akopylov on 02/09/16.
 */
@ServerEndpoint(value="/scrabble/game/feedback/")
public class GameEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(GameEndpoint.class);
    private String user;

    private ObjectMapper mapper = new ObjectMapper();


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
        Response result = new Response();
        result.setName(user.toUpperCase());
        result.setValue(message.length());
        List<Character> l = new ArrayList<>(message.length());
        for (char c : message.toCharArray()) l.add(c);
        result.setLetters(l);
//        return "Hello, " + user + ": " + message;
        return mapper.writer().writeValueAsString(result);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        LOG.info("websocket session closed for " + user + "; " + reason);
    }

    @OnError
    public void onError(Throwable th) {
        LOG.info("websocket error for " + user + "; " + (th != null ? th.getMessage() : "null"));
    }

    // debug class
    private class Response {
        String name;
        int value;
        List<Character> letters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public List<Character> getLetters() {
            return letters;
        }

        public void setLetters(List<Character> letters) {
            this.letters = letters;
        }
    }
}
