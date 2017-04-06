package io.jianxun.domain.business.user;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jianxun.domain.business.AbstractBusinessEntity;;

/**
 * 系统用户
 * 
 * @author tongtn
 *
 *         createDate: 2017-03-14
 */
@Entity
@Table(name = "jx_sys_user")
public class User extends AbstractBusinessEntity implements UserDetails {

	private static final long serialVersionUID = 585375273427805552L;

	// 登录名称
	@NotNull(message = "{user.username.notnull}")
	private String username;
	// 密码
	private String password;
	// 显示名称
	private String displayName;
	// 账户锁定
	private boolean accountNonLocked;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[用户id:%d,用户名称%s]", this.getId(), this.getUsername());
	}

}
