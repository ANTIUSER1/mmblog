/**
 * конфиг для веб-приложения
 */
package pn.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Import({
        pn.config.DatabaseConfig.class,
        pn.back.config.FileFlowConfig.class,
        pn.back.mappers.MessageMapper.class,
        pn.back.mappers.CommentMapper.class
})
@EnableWebMvc
@ComponentScan(basePackages = {
        "pn.back.controllers",
        "pn.back.repo",
        "pn.back.services"
}
)
public class WebConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    String applicationName() {
        return appName;
    }
}
