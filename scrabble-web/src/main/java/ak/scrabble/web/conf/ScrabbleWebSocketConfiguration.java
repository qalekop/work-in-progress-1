package ak.scrabble.web.conf;

import ak.scrabble.web.endpoint.ScrabbleWSHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Created by akopylov on 27/09/2016.
 */
@Configuration
@EnableWebSocket
public class ScrabbleWebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(scrabbleHandler(), "/scrabble/game/feedback/")
            .setAllowedOrigins("http://localhost");
    }

    @Bean
    public WebSocketHandler scrabbleHandler() {
        return new ScrabbleWSHandler();
    }
}
