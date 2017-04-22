package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QProductionLine;

/**
 * 仓库查询
 * 
 * @author Administrator
 *
 */
public class ProductionLinePredicates {

	private ProductionLinePredicates() {
	}

	// 仓库名查询
	public static Predicate namePredicate(String name) {
		return nameAndIdNotPredicate(name, null);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QProductionLine productionLine = QProductionLine.productionLine;
		if (id == null)
			return productionLine.name.eq(name);
		return productionLine.id.ne(id).and(productionLine.name.eq(name));
	}

	public static Predicate departPredicate(Depart depart) {
		QProductionLine productionLine = QProductionLine.productionLine;
		return productionLine.depart.eq(depart);
	}

	public static Predicate departSubPredicate(Depart depart) {
		QProductionLine productionLine = QProductionLine.productionLine;
		return productionLine.depart.levelCode.startsWith(depart.getLevelCode());
	}

}
