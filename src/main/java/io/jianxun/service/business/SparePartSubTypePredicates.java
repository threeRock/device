package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.QSparePartSubType;
import io.jianxun.domain.business.SparePartMainType;

/**
 * 角色查询
 * 
 * @author Administrator
 *
 */
public class SparePartSubTypePredicates {

	private SparePartSubTypePredicates() {
	}

	// 名称查询
	public static Predicate namePredicate(String name, SparePartMainType mainType) {
		return nameAndMainTypeAndIdNotPredicate(name, mainType, null);
	}

	public static Predicate nameAndMainTypeAndIdNotPredicate(String name, SparePartMainType mainType, Long id) {
		QSparePartSubType subtype = QSparePartSubType.sparePartSubType;
		if (id == null)
			return subtype.name.eq(name).and(subtype.mainType.id.eq(mainType.getId()));
		return subtype.id.ne(id).and(subtype.name.eq(name)).and(subtype.mainType.id.eq(mainType.getId()));
	}

}
