package wang.joye.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 调试时生效的bean
 *
 * @author 汪继友
 * @since 2020/7/15
 */
@Profile("dev")
@Component
public class DebugComponent {

    @Bean
    public WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:9001")
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }
}
