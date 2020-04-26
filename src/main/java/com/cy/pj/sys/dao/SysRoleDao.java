package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleDao {
	/**基于条件从指定位置查询当前页数据*/
	List<SysRole> findPageObjects(String name,Integer startIndex,Integer pageSize);
	/**基于条件查询统计数据*/
    int getRowCount(String name);
    
    int deleteObject(Integer id);
    
    int insertObject(SysRole entity);
    
    /**
     * 基于id查询以及对于的
     */
    SysRoleMenuVo findObjectById(Integer id);
    
    int updateObject(SysRole entity);
    //查询角色id，name
    List<CheckBox> findObjects();
    
}
