package io.jianxun.service.business;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.QDevice;

/**
 * 仓库查询
 * 
 * @author Administrator
 *
 */
public class DevicePredicates {

	private DevicePredicates() {
	}

	// 仓库名查询
	public static Predicate namePredicate(String name) {
		return nameAndIdNotPredicate(name, null);
	}

	public static Predicate nameAndIdNotPredicate(String name, Long id) {
		QDevice device = QDevice.device;
		if (id == null)
			return device.name.eq(name);
		return device.id.ne(id).and(device.name.eq(name));
	}

	public static Predicate departSubPredicate(Depart depart) {
		QDevice device = QDevice.device;
		return device.depart.levelCode.startsWith(depart.getLevelCode());
	}

	public static Predicate codeAndIdNotPredicate(String code, Long id) {
		QDevice device = QDevice.device;
		if (id == null)
			return device.code.eq(code);
		return device.id.ne(id).and(device.code.eq(code));
	}

	public static Predicate departPredicate(Depart depart) {
		QDevice device = QDevice.device;
		return device.depart.eq(depart);
	}

}
