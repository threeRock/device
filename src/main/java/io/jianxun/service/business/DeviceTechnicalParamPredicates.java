package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.QDeviceTechnicalParam;

public class DeviceTechnicalParamPredicates {

	public static Predicate devicePredicate(Device device) {
		QDeviceTechnicalParam deviceTechnicalParam = QDeviceTechnicalParam.deviceTechnicalParam;
		return deviceTechnicalParam.device.eq(device);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QDeviceTechnicalParam deviceTechnicalParam = QDeviceTechnicalParam.deviceTechnicalParam;
		if (id == null)
			return deviceTechnicalParam.name.eq(name);
		return deviceTechnicalParam.id.ne(id).and(deviceTechnicalParam.name.eq(name));
	}

	public static Predicate departPredicate(Depart depart) {
		QDeviceTechnicalParam deviceTechnicalParam = QDeviceTechnicalParam.deviceTechnicalParam;
		return deviceTechnicalParam.device.depart.levelCode.startsWithIgnoreCase(depart.getLevelCode());
	}

}
