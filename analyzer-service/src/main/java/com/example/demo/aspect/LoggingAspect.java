package com.example.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.demo.service..*.*(..))")
    public Object logWaktu(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();
        log.info("Masuk {} args {}", methodName, Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        log.info("Selesai {} dalam {} ms", methodName, System.currentTimeMillis() - startTime);

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.example.demo.service..*.*(..))", throwing = "exception")
    public void logError(JoinPoint joinPoint, Exception exception) {
        log.error("Error di {}: {}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }
}
