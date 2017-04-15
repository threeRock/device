package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.QSparePartMainType;

/**
 * 角色查询
 * 
 * @author Administrator
 *
 */
public class SparePartMainTypePredicates {

	private SparePartMainTypePredicates() {
	}

	// 角色名查询
	public static Predicate namePredicate(String name) {
		return nameAndIdNotPredicate(name, null);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QSparePartMainType maintype = QSparePartMainType.sparePartMainType;
		if (id == null)
			return maintype.name.eq(name);
		return maintype.id.ne(id).and(namePredicate(name));
	}

}
