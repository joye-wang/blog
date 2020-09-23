package wang.joye.blog.global;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求参数、响应体 统一日志打印
 */
@Aspect
@Component
@Slf4j
public class RestControllerAspect {

    /**
     * 环绕通知
     *
     * @param joinPoint 连接点
     * @return 切入点返回值
     * @throws Throwable 异常信息
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController) ")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String classMethod = joinPoint.getSignature().getName();
        String ip = ServletUtil.getClientIP(request);
        UserAgent agent = UserAgentParser.parse(request.getHeader("user-agent"));

        // 过滤掉HttpServletRequest相关参数，因为序列化时，json会尝试获取request上下文，造成异常
        List<Object> args = Arrays.stream(joinPoint.getArgs()).filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());
        String jsonArgs = JSON.toJSONString(args);

        String buffer = "********* request log *********\n" +
                String.format("http method: %s %s\n", request.getMethod(), request.getRequestURI()) +
                String.format("class method: %s %s\n", className, classMethod) +
                String.format("params: %s\n", JSON.toJSONString(request.getParameterMap())) +
                        String.format("args: %s\n", jsonArgs) +
                        String.format("remote ip: %s", ip);
        log.info(buffer);

        return joinPoint.proceed();
    }
}
