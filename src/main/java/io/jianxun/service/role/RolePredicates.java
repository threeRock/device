package io.jianxun.service.role;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.role.QRole;

/**
 * 角色查询
 * 
 * @author Administrator
 *
 */
public class RolePredicates {

	private RolePredicates() {
	}

	// 角色名查询
	public static Predicate namePredicate(String name) {
		QRole role = QRole.role;
		return role.name.contains(name);
	}

}
