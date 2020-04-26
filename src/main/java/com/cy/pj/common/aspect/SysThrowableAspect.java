package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
/**
 * 异常监控切面对象
 * */
@Slf4j
@Component
@Aspect
public class SysThrowableAspect {
	/**异常通知*/
	@AfterThrowing(pointcut="bean(*ServiceImpl)",throwing="e")
	public void doHandleException(JoinPoint jp,Throwable e) {
		//此对象中封装了要调用的目标方法信息:例如方法名,参数类型,返回值
		String className=jp.getTarget().getClass().getName();
		MethodSignature ms=(MethodSignature)jp.getSignature();
		log.error("{} error message is {}",className+"."+ms.getName(),e.getMessage());
		log.error("method error {}",System.currentTimeMillis());

	}
}
