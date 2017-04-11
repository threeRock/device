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
		return usernameAndIdNotPredicate(username, null);
	}

	// 用户名及ID查询
	public static Predicate usernameAndIdNotPredicate(String username, Long id) {
		QUser user = QUser.user;
		if (id == null)
			return user.username.eq(username);
		return user.id.ne(id).and(usernamePredicate(username));
	}

}
