package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.domain.business.StockIn;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.web.dto.DepartTree;

@Service
public class StockInService extends AbstractBaseService<StockIn> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<DepartTree> getStockInTree() {
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
			d.setUrl("stockin/page");
			d.setDivid("#stockin-page-layout");
			tree.add(d);
		}
		return tree;
	}

	public Integer getSparePartCapacity(Depart depart, SparePart sparePart, Long id) {
		return this.findActiveAll(StockInPredicates.sparePartPredicate(depart, sparePart, id), new Sort("id")).stream()
				.mapToInt(stockOut -> stockOut.getCapacity()).sum();
	}

	public boolean validateCapacity(StockIn stockin) {
		Integer outCount = stockOutService.getSparePartCapacity(stockin.getDepart(), stockin.getSparepart(), null);
		Integer inCount = getSparePartCapacity(stockin.getDepart(), stockin.getSparepart(), stockin.getId());
		return stockin.getCapacity() + inCount >= outCount;
	}

	public boolean validateDeleteCapacity(StockIn stockin) {
		Integer outCount = stockOutService.getSparePartCapacity(stockin.getDepart(), stockin.getSparepart(), null);
		Integer inCount = getSparePartCapacity(stockin.getDepart(), stockin.getSparepart(), stockin.getId());
		return inCount >= outCount;
	}

	@Override
	public StockIn delete(Long id) {
		StockIn in = findActiveOne(id);
		if (in == null)
			throw new BusinessException(localeMessageSourceService.getMessage("stockin.notfound"));
		if (!validateDeleteCapacity(in))
			throw new BusinessException(localeMessageSourceService.getMessage("stockin.capacity.notenough"));
		return super.delete(id);
	}

	@Autowired
	private DepartService departService;
	@Autowired
	private StockOutService stockOutService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
