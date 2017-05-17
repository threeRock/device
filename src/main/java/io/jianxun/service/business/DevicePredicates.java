package io.jianxun.service.business;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.DeviceStatus;
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
		if (StringUtils.isBlank(depart.getLevelCode()))
			return null;
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

	public static Predicate statusPredicate(DeviceStatus status) {
		QDevice device = QDevice.device;
		if (status != null)
			return device.status.eq(status.getName());
		return device.status.isNull();
	}

	public static Predicate nameContainsPredicate(String name) {
		QDevice device = QDevice.device;
		return device.name.containsIgnoreCase(name);
	}

}
