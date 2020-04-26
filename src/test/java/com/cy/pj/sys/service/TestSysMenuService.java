package com.cy.pj.sys.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserDeptVo;

@SpringBootTest
public class TestSysMenuService {

	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysUserDao sysUserDao;

	@Test
	public void testSaveObject() {
		SysMenu e=new SysMenu();
		e.setName("ash");
	int rows = sysMenuService.saveObject(e);
	System.out.println(rows);
		
	}
	
	@Test
	public void testUser() {
		List<SysUserDeptVo> list = sysUserDao.findPageObjects("admin", 0, 5);
		list.forEach((s)->System.out.println(s));
	}
}
