package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {

	List<SysUserDeptVo> findPageObjects(@Param("username")String username,
			                           @Param("startIndex")Integer startIndex,
			                           @Param("pageSize") Integer pageSize);
	/**
	 * 按条件统计用户记录总数
	 * @param username
	 * @return
	 */
	int getRowCount(String username);
	
	int validById(@Param("id")Integer id,
			      @Param("valid")Integer valid,
			      @Param("modifiedUser")String modifiedUser);
	
	int insertObject(SysUser entity);
	
	SysUserDeptVo findObjectById(Integer id);
	
	int updateObject(SysUser entity);
	
	
	/**
	 * 基于用户名获取用户信息
	 * @param username
	 * @return
	 */
	@Select("select * from sys_users where username=#{username}")
	SysUser findUserByUserName(String username); 
	//基于用户id修改密码
	int updateUserPassword(Integer id,String password,String salt);

}
