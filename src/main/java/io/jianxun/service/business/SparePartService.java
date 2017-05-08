package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.web.dto.DepartTree;

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

	@Autowired
	private DepartService departService;

	@Autowired
	private StockInService stockInService;
	@Autowired
	private StockOutService stockOutService;

}
