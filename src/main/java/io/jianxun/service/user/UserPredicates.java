package io.jianxun.service.user;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.user.QUser;

/**
 * 用户相关查询条件
 * 
 * @author Administrator
 *
 */
public class UserPredicates {

	private UserPredicates() {
	}

	// 用户名查询
	public static Predicate usernamePredicate(String username) {
		QUser user = QUser.user;
		return user.username.eq(username);
	}

}
