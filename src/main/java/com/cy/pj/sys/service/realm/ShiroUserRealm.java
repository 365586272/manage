package com.cy.pj.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * shiro框架中的realm对象,通过此对象完成认证和授权业务数据获取和封装
 * @author DELL、
 *
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm{
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	
	/**
	 * 设置凭证匹配器,通过此对象对登录时输入的密码进行加密,
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//1.构建凭证匹配其对象
		HashedCredentialsMatcher cMathcher = new HashedCredentialsMatcher();
		//2.设置加密算法
		cMathcher.setHashAlgorithmName("MD5");
		//3.设置加密次数
		cMathcher.setHashIterations(1);
		super.setCredentialsMatcher(cMathcher);//一定要创建的对象传入父类方法
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取用户登录时输入的用户名,
          UsernamePasswordToken uToken=(UsernamePasswordToken)token;
          String username = uToken.getUsername();
          //2.基于用户名查询数据库用户信息并校验
          SysUser user = sysUserDao.findUserByUserName(username);
		//3.认证用户是否存在
          if(user==null)throw new UnknownAccountException();
		//4.认证用户是否被禁用
          if(user.getValid()==0)throw new LockedAccountException();
		ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
		//5.对用户信息进行封装并返回
          SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(
        		  user,//用户身份 
        		  user.getPassword(), //hashedCredentials已加密的密码
        		  credentialsSalt,
        		  getName());
		return info;//会返回给securityManage对象,调用认证方法对用户信息进行认证
	}

	//此方法负责授权信息的获取和封装
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("get data from database");
		//1.获取用户登录信息(当获取用户身份时,有登录时设置的身份决定)
		SysUser user =(SysUser) principals.getPrimaryPrincipal();
		Integer userId = user.getId();
		//基于用户登录id查询角色信息
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
			//3.基于角色id获取菜单id(sys_role_menus)
		Integer [] array= {};
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		if(menuIds==null||menuIds.size()==0)
		    throw new AuthorizationException();
			//4.基于菜单id获取权限标识(sys_menus)
		List<String> permissions = sysMenuDao.findPermissions(menuIds.toArray(array));
		
		Set<String> set = new HashSet<>();//set存放数据不重复
		for(String per:permissions) {
			if(!StringUtils.isEmpty(per)) {
			set.add(per);
			}
		}
		//5.封装数据并返回
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(set);
		return info;//securityManager,由此对象基于用户信息进行资源访问
	}

	/**
	 * 此方法负责认证信息的获取和封装
	 */

}
