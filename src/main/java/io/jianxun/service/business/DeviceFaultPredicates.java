package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.QDeviceFault;

public class DeviceFaultPredicates {

	public static Predicate devicePredicate(Device device) {
		QDeviceFault deviceFault = QDeviceFault.deviceFault;
		return deviceFault.device.eq(device);
	}

	public static Predicate departPredicate(Depart depart) {
		QDeviceFault deviceFault = QDeviceFault.deviceFault;
		return deviceFault.device.depart.levelCode.startsWithIgnoreCase(depart.getLevelCode());
	}

}
