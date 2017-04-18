package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QStorehouse;

/**
 * 仓库查询
 * 
 * @author Administrator
 *
 */
public class StorehousePredicates {

	private StorehousePredicates() {
	}

	// 仓库名查询
	public static Predicate namePredicate(String name) {
		return nameAndIdNotPredicate(name, null);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QStorehouse storehouse = QStorehouse.storehouse;
		if (id == null)
			return storehouse.name.eq(name);
		return storehouse.id.ne(id).and(storehouse.name.eq(name));
	}

	public static Predicate departPredicate(Depart depart) {
		QStorehouse storehouse = QStorehouse.storehouse;
		return storehouse.depart.eq(depart);
	}

}
