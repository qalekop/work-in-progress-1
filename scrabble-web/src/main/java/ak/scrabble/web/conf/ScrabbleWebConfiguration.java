package ak.scrabble.web.conf;

import com.github.resource4j.thymeleaf.ThymeleafResourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;

/**
 * Created by akopylov on 15.09.2015.
 */
@Configuration
@ComponentScan(basePackages = "ak.scrabble.web")
@EnableWebMvc
@EnableMBeanExport(
        registration = RegistrationPolicy.IGNORE_EXISTING,
        defaultDomain = "ak.scrabble.web")
@Import({ThymeleafResourceConfiguration.class})
public class ScrabbleWebConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver viewResolver(final SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setOrder(1);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Override
    public final void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public final void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
