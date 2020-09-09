package wang.joye.blog;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wang.joye.blog.util.IpUtil;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("wang.joye.blog.mapper")
public class BlogApiApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BlogApiApplication.class, args);

        String serverPort = context.getEnvironment().getProperty("server.port", "8080");
        String serverPath = context.getEnvironment().getProperty("server.servlet.context-path", "/");

        String apiUrl = String.format("http://%s:%s%s", IpUtil.getMachineIP(), serverPort, serverPath);

        log.info("application started at            {}", apiUrl);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BlogApiApplication.class);
    }

    /**
     * 为所有的RestController添加前缀api
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("api", c -> c.isAnnotationPresent(RestController.class));
    }

    /**
     * 添加fastjson转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConvert = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        // 日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        SerializeConfig serializeConfig = new SerializeConfig();
        config.setSerializeConfig(serializeConfig);
        // 输出null值
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        fastJsonConvert.setFastJsonConfig(config);
        converters.add(0, fastJsonConvert);
    }

    @PostConstruct
    public void init() {
        /*
         * 注册shutdown钩子，当应用程序正常关闭时，会调用这里的方法
         * kill -9时，不会触发这段代码
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Blog Application is stopped");
        }));
    }

    /**
     * 本地允许跨域
     */
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }*/
}
