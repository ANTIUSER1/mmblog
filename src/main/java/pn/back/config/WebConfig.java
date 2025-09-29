package pn.back.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Import({pn.config.DatabaseConfig.class, pn.back.config.FileFlowConfig.class, pn.back.mappers.MessageMapper.class})
@EnableWebMvc
@ComponentScan(basePackages = {
        "pn.back.controllers",
        "pn.back.repo",
        "pn.back.services"
}
)
@PropertySource("classpath:application.properties")
public class WebConfig {


}
