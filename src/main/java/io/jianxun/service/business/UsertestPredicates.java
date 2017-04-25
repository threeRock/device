package io.jianxun.service.business;

import org.apache.commons.lang3.StringUtils;
import com.querydsl.core.types.Predicate;

//import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Depttest;
//import io.jianxun.domain.business.QUser;
import io.jianxun.domain.business.QUsertest;

public class UsertestPredicates {

	private UsertestPredicates() {
	}

	// 用户名查询
	public static Predicate usernamePredicate(String username) {
		return usernameAndIdNotPredicate(username, null);
	}

	// 用户名及ID查询
	public static Predicate usernameAndIdNotPredicate(String username, Long id) {
		QUsertest user = QUsertest.usertest;
		if (id == null)
			return user.username.eq(username);
		return user.id.ne(id).and(user.username.eq(username));
	}

	public static Predicate depttestPredicate(Depttest depart) {
		QUsertest usertest = QUsertest.usertest;
		return usertest.depttest.eq(depart);
	}

	public static Predicate departSubPredicate(Depttest depart) {
		QUsertest user = QUsertest.usertest;
		if (StringUtils.isBlank(depart.getLevelCode()))
			return null;
		return user.depttest.levelCode.startsWith(depart.getLevelCode());
	}

}
