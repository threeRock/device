package io.jianxun.service.business;

import java.time.LocalDateTime;
import java.time.Month;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QStockIn;
import io.jianxun.domain.business.SparePart;

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

	public static Predicate sparePartPredicate(Depart depart, SparePart sparepart) {
		QStockIn stockIn = QStockIn.stockIn;
		return stockIn.sparepart.eq(sparepart).and(departPredicate(depart));
	}

	public static Predicate sparePartPredicate(Depart depart, SparePart sparepart, Long id) {
		if (id != null && id != 0) {
			QStockIn stockIn = QStockIn.stockIn;
			return stockIn.id.ne(id).and(sparePartPredicate(depart, sparepart));
		}
		return sparePartPredicate(depart, sparepart);

	}

	public static Predicate sparePartBeforePredicate(SparePart sparePart, Integer year) {
		QStockIn stockIn = QStockIn.stockIn;
		System.out.println(LocalDateTime.of(year - 1, Month.DECEMBER, 31, 23, 59));
		return stockIn.lastModifiedDate.before(LocalDateTime.of(year - 1, Month.DECEMBER, 31, 23, 59))
				.and(sparePartPredicate(sparePart.getDepart(), sparePart));
	}

	public static Predicate sparePartAfterPredicate(SparePart sparePart, Integer year) {
		return sparePartBeforePredicate(sparePart, year + 1);
	}

}
