package com.hirix.aspect;

@org.springframework.stereotype.Component
@org.aspectj.lang.annotation.Aspect
public class LoggingAspect {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoggingAspect.class);

    @org.aspectj.lang.annotation.Before("aroundRepositoryPointcut()")
    public void logBefore(org.aspectj.lang.JoinPoint joinPoint) {
        log.info("Method " + joinPoint.getSignature().getDeclaringTypeName() + " " + joinPoint.getSignature().getName() + " start");
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "aroundRepositoryPointcut()")
    public void doAccessCheck(org.aspectj.lang.JoinPoint joinPoint) {
        log.info("Method " + joinPoint.getSignature().getDeclaringTypeName() + " " + joinPoint.getSignature().getName() + " finished");
    }

    @org.aspectj.lang.annotation.Pointcut("execution(* com.hirix.repository..*(..))")
    public void aroundRepositoryPointcut() {
    }
}
