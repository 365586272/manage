package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;

@SpringBootTest
public class TestSysLogService {

	@Autowired
	private SysLogService sysLogService;

	@Test
	public void test() {
		PageObject<SysLog> fs = sysLogService.findPageObjects("", 1);
		System.out.println(fs);    		

	}
}
