package com.cy.pj.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

@RestController
@RequestMapping("/user/")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("doLogin")
	public JsonResult doLogin(String username,String password,boolean isRememberMe) {
		//1.创建那subject对象
		Subject subject = SecurityUtils.getSubject();
		//2.提交用户信息,传递给securityManager
		UsernamePasswordToken token=new UsernamePasswordToken(username, password);
		if(isRememberMe) {
			token.setRememberMe(true);
		}
		subject.login(token);
		return new JsonResult("login ok");
	}

	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username ,Integer pageCurrent) {

		PageObject<SysUserDeptVo> pageObject = sysUserService.findPageObjects(username, pageCurrent);
	System.out.println(pageObject);
		return  new JsonResult(pageObject);
	}

	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid){
		sysUserService.validById(id,valid, ShiroUtils.getUserName());//"admin"用户将来是登陆用户
		return new JsonResult("update ok");
	}

	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,Integer... roleIds){
		sysUserService.saveObject(entity,roleIds);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(
			Integer id){
		Map<String,Object> map=
		sysUserService.findObjectById(id);
		return new JsonResult(map);
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(
	    SysUser entity,Integer[] roleIds){
		sysUserService.updateObject(entity,roleIds);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(
				 String pwd,
				 String newPwd,
				 String cfgPwd) {
			 sysUserService.updateUserPassword(pwd, newPwd, cfgPwd);
			 return new JsonResult("update ok");
	}
}
