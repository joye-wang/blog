package wang.joye.blog.global;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.util.TResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Date;

/**
 * @author 天宇
 * 全局controller advice，捕捉异常，并包装结果
 * 这里必须使用RestControllerAdvice，否则会跳转到template页面
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public TResult handleException(HttpServletRequest req, Exception e) {
        log.error("******** 系统发生异常********");
        log.error("request uri: " + req.getMethod() + " " + req.getRequestURI());
        log.error("parameter map: " + JSON.toJSONString(req.getParameterMap()));
        log.error("request time: " + new Date());
        log.error("堆栈信息", e);
        return TResult.failure(e.getMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public TResult handleBusinessException(HttpServletRequest req, BusinessException e) {
        log.warn(e.getMessage());
        return TResult.failure(e.getMessage());
    }

    /**
     * 处理数据绑定异常
     */
    @ExceptionHandler(BindException.class)
    public TResult handleBindException(HttpServletRequest req, BindException e) {
        StringBuilder error = new StringBuilder();
        e.getFieldErrors().forEach(i -> error.append(i.getField()).append(i.getDefaultMessage()).append("\n"));
        return TResult.failure(error.toString());
    }

    /**
     * 处理参数接收异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalStateException.class, MultipartException.class,
            HttpMessageNotReadableException.class, ValidationException.class, HttpRequestMethodNotSupportedException.class})
    public TResult handleParamException(HttpServletRequest req, Exception e) {
        return TResult.failure(e.getMessage());
    }
}
