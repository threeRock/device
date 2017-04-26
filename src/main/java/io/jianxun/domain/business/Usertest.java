package io.jianxun.domain.business;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;

import io.jianxun.domain.AbstractBusinessDepartEntity;
import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 系统用户
 * 
 * @author tongtn
 *
 *         createDate: 2017-03-14
 */
@Entity
@Table(name = "jx_test_usertest")
public class Usertest extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371614245790559518L;
	// 登录名称
	@NotBlank(message = "{usertest.username.notnull}")
	private String username;
	// 密码
	private String password;
	// 显示名称
	private String displayName;
	
	@ManyToOne
	@JoinColumn(name = "depttest_id")
	@NotNull(message = "{depttest.notnull}")
	private Depttest depttest; 

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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Depttest getDepttest() {
		return this.depttest;
	}

	public void setDepttest(Depttest d) {
		this.depttest = d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[用户id:%d,用户名称:%s]", this.getId(), this.getUsername());
	}

}
