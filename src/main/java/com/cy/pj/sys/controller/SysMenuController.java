package com.cy.pj.sys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;


//@Controller
@RestController//==@Controller+//@ResponseBody
@RequestMapping("/menu/")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping("doFindObjects")
	public JsonResult doFindObjects() {
		List<Map<String, Object>> list = sysMenuService.findObjects();
		return new JsonResult(list);
	}
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysMenuService.deleteObject(id);
		
		return new JsonResult("delete ok");
	}
	
	@RequestMapping("doFindZtreeMenuNodes")
	public JsonResult doFindZtreeMenuNodes() {
		List<Node> list = sysMenuService.findZtreeMenuNodes();
		return new JsonResult(list);
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysMenu entity) {
		sysMenuService.saveObject(entity);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysMenu entity){
		sysMenuService.updateObject(entity);
		return new JsonResult("update ok");
	}
}
