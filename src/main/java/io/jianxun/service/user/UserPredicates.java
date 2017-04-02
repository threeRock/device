package io.jianxun.service.user;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.user.QUser;

public class UserPredicates {

	public static Predicate usernamePredicate(String username) {
		QUser user = QUser.user;
		return user.username.eq(username);
	}

}
