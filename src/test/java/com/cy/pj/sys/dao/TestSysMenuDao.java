package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestSysMenuDao {

	@Autowired
	private SysMenuDao sysMenuDao;

	@Test
	public void testFindObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		Assertions.assertNotEquals(null, list);//断言测试
		/*
		 * for(Map<String, Object> list1:list) { System.out.println(list1); }
		 */
		//jdk8 lambda表达式
		list.forEach((map)->System.out.println(map));
		
	}
}
