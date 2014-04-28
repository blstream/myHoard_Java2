package com.blstream.myhoard.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author gohilukk
 */
@Aspect
public class BusinessProfiler {
    
        @Pointcut("execution(* com.blstream.myhoard.*.*(..))")
        public void businessMethods() { }

        @Around("businessMethods()")
        public Object profile(ProceedingJoinPoint pjp) throws Throwable {
                long start = System.currentTimeMillis();
                Object output = pjp.proceed();
                long elapsedTime = System.currentTimeMillis() - start;
                return output;
        }
}
