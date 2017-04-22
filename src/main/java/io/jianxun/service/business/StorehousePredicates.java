package io.jianxun.service.business;

import org.apache.commons.lang3.StringUtils;

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

	public static Predicate codeAndIdNotPredicate(String code, Long id) {
		QStorehouse storehouse = QStorehouse.storehouse;
		if (id == null)
			return storehouse.code.eq(code);
		return storehouse.id.ne(id).and(storehouse.code.eq(code));
	}

	public static Predicate departSubPredicate(Depart depart) {
		QStorehouse storehouse = QStorehouse.storehouse;
		if (StringUtils.isBlank(depart.getLevelCode()))
			return null;
		return storehouse.depart.levelCode.startsWith(depart.getLevelCode());
	}

}
