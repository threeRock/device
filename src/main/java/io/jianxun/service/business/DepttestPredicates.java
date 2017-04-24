package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depttest;
import io.jianxun.domain.business.QDepttest;

public class DepttestPredicates {

	private DepttestPredicates() {
	}

	// 机构名查询
	public static Predicate namePredicate(String name) {
		return nameAndIdNotPredicate(name, null);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QDepttest depart = QDepttest.depttest;
		if (id == null)
			return depart.name.eq(name);
		return depart.id.ne(id).and(depart.name.eq(name));
	}

	//根据上级机构查询
	public static Predicate parentPredicate(Depttest parent) {
		QDepttest depart = QDepttest.depttest;
		return depart.parent.eq(parent);
	}

}
