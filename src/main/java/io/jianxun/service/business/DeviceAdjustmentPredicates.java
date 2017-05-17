package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.QDeviceAdjustment;

public class DeviceAdjustmentPredicates {

	public static Predicate devicePredicate(Device device) {
		QDeviceAdjustment deviceAdjustment = QDeviceAdjustment.deviceAdjustment;
		return deviceAdjustment.device.eq(device);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QDeviceAdjustment deviceAdjustment = QDeviceAdjustment.deviceAdjustment;
		if (id == null)
			return deviceAdjustment.name.eq(name);
		return deviceAdjustment.id.ne(id).and(deviceAdjustment.name.eq(name));
	}

	public static Predicate departPredicate(Depart depart) {
		QDeviceAdjustment deviceAdjustment = QDeviceAdjustment.deviceAdjustment;
		return deviceAdjustment.device.depart.levelCode.startsWithIgnoreCase(depart.getLevelCode());
	}

}
