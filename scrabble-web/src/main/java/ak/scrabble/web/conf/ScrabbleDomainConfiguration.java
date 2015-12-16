package ak.scrabble.web.conf;

import ak.scrabble.web.security.ScrabbleSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by akopylov on 15.09.2015.
 */
@Configuration
@ComponentScan(basePackages = "ak.scrabble.web")
@Import(value = {ScrabbleSecurityConfiguration.class})
public class ScrabbleDomainConfiguration {
    @Bean(name = "applicationName")
    public String applicationName() {
        return "Web Scrabble";
    }
}
