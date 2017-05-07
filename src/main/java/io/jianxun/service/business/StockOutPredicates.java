package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QStockOut;

/**
 * 出库单查询
 * 
 * @author Administrator
 *
 */
public class StockOutPredicates {

	private StockOutPredicates() {
	}

	public static Predicate departPredicate(Depart depart) {
		QStockOut stockOut = QStockOut.stockOut;
		return stockOut.depart.eq(depart);
	}

}
