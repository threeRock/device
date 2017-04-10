package io.jianxun.domain.business.role;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.AbstractBusinessEntity;

/**
 * 用户权限信息集
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "jx_sys_role")
public class Role extends AbstractBusinessEntity {

	private static final long serialVersionUID = -4353349855697950355L;

	// 角色名称
	@NotBlank(message = "{role.name.notblank}")
	private String name;

	// 权限列表
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "jx_sys_role_pers")
	private List<String> permissions = Lists.newArrayList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
