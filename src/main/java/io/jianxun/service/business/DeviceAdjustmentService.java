package io.jianxun.service.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.ExpressionUtils;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceAdjustment;
import io.jianxun.service.AbstractBaseService;

@Service
@Transactional(readOnly = true)
public class DeviceAdjustmentService extends AbstractBaseService<DeviceAdjustment> {

	public boolean validateNameUnique(Device device, String name, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(DeviceAdjustmentPredicates.devicePredicate(device),
				DeviceAdjustmentPredicates.nameAndIdNotPredicate(name, id)));
	}

}
