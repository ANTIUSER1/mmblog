/**
 * конфиг работы с файлами
 */
package pn.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class FileFlowConfig {

    @Value("${upload.dir}")
    String dir;

    @Bean
    String uploadDir() {
        return dir;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
