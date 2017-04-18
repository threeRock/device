package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QUser;

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
		return user.id.ne(id).and(user.username.eq(username));
	}

	public static Predicate departPredicate(Depart depart) {
		QUser user = QUser.user;
		return user.depart.eq(depart);
	}

}
