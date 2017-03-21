package io.jianxun.domain.business.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.jianxun.domain.business.BusinessEntity;;

/**
 * 系统用户
 * 
 * @author tongtn
 *
 *         createDate: 2017-03-14
 */
@Entity
@Table(name = "jx_sys_user")
public class User extends BusinessEntity {

	private static final long serialVersionUID = 585375273427805552L;

	//登录名称
	@NotNull(message = "{user.username.notnull}")
	private String username;
	//密码
	private String passowrd;
	//显示名称
	private String displayName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassowrd() {
		return passowrd;
	}

	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	
	


}
