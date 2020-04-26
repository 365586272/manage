package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleMenuDao {
    
	int deleteObjectsByMenuId(Integer menuId);
	//根据用户名删除信息
	int deleteObjectsByRoleId(Integer roleId);
	
	int insertObjects(@Param("roleId")Integer roleId,@Param("menuIds")Integer... menuIds);
	
	//基于角色id查询caidanid
	
	List<Integer> findMenuIdsByRoleId(Integer roleId);
	
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")Integer... roleIds);
}
