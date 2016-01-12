package ak.scrabble.web.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Created by alexeikopylov on 19.12.15.
 */
public class WebContextListener extends ContextLoaderListener {

    private static final Logger LOG = LoggerFactory.getLogger(WebContextListener.class);


    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        LOG.info("Scrabble stopped");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        event.getServletContext().setAttribute("timestamp", Long.toHexString(System.currentTimeMillis()));
        LOG.info("Scrabble started @");
    }
}
