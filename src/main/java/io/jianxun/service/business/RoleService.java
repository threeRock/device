package io.jianxun.service.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jianxun.domain.business.PermissionDef;
import io.jianxun.domain.business.Role;
import io.jianxun.service.AbstractBaseService;

@Service
public class RoleService extends AbstractBaseService<Role> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String SUPER_ROLE_NAME = "系统管理员角色";

	/**
	 * 创建所有权限角色
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Role createSuperRole() {
		Role role;
		if (this.repository.count() == 0) {
			role = new Role();
			role.setName(SUPER_ROLE_NAME);
			role.setPermissions(PermissionDef.getPermissionCodeList());
			return this.save(role);
		}
		return null;

	}

}
