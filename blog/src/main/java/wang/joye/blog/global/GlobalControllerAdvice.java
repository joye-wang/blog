package wang.joye.blog.global;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import wang.joye.blog.util.TResult;

/**
 * @author 汪继友
 * 只对ResponseBody返回数据生效
 * 包装请求成功的返回结果
 */
@RestControllerAdvice
public class GlobalControllerAdvice implements ResponseBodyAdvice<Object> {

    /*
     * 此组件是否支持给定的控制器方法返回类型
     * 不拦截BasicErrorController
     * 不拦截TResult型的返回数据
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getDeclaringClass() != BasicErrorController.class
                && !methodParameter.getGenericParameterType().getTypeName().equals(TResult.class.getTypeName());
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return TResult.success(o);
    }
}
