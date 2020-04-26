package com.cy.pj.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration这是描述的类由spring容器管理的一个配置类
 * @author DELL、
 *
 */
@Configuration
public class SpringShiroConfig {
	/**
	 * DefaultWebSecurityManager此对象是shiro框架的核心
	 * @Bean注解描述的方法返回值交给spring管理
	 * SecurityManager 导包是shiro
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager sManager = 
				new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(rememberMeManager);
		sManager.setSessionManager(sessionManager);
		return sManager;
	}
	/**
	 * ShiroFilterFactoryBean基于此对象创建过滤器工厂
	 * 通过过滤器工厂创建过滤器,
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactory (
			SecurityManager securityManager) {
		ShiroFilterFactoryBean sfBean=
				new ShiroFilterFactoryBean();
		sfBean.setSecurityManager(securityManager);
		sfBean.setLoginUrl("/doLoginUI");
		//定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
		LinkedHashMap<String,String> map=
				new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"
		map.put("/bower_components/**","anon");
		map.put("/build/**","anon");
		map.put("/dist/**","anon");
		map.put("/plugins/**","anon");
		map.put("/user/doLogin","anon");
		map.put("/doLogout","logout");
		//除了匿名访问的资源,其它都要认证("authc")后访问
		map.put("/**","user");//当写了记住我功能以后需要将认证改成user
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;
	}
	/**
	 * 配置shiro授权需要advisor对象,提供pointcut和advice
	 * 此对象中,@requiredpremissions描述的方法会作为切入点
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor 
	authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor advisor = 
				new AuthorizationAttributeSourceAdvisor();
		//advisor.setSecurityManager(securityManager);
		return advisor;

	}
	/**
	 * 通过此对象可以缓存用户的权限信息
	 * @return
	 */
	@Bean
	public CacheManager shiroCacheManager(){
		 return new MemoryConstrainedCacheManager();
	}
	/**
	 * 配置记住我管理器对象
	 * @return
	 */
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager = new CookieRememberMeManager();
		
		Cookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*60*60);
		cManager.setCookie(cookie );
		return cManager;
	}
	
	@Bean
	public SessionManager sessionManager() {
		 DefaultWebSessionManager sManager = new DefaultWebSessionManager();
		 sManager.setGlobalSessionTimeout(60*60*1000);
		return sManager;
	}

}
