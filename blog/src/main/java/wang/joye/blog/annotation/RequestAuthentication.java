package wang.joye.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解可用在controller或method上，代表该方法需要进行身份验证（登录）
 *
 * @author 汪继友
 * @since 2020/6/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestAuthentication {
}
