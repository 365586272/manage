package com.cy.pj.sys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

/**
 * 此controller主要负责响应一些页面
 * */



@Controller
@RequestMapping("/")
public class PageController {
	@Autowired
	private SysMenuService sysMenuService;
	@RequestMapping("doLoginUI")
	public String doLoginUI(){
			return "login";
	}

	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		//model为springmvc模块用来封装响应数据的对象,底层将其放到请求作用域
		model.addAttribute("username",ShiroUtils.getUserName());
		List<SysUserMenuVo> userMenus = sysMenuService.findMenusByIds(ShiroUtils.getUser().getId());
		System.out.println(userMenus);
		model.addAttribute("userMenus",userMenus);
		return "starter";
	}
	/**
	 * 返回日志页面
	 * */
	/*
	 * @RequestMapping("log/log_list") public String doLogUI() { return
	 * "sys/log_list"; }
	 */
	
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	
	//rest风格的一种url定义,语法{url}
	//@PathVariable描述的方法参数表示他的值从url路径中获取(和参数名相同的url变量值)
	@RequestMapping("{module}/{moduleUI}")//module模块
	public String doMenuUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}
	
	
	
	
}
