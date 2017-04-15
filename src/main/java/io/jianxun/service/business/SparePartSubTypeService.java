package io.jianxun.service.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;
import io.jianxun.service.AbstractBaseService;

@Service
public class SparePartSubTypeService extends AbstractBaseService<SparePartSubType> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean validateNameUnique(String name, SparePartMainType mainType, Long id) {
		long count = countActiveAll(SparePartSubTypePredicates.nameAndMainTypeAndIdNotPredicate(name, mainType, id));
		return 0 == count;
	}

}
