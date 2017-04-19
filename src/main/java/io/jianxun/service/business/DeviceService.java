package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.web.dto.DepartTree;

@Service
public class DeviceService extends AbstractBaseService<Device> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean validateNameUnique(String name, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(DevicePredicates.departPredicate(depart),
				DevicePredicates.nameAndIdNotPredicate(name, id)));
	}

	public boolean validateCodeUnique(String code, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(DevicePredicates.departPredicate(depart),
				DevicePredicates.codeAndIdNotPredicate(code, id)));
	}

	public List<DepartTree> getDeviceTree() {
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
			d.setUrl("device/page");
			d.setDivid("#device-page-layout");
			tree.add(d);
		}
		return tree;
	}

	@Autowired
	private DepartService departService;

}
