package wang.joye.blog.config;

import org.springframework.stereotype.Component;

/**
 * 封装一些方法在thymeleaf模板中使用
 *
 * @author 汪继友
 * @since 2020/7/3
 */
@Component
public class ThymeleafBean {

    /**
     * 向上取整
     */
    public int ceil(double v) {
        return (int) Math.ceil(v);
    }
}
