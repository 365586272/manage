package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * 通过@Aspect注解描述的对象,为AOP一种切面类型在这种切面类型通常要定义两部分内容
 * 1)切入点(pointcut):在哪进行功能增强
 * 2)(Advice)通知,扩展功能
 * @author DELL、
 */
//@Order(1)
//@Transactional(propagation = Propagation.REQUIRED)
@Aspect
@Component
@Slf4j
public class SysLogAspect {
	/**
	 * @Pointcut定义切入点
	 * bean(sysUserServiceImpl)切入点表达式,sysUserServiceImpl为spring容器中bean的名字
	 * 在当前应用中,这个类中所有方法的集合为切入点(这个切入点中任意一个方法执行时,都要进行功能扩展);
	 */
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	public void doPointCut() {}//方法实现不要写内容

	//@Around描述通知advice,切面中的方法,除了切入点都是通知,要实现扩展的功能
	//一种环绕通知,环绕通知内部可以手动调用目标方法,可以在目标方法之前或之后进行额外功能
	@Around("doPointCut()")
	public Object logAround(ProceedingJoinPoint jp) throws Throwable{
		long start=System.currentTimeMillis();
		log.info("method start {}",start);
		try {
			Object result = jp.proceed();//调用本类中其他通知,下一个切面,或目标方法
			long end=System.currentTimeMillis();
			log.info("method end {}",end);
			saveLog(jp,(end-start));
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("method error {}",System.currentTimeMillis());
			throw e;
		}

	}
	@Autowired
	private SysLogService sysLogService;

	private void saveLog(ProceedingJoinPoint jp, long time) throws Throwable {

		SysLog log=new SysLog();
		MethodSignature ms=(MethodSignature)jp.getSignature();
		Class<?> c = jp.getTarget().getClass();
		//反射获取方法对象
		Method targetMethod = c.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
		RequiredLog anno = targetMethod.getAnnotation(RequiredLog.class);
		String operation="";
		if(operation!=null) {
			operation = anno.value();
			}
		String className=c.getName();
		//SysUser user =(SysUser) SecurityUtils.getSubject().getPrincipal();
		String username=ShiroUtils.getUserName();
		String method=className+"."+ms.getName();
		String params =new ObjectMapper().writeValueAsString(jp.getArgs());//Arrays.toString( jp.getArgs());
		log.setTime(time);
		log.setUsername(username);
		log.setParams(params);
		log.setIp(IPUtils.getIpAddr());
		log.setMethod(method);
		log.setCreatedTime(new Date());
		log.setOperation(operation);
		sysLogService.saveObject(log);
	}
}
