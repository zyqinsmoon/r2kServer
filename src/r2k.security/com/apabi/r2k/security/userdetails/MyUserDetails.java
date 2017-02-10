/**
 *
 */
package com.apabi.r2k.security.userdetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.apabi.r2k.security.model.AuthUser;

/**
 * 扩展Spring Security默认的{@link User}，添加当前管理员及其权限信息.
 *
 */
public class MyUserDetails extends User {

	private static final long serialVersionUID = 1931718733626558659L;

	private static final String ROLE_SA = "ROLE_1";
	
	private AuthUser authUser;
	
	private Set<Long> sites = new HashSet<Long>();
	
	private Set<String> roles = new HashSet<String>();

	public MyUserDetails(final AuthUser authUser, final boolean enabled, final boolean accountNonExpired,
			final boolean credentialsNonExpired, final boolean accountNonLocked, 
			final Collection<GrantedAuthority> authorities) {
		super(authUser.getLoginName(), authUser.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.authUser = authUser;
		for (final GrantedAuthority auth : authorities) {
			roles.add(auth.getAuthority());
		}
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(final AuthUser authUser) {
		this.authUser = authUser;
	}
	
	/**
	 * @return 是否为超级管理员（ROLE_超级管理员）
	 */
	public boolean isSa() {
		return roles.contains(ROLE_SA);
	}
	
	/**
	 * @return 是否拥有站点的访问权限.
	 */
	public boolean canAccessSite(final Long siteId) {
		return sites.contains(siteId);
	}

	

	@Override
	public boolean equals(final Object rhs) {
		return super.equals(rhs);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}