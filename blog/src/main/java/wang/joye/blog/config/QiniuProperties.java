package wang.joye.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 汪继友
 * @since 2020/6/26
 */
@ConfigurationProperties("qiniu")
@Component
@Data
public class QiniuProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
    private Long tokenValidSecond;
}
