package com.cy.pj.sys.serviceimpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredCache;
import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Service
public class SysMenuServiceImpl implements SysMenuService{

	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	@Override
	public List<Map<String, Object>> findObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		if(list==null||list.size()==0)
			throw new ServiceException("没有对应的菜单信息");
		return list;
	}
	
	@Override
	public int deleteObject(Integer id) {
		//1.验证数据合法性
		if(id==null||id<=0)
			throw new IllegalArgumentException("请先选择");
          //基于id进行子元素查询
		int count = sysMenuDao.getChildCount(id);
		if(count>0)
			throw new ServiceException("请先删除子菜单");
		//删除角色,菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//4.删除菜单元素
		int rows = sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此菜单可能已经不存在");
		return rows;
	}

	@RequiredCache
	@Override
	public List<Node> findZtreeMenuNodes() {
		List<Node> list = sysMenuDao.findZtreeMenuNodes();
		if(list==null||list.size()==0)
			throw new IllegalArgumentException("没有查询到菜单");
		return list;
	}

	@Override
	public int saveObject(SysMenu entity) {
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("菜单名不能为空");
		int rows;
		//2.保存数据
		try{
		rows=sysMenuDao.insertObject(entity);
		}catch(Exception e){
		e.printStackTrace();
		throw new ServiceException("保存失败");
		}
		//3.返回数据
		return rows;
	}

	@Override
	public int updateObject(SysMenu entity) {
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("菜单名不能为空");
			
			//2.更新数据
			int rows=sysMenuDao.updateObject(entity);
			if(rows==0)
			throw new ServiceException("记录可能已经不存在");
			//3.返回数据
			return rows;
	}

	@Override
	public List<SysUserMenuVo> findMenusByIds(Integer userId) {
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		List<SysUserMenuVo> result = sysMenuDao.findMenusByIds(menuIds);
		return result;
	}

}
