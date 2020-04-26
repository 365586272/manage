package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Mapper
public interface SysMenuDao {

	/**
	 *查询所有菜单,以及这个菜单对应的上级菜单 
	 */
	List<Map<String, Object>> findObjects();
	//是否有下级菜单
	int getChildCount(Integer id);
	//删除菜单
	int deleteObject(Integer id);
	//查询上级菜单
	@Select("select id,name,parentId from sys_menus")
	List<Node> findZtreeMenuNodes();
	//保存操作
    int	insertObject(SysMenu entity);
    //更新操作
    int updateObject(SysMenu entity);
    
    List<String> findPermissions(@Param("menuIds")Integer... menuIds);

     List<SysUserMenuVo> findMenusByIds(List<Integer> menuIds);
}
