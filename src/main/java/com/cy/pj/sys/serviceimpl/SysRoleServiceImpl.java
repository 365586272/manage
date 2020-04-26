package com.cy.pj.sys.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private  SysRoleDao sysRoleDao;
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("输入的页码值不正确");
		int rowCount = sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		return new PageObject<>(pageCurrent, pageSize, rowCount, records) ;
	}
	@Override
	public int deleteObject(Integer id) {
		
		if(id==null||id<0)
			throw new IllegalArgumentException("id值无效");
		sysRoleMenuDao.deleteObjectsByRoleId(id);
		sysUserRoleDao.deleteObjectsByRoleId(id);
		int rows = sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录可能不存在了");
		return rows;
	}
	@Override
	public int saveObject(SysRole entity, Integer... menuIds) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色分配权限");
	    int rows = sysRoleDao.insertObject(entity);
	    sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		if(id==null||id<0)
			throw new IllegalArgumentException("id值不合法");
		SysRoleMenuVo result = sysRoleDao.findObjectById(id);
		if(result==null)
			throw new ServiceException("此记录已经不存在了");
		return result;
	}
	@Override
	public int updateObject(SysRole entity,Integer...menuIds) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
	    	throw new ServiceException("必须为角色指定一个权限");
		int rows = sysRoleDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("对象可能已经不存在了");
		sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		
		return rows;
	}
	@Override
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}

}
