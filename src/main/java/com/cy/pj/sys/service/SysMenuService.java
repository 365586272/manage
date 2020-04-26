package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

public interface SysMenuService {

	List<Map<String, Object>> findObjects();
    //基于菜单id删除操作
	int deleteObject(Integer id);
	
	List<Node> findZtreeMenuNodes();
	
	int saveObject(SysMenu entity);
	
	int updateObject(SysMenu entity);
	
	List<SysUserMenuVo> findMenusByIds(Integer userId);
}
