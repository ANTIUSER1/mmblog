package pn.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pn.back.mappers.CommentMapper;
import pn.back.mappers.MessageMapper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"pn.back.controllers", "pn.back.services", "pn.back.repo"})
@Import({MessageMapper.class, CommentMapper.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class WebConfigTest {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    String applicationName() {
        return appName;
    }
}