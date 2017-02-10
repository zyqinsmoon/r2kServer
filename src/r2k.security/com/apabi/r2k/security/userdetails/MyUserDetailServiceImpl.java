package com.apabi.r2k.security.userdetails;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.service.AuthRoleService;
import com.apabi.r2k.security.service.AuthUserService;
import com.apabi.r2k.security.utils.AuthUserUtils;
import com.apabi.r2k.security.utils.AuthUtil;




/**
 * 实现SpringSecurity的UserDetailsService接口,实现获取用户Detail信息的回调函数.
 *
 * @author 
 */
@Service("myUserDetailService")
public class MyUserDetailServiceImpl implements UserDetailsService, UserDetailFactory {

	@Resource
	private AuthUserService authUserService;
	@Resource
	private AuthOrgService authOrgService;
	@Resource
	private AuthRoleService authRoleService;
	/**
	 * 获取用户Detail信息的回调函数.
	 */
	public UserDetails loadUserByUsername(String loginName)  {
		//loginName = loginName.substring(2);
		AuthUser authUser = null;
		try {
			authUser = authUserService.getByLoginName(loginName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (authUser == null) {
			throw new UsernameNotFoundException("用户" + loginName + " 不存在");
		}
		//判断用户是那个机构的
		 if (authUser.getAuthOrgId()==null) {
			// 如果该用戶不属于任何机构则不允许登陆
			throw new UsernameNotFoundException("用户" + loginName + "未关联机构");
		}else{
			try {
				AuthOrg authOrg = authOrgService.getOrgObject(authUser.getAuthOrgId());
				authUser.setAuthOrg(authOrg);
				authUser.setCurrentOrg(authOrg);
				List<AuthRole> authRoles = authRoleService.getRolesByOrg(authUser.getCurrentOrg().getOrgId());
				authUser.setAuthRoleList(authRoles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(authUser.getEnabled()==null||authUser.getEnabled()==-1){
			throw new UsernameNotFoundException("用户" + loginName + "已被停用");
		}
			 //判断用户是否被赋予了角色
		if(authUser.getAuthRoleList()==null||authUser.getAuthRoleList().size()==0){
			// 如果该用戶无角色信息则不允许登陆
			throw new UsernameNotFoundException("用户" + loginName + "未关联角色");
		}

		authUser.setUserAgent(GlobalConstant.USER_AGENT_ADMIN);
		final Collection<GrantedAuthority> grantedAuths = AuthUtil.obtainGrantedAuthorities(authUser.getAuthRoleList());
		final boolean enabled = true;
		final boolean accountNonExpired = true;
		final boolean credentialsNonExpired = true;
		final boolean accountNonLocked = true;
		final User userdetail = new MyUserDetails(authUser, enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				grantedAuths);

		return userdetail;
	}
	
	@Override
	public MyUserDetails createScreenUserDeatil(String orgId, String devId, String userAgent, String softVersion)   {
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createScreenUser(authOrg, devId, userAgent, softVersion);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		DevTypeEnum devType = DevTypeEnum.findName(userAgent);
		return createUserDetails(authUser,devType.getId());
	}
	
	@Override
	public MyUserDetails createIPadUserDetails(String userName, String password,
			String orgId, String userAgent) throws AuthenticationException {
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createAuthUser(userName,authOrg, userAgent);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		return createUserDetails(authUser,DevTypeEnum.iPad.getId());
	}
	
	@Override
	public MyUserDetails createIPhoneUserDetails(String userName, String password, String orgId, String userAgent) throws AuthenticationException {
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createAuthUser(userName, authOrg, userAgent);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		return createUserDetails(authUser,DevTypeEnum.iPhone.getId());
	}
	
	@Override
	public MyUserDetails createWeiXinUserDetails(String userName, String password, String orgId, String userAgent)
			throws AuthenticationException {
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createAuthUser(userName, authOrg, userAgent);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		return createUserDetails(authUser,DevTypeEnum.WeiXin.getId());
	}
	
	@Override
	public MyUserDetails createSlaveUserDetails(String orgId, String devId, String userAgent) throws AuthenticationException{
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createDeviceUser(authOrg, devId, userAgent);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		return createUserDetails(authUser,DevTypeEnum.Slave.getId());
	}
	
	@Override
	public MyUserDetails createNormalUserDetails(String userName, String password, String orgId, String userAgent)
			throws AuthenticationException {
		AuthOrg authOrg;
		AuthUser authUser = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
			authUser = AuthUserUtils.createAuthUser(userName, authOrg, userAgent);
		} catch (Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e.getCause());
		}
		DevTypeEnum devType = DevTypeEnum.findName(userAgent);
		return createUserDetails(authUser,devType.getId());
	}

	//通用创建UserDetail方法
	public MyUserDetails createUserDetails(AuthUser authUser,int deviceType) throws AuthenticationException{
		if(authUser == null){
			return null;
		}
		String msg = null;
		Collection<GrantedAuthority> grantedAuthorities = null;
		MyUserDetails loadedUser = null;
		String orgId = authUser.getAuthOrg().getOrgId();
		try {
			msg = "获取机构权限异常";
			String devType = DevTypeEnum.findId(deviceType).getName();
			List<AuthRole> authRoles = authRoleService.getRoles(orgId, devType, 1);		//获取当前机构前台权限
			if(authRoles != null && authRoles.size() > 0){
				authUser.setAuthRoleList(authRoles);
				grantedAuthorities = AuthUtil.obtainGrantedAuthorities(authRoles);
				loadedUser = new MyUserDetails(authUser, true, true, true, true, grantedAuthorities);
			}else{
				msg = "该机构没有任何权限";
			}
		} catch (Exception e) {
			throw new AuthenticationServiceException(msg);
		}
        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
	}

}
