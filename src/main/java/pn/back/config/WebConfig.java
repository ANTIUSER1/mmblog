package pn.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
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
@PropertySource("classpath:application.properties")
public class WebConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    String applicationName() {
        return appName;
    }
}
