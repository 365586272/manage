package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysLog;

@SpringBootTest
public class TestSysLogDao {

	@Autowired
	private SysLogDao sysLogDao;

	@Test
    public void TestSysLogDao() {
		
		List<SysLog> list = sysLogDao.findPageObjects("",0,4);
		System.out.println(list);
		
    }

	@Test
    public void TestSysLogDaoget() {
		
		int rows = sysLogDao.getRowCount("");
		System.out.println(rows);
		
    }
	@Test
	public void TestDeleteObjects() {
		int rows = sysLogDao.deleteObjects(99,100);
		System.out.println(rows);
	}
}
