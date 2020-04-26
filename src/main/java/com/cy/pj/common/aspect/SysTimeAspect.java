package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

//@Order(3)
@Service
@Aspect
public class SysTimeAspect {

	@Pointcut("bean(sysRoleServiceImpl)")
	public void doTime() {}
	
	@Before("doTime()")
	public void doBefore(JoinPoint jp) {
		System.out.println("time before()");
	}
	@After("doTime()")//finally{}
	public void doAfter() {
		System.out.println("time after()");
	}
	@AfterReturning("doTime()")
	public void  doAfterReturning() {
		System.out.println("time doAfterReturning()");
	}
	@AfterThrowing("doTime()")
	public void  doAfterThrowing() {
		System.out.println("time doAfterthrowing()");
	}
	
	@Around("doTime()")
	public Object doAround(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("doAround.before");
		try {
        Object result = jp.proceed();
        return result;
  		
		}catch(Throwable e) {
			System.out.println("doAround -->"+e.getMessage());
			throw e;
		}finally {
			System.out.println("doAround.after");
		}
	}
	
}
