package wang.joye.blog.controller.admin;

import wang.joye.blog.config.QiniuProperties;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 七牛云相关接口
 *
 * @author 汪继友
 */
@RestController
@RequestMapping("qiniu")
public class QiniuController {

    private final QiniuProperties qiniuProperties;
    private final Auth auth;
    private String uploadToken;
    private LocalDateTime lastTime;

    public QiniuController(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
        auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
    }

    /**
     * 得到七牛上传文件的token
     */
    @GetMapping("token")
    public String getUploadToken() {
        // 系统内的token提前5分钟获取
        long tempValid = qiniuProperties.getTokenValidSecond() - 5 * 60;
        // 如果uploadToken为null，或者token已经过期，则重新请求token
        if (uploadToken == null || lastTime.plusSeconds(tempValid).isBefore(LocalDateTime.now())) {
            uploadToken = auth.uploadToken(qiniuProperties.getBucket(), null, qiniuProperties.getTokenValidSecond(), null);
            lastTime = LocalDateTime.now();
        }
        return uploadToken;
    }
}
