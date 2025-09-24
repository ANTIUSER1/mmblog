package pn.back.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {

        "pn.back.controllers",
        "pn.back.repo",
        "pn.back.services"
}
)
@PropertySource("classpath:application.yml")
public class WebConfig {


}
