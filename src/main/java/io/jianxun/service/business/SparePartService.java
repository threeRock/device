package io.jianxun.service.business;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.types.ExpressionUtils;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.domain.business.StockOut;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.web.dto.DepartTree;
import io.jianxun.web.dto.SparePartUseDto;

@Service
public class SparePartService extends AbstractBaseService<SparePart> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean validateNameUnique(String name, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(SparePartPredicates.departPredicate(depart),
				SparePartPredicates.nameAndIdNotPredicate(name, id)));
	}

	public List<DepartTree> getSparePartTree() {
		return convertEntityToDepartTree(departService.getUserDepart());
	}

	private List<DepartTree> convertEntityToDepartTree(List<Depart> userDepart) {
		List<DepartTree> tree = Lists.newArrayList();
		for (Depart depart : userDepart) {
			DepartTree d = new DepartTree();
			d.setId(depart.getId());
			if (depart.getParent() != null)
				d.setpId(depart.getParent().getId());
			d.setName(depart.getName());
			d.setUrl("device/sparepart/page");
			d.setDivid("#sparePart-page-layout");
			tree.add(d);
		}
		return tree;
	}

	public void getStock(List<SparePart> parts) {
		for (SparePart sparePart : parts) {
			sparePart.setStock(getStock(sparePart));
		}

	}

	public Integer getStock(SparePart sparePart) {
		Integer in = stockInService.getSparePartCapacity(sparePart.getDepart(), sparePart, null);
		Integer out = stockOutService.getSparePartCapacity(sparePart.getDepart(), sparePart, null);
		return in - out;
	}

	// 获取备件消耗情况
	public SparePartUseDto getUse(Integer year, SparePart sparePart) {
		if (year == null || year == 0)
			year = LocalDate.now().getYear();
		SparePartUseDto use = new SparePartUseDto();
		use.setYear(year);
		use.setSparePart(sparePart);

		if (year == null || year == 0)
			year = LocalDate.now().getYear();
		int before = stockInService.getSparePartBeforeCapacity(sparePart, year)
				- stockOutService.getSparePartBeforeCapacity(sparePart, year);
		use.setBeforeStock(before);
		getMonthUse(use, sparePart, year);
		int after = stockInService.getSparePartAfterCapacity(sparePart, year);
		after = after - stockOutService.getSparePartAfterCapacity(sparePart, year);
		use.setAfterStock(after);
		return use;
	}

	private void getMonthUse(SparePartUseDto use, SparePart sparePart, Integer year) {
		Map<Month, Integer> maps = Maps.newHashMap();
		List<StockOut> out = this.stockOutService.getOutByYear(sparePart, year);
		for (StockOut stockOut : out) {
			Month month = stockOut.getLastModifiedDate().getMonth();
			Integer i = stockOut.getCapacity();
			if (maps.containsKey(month)) {
				Integer t = maps.get(month);
				t = t + i;
			} else
				maps.put(month, i);
		}
		if (maps.get(Month.JANUARY) != null)
			use.setOne(maps.get(Month.JANUARY));
		if (maps.get(Month.FEBRUARY) != null)
			use.setTwo(maps.get(Month.FEBRUARY));
		if (maps.get(Month.MARCH) != null)
			use.setThree(maps.get(Month.MARCH));
		if (maps.get(Month.APRIL) != null)
			use.setFour(maps.get(Month.APRIL));
		if (maps.get(Month.MAY) != null)
			use.setFive(maps.get(Month.MAY));
		if (maps.get(Month.JUNE) != null)
			use.setSix(maps.get(Month.JUNE));
		if (maps.get(Month.JULY) != null)
			use.setSeven(maps.get(Month.JULY));
		if (maps.get(Month.AUGUST) != null)
			use.setEight(maps.get(Month.AUGUST));
		if (maps.get(Month.SEPTEMBER) != null)
			use.setNight(maps.get(Month.SEPTEMBER));
		if (maps.get(Month.OCTOBER) != null)
			use.setTen(maps.get(Month.OCTOBER));
		if (maps.get(Month.NOVEMBER) != null)
			use.setEleven(maps.get(Month.NOVEMBER));
		if (maps.get(Month.DECEMBER) != null)
			use.setTwelve(maps.get(Month.DECEMBER));
	}

	@Autowired
	private DepartService departService;

	@Autowired
	private StockInService stockInService;
	@Autowired
	private StockOutService stockOutService;

}
