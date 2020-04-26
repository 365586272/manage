package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;
/**
 * spring mvc
 * 拦截器
 * @author DELL、
 *
 */
public class TimeAccessInterceptor implements HandlerInterceptor{
	/**
	 * 拦截器的prehandle方法会在目标控制层方法执行之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获得日历对象
		Calendar c = Calendar.getInstance();
		//2.设置开始访问时间
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long start=c.getTimeInMillis();
		//3.定义终止访问时间
		c.set(Calendar.HOUR_OF_DAY, 17);
		long end=c.getTimeInMillis();
		long cTime=System.currentTimeMillis();
		//4.进行时间判定
		if(cTime>start||cTime<end)
			throw new ServiceException("请在9-17点之间访问");
		return true;
	}
}
