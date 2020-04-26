package com.cy.pj.sys.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

import lombok.extern.slf4j.Slf4j;

@Transactional(readOnly = false,
isolation = Isolation.READ_COMMITTED,
rollbackFor = Throwable.class,propagation = Propagation.REQUIRED)
@Service
//@Slf4j
public class SysUserServiceImpl implements SysUserService {

	//private Logger log=LoggerFactory.getLogger(SysUserServiceImpl.class);
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Transactional(readOnly = true)
	@RequiredLog("用户查询")
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		
		System.out.println("log.service.Thread"+Thread.currentThread().getName());
		//1.验证参数的有效性
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码不正确");
		//rowcount页面总记录
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount<1) 
			throw new ServiceException("没有记录");
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
		return new PageObject<>(pageCurrent, pageSize, rowCount, records);
	}
	
	@RequiresPermissions("sys:user:update")
	//@Transactional
	@RequiredLog("禁用启用")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		
		if(id==null||id<=0)
			throw new IllegalArgumentException("参数不合法id="+id);
		if(valid!=0&&valid!=1)
			throw new IllegalArgumentException("参数不合法valid="+valid);
		if(StringUtils.isEmpty(modifiedUser))
			throw new ServiceException("修改用户不能为空");
		int rows = sysUserDao.validById(id, valid, modifiedUser);
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
		return rows;
	}
	@Override
	public int saveObject(SysUser entity, Integer... roleIds) {
		//log.info("method start"+System.currentTimeMillis());
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new ServiceException("密码不能为空");
		if(roleIds==null || roleIds.length==0)
			throw new ServiceException("至少要为用户分配角色");
		//2.保存用户自身信息
		//2.1对密码进行加密
		String source=entity.getPassword();
		//UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法。
		String salt=UUID.randomUUID().toString();
		//DigestUtils.md5Digest(bytes);
		SimpleHash sh=new SimpleHash(//Shiro框架
				"MD5",//algorithmName 算法名称
				source,//原密码
				salt, //盐值
				1);//hashIterations表示加密次数
		entity.setSalt(salt);
		entity.setPassword(sh.toHex());
		int rows = sysUserDao.insertObject(entity);
		sysUserRoleDao.insertObject(entity.getId(), roleIds);
		//log.info("method end"+System.currentTimeMillis());
		//log.info("total time :"+(end-start));
		return rows;
	}
	@Override
	public Map<String, Object> findObjectById(Integer userId) {
		if(userId==null||userId<1)
			throw new IllegalArgumentException("用户id不正确");
		SysUserDeptVo user = sysUserDao.findObjectById(userId);
		if(user==null)
			throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		if(roleIds==null||roleIds.size()==0)
			throw new ServiceException("没有查询到对应角色信息");
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	@Override
	public int updateObject(SysUser entity, Integer... roleIds) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为其指定角色");
		int rows = sysUserDao.updateObject(entity);
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObject(entity.getId(), roleIds);
		return rows;
	}
	

	@Override
	public int updateUserPassword(String sourcePassword, String newPassword, String cfgPassword) {
		//1.参数校验
		//1)非空校验
		if(StringUtils.isEmpty(sourcePassword))
			throw new IllegalArgumentException("原密码不能为空");
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		if(StringUtils.isEmpty(cfgPassword))
			throw new IllegalArgumentException("确认密码不能为空");
		//2)新密码和确认密码是否相同
		if(!newPassword.equals(cfgPassword))
			throw new ServiceException("两次输入密码不相同");
		//3)原密码是否和数据库的密码是否正确
		//3.1)获取登录用户
		SysUser user=ShiroUtils.getUser();
		SimpleHash sh = new  SimpleHash("MD5", sourcePassword,
				     user.getSalt(), 1);
		sourcePassword=sh.toHex();
		if(!sourcePassword.equals(user.getPassword()))
                 throw new ServiceException("原密码不正确");		
		//2.修改密码
		//1)获取盐值
		String salt=UUID.randomUUID().toString();
		//2)对用户输入的密码进行加密
		sh = new SimpleHash("MD5", newPassword, salt, 1);
		//3)更新密码
		newPassword=sh.toHex();
		//4)返回结果
		//SysUser user=ShiroUtils.getUser();
		int rows = sysUserDao.updateUserPassword(user.getId(), newPassword, salt);
		return rows;
	}

	


}
