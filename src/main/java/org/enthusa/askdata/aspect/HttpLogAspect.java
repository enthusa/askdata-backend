package org.enthusa.askdata.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author henry
 * @date 2023/8/28
 */
@Aspect
@Component
@Slf4j
public class HttpLogAspect {

    @Pointcut("execution(public * org.enthusa.askdata.controller..*.*(..))")
    public void httpLog() {
    }

    @Before("httpLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("{} {}, Args: {}", request.getMethod(), request.getRequestURL(), joinPoint.getArgs());
    }
}
