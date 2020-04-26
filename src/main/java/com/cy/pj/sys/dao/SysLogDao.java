package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysLog;

@Mapper
public interface SysLogDao {

	List<SysLog> findPageObjects(@Param("username")String username,
			                      @Param("startIndex")Integer startIndex,
			                      @Param("pageSize")Integer pageSize);
	
	int getRowCount(@Param("username")String username);
	
	int deleteObjects(@Param("ids")Integer... ids);
	
	int insertObject(SysLog entity);
}
