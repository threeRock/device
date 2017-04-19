package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Storehouse;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.web.dto.DepartTree;

@Service
public class StorehouseService extends AbstractBaseService<Storehouse> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean validateNameUnique(String name, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(StorehousePredicates.departPredicate(depart),
				StorehousePredicates.nameAndIdNotPredicate(name, id)));
	}
	
	public boolean validateCodeUnique(String code, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(StorehousePredicates.departPredicate(depart),
				StorehousePredicates.codeAndIdNotPredicate(code, id)));
	}

	public List<DepartTree> getStorehouseTree() {
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
			d.setUrl("device/storehouse/page");
			d.setDivid("#storehouse-page-layout");
			tree.add(d);
		}
		return tree;
	}

	@Autowired
	private DepartService departService;

}
