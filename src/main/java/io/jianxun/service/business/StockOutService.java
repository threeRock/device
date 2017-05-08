package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.domain.business.StockOut;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.web.dto.DepartTree;

@Service
public class StockOutService extends AbstractBaseService<StockOut> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<DepartTree> getStockOutTree() {
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
			d.setUrl("stockout/page");
			d.setDivid("#stockout-page-layout");
			tree.add(d);
		}
		return tree;
	}

	public Integer getSparePartCapacity(Depart depart, SparePart sparepart, Long id) {
		return findActiveAll(StockOutPredicates.sparePartPredicate(depart, sparepart, id), new Sort("id")).stream()
				.mapToInt(stockOut -> stockOut.getCapacity()).sum();

	}

	public boolean validateCapacity(StockOut stockout) {
		Integer outCount = getSparePartCapacity(stockout.getDepart(), stockout.getSparePart(), stockout.getId());
		Integer inCount = this.stockInService.getSparePartCapacity(stockout.getDepart(), stockout.getSparePart(), null);
		return inCount >= outCount + stockout.getCapacity();
	}

	public boolean validateDeleteCapacity(StockOut stockout) {
		Integer outCount = getSparePartCapacity(stockout.getDepart(), stockout.getSparePart(), stockout.getId());
		Integer inCount = this.stockInService.getSparePartCapacity(stockout.getDepart(), stockout.getSparePart(), null);
		return inCount >= outCount;
	}

	@Override
	@Transactional(readOnly = false)
	public StockOut delete(Long id) {
		StockOut out = findActiveOne(id);
		if (out == null)
			throw new BusinessException(localeMessageSourceService.getMessage("stockout.notfound"));
		if (!validateDeleteCapacity(out))
			throw new BusinessException(localeMessageSourceService.getMessage("stockout.capacity.notenough"));
		return super.delete(id);
	}

	@Autowired
	private DepartService departService;
	@Autowired
	private StockInService stockInService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
