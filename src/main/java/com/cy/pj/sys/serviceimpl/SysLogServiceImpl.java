package com.cy.pj.sys.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService{

	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		//pageCurrent为页码
		//1.验证参数的有效性
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码不正确");
		//rowcount页面总记录
		int rowCount=sysLogDao.getRowCount(username);
		if(rowCount==0) 
			throw new ServiceException("没有记录");
		//页面大小
		int pageSize=3;
		////limit (页码-1)*每页显示记录数
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysLog> records=sysLogDao.findPageObjects(username, startIndex, pageSize);
		//对分页信息及当前页记录进行封装
		/*
		 * PageObject<SysLog> pageObject = new PageObject();
		 * pageObject.setPageCurrent(pageCurrent); pageObject.setPageSize(pageSize);
		 * pageObject.setRecords(records); pageObject.setRowCount(rowCount);
		 * pageObject.setPageCount((rowCount-1)/pageSize+1);
		 */
		return new PageObject<SysLog>(pageCurrent, pageSize, rowCount, records);
	}

	@Override
	public int deleteObjects(Integer... ids) {
		if(ids==null||ids.length==0)
			throw new IllegalArgumentException("请选择一个要删除的对象");
		int rows;
		try {
			rows= sysLogDao.deleteObjects(ids);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException("系统故障,正在恢复中");
		}
		if(rows==0)
			throw new ServiceException("记录可能不存在了");
		
		return rows;
	}

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(SysLog entity) {
	System.out.println("log.service.Thread"+Thread.currentThread().getName());	
	try {	
	Thread.sleep(5000);}catch(Exception e) {}
		sysLogDao.insertObject(entity);
		
	}
	
	
	

}
