package io.jianxun.service.business;

import java.time.LocalDateTime;
import java.time.Month;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QStockOut;
import io.jianxun.domain.business.SparePart;

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

	public static Predicate sparePartPredicate(Depart depart, SparePart sparePart) {
		QStockOut stockOut = QStockOut.stockOut;
		return stockOut.sparePart.eq(sparePart).and(departPredicate(depart));

	}

	public static Predicate sparePartPredicate(Depart depart, SparePart sparePart, Long id) {
		if (id != null && id > 0) {

			QStockOut stockOut = QStockOut.stockOut;
			return stockOut.id.ne(id).and(sparePartPredicate(depart, sparePart));
		}
		return sparePartPredicate(depart, sparePart);

	}

	public static Predicate sparePartBeforePredicate(SparePart sparePart, int year) {
		QStockOut stockOut = QStockOut.stockOut;
		return stockOut.lastModifiedDate.before(LocalDateTime.of(year - 1, Month.DECEMBER, 31, 23, 59))
				.and(sparePartPredicate(sparePart.getDepart(), sparePart));

	}

	public static Predicate sparePartAfterPredicate(SparePart sparePart, Integer year) {
		return sparePartBeforePredicate(sparePart, year + 1);
	}

	public static Predicate sparePartBetweenPredicate(SparePart sparePart, Integer year) {
		QStockOut stockOut = QStockOut.stockOut;
		return stockOut.lastModifiedDate
				.between(LocalDateTime.of(year - 1, Month.DECEMBER, 31, 23, 59),
						LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59))
				.and(sparePartPredicate(sparePart.getDepart(), sparePart));
	}

}
