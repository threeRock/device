package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.QDeviceCheckInfo;

public class DeviceCheckInfoPredicates {

	public static Predicate devicePredicate(Device device) {
		QDeviceCheckInfo deviceCheckInfo = QDeviceCheckInfo.deviceCheckInfo;
		return deviceCheckInfo.device.eq(device);
	}

	public static Predicate departPredicate(Depart depart) {
		QDeviceCheckInfo deviceCheckInfo = QDeviceCheckInfo.deviceCheckInfo;
		return deviceCheckInfo.device.depart.levelCode.startsWithIgnoreCase(depart.getLevelCode());
	}

}
