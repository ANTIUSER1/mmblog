package pn.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${spring.web.resources.static-locations}")
//    @Value("${app.static-path}")
    private String staticPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        System.out.println("\n\n  START :::: " + staticPath);
        //  registry.addRedirectViewController("/", "/");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations(
//                        "classpath:/static/",
//                        "classpath:/public/",
                        "file:" + staticPath + "/"
                );

        registry.addResourceHandler("/webapps/**")
                .addResourceLocations("file:./webapps/");
    }
}

