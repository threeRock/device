package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.StockIn;
import io.jianxun.service.AbstractBaseService;
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

	@Autowired
	private DepartService departService;

}
