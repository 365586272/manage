package com.cy.pj.common.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysCacheAspect {
	
	private  Map<String, Object> cache=new ConcurrentHashMap<String, Object>();
	 //Object result=null;
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	public void doCacheAspect() {}
	@Around("doCacheAspect()")
	public Object doCacheAround(ProceedingJoinPoint jp) throws Throwable  {
		System.out.println("get from cache");
		//cache.put("result",result);
		Object cacheTree=cache.get("CacheTree");
		if(cacheTree!=null) return cacheTree;
		Object result = jp.proceed();
		cache.put("CacheTree", result);
		System.out.println("put to cache");
		return cacheTree;
	}

}
