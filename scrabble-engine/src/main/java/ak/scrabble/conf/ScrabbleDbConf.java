package ak.scrabble.conf;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;

/**
 * Created by akopylov on 06.11.2015.
 */
@Configuration
@ComponentScan(basePackages = {"ak.scrabble"})
@PropertySource("classpath:db.properties")
public class ScrabbleDbConf {


    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.username}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPwd;

    @Value("${foo}")
    private String bar;


    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPwd);

        return dataSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
