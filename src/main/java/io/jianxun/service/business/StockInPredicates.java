package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QStockIn;

/**
 * 仓库查询
 * 
 * @author Administrator
 *
 */
public class StockInPredicates {

	private StockInPredicates() {
	}

	public static Predicate departPredicate(Depart depart) {
		QStockIn stockIn = QStockIn.stockIn;
		return stockIn.depart.eq(depart);
	}

}
