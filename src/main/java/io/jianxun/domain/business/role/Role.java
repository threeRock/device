package io.jianxun.domain.business.role;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.AbstractBusinessEntity;

@Entity
@Table(name = "jx_sys_user")
public class Role extends AbstractBusinessEntity {

	private static final long serialVersionUID = -4353349855697950355L;

	/**
	 * 角色显示名称
	 */
	@NotNull(message = "{role.name.notnull}")
	@Length(min = 6, message = "{role.name.length}")
	private String name;

	// 权限列表
	private List<String> permissions = Lists.newArrayList();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "jx_sys_role_pers")
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
