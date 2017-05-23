package io.jianxun.web.business.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceService;

@Component
public class DeviceValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Device.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Device device = (Device) target;
		final String name = device.getName();
		final Depart depart = device.getDepart();
		final String code = device.getCode();
		final Long id = device.getId();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId()))
			errors.rejectValue("depart", "depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));
		if (device.getMainType() == null || device.getMainType().getId() == null)
			errors.rejectValue("mainType", "mainType.notnull",
					localeMessageSourceService.getMessage("device.mainType.notnull"));
		if (device.getProductionLine() == null || device.getProductionLine().getId() == null)
			errors.rejectValue("productionLine", "productionLine.notnull",
					localeMessageSourceService.getMessage("device.productionLine.notnull"));
		if(StringUtils.isBlank(name))
			errors.rejectValue("name", "name.notblank",
					localeMessageSourceService.getMessage("device.name.notblank"));
		if(StringUtils.isBlank(code))
			errors.rejectValue("code", "code.notblank",
					localeMessageSourceService.getMessage("device.code.notblank"));
		if (!deviceService.validateNameCodeUnique(name, code, depart, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("device.name.code.isUsed", new Object[] { name,code }));
		//注释修改为同时判断名称及编码
//		if (!deviceService.validateNameUnique(name, depart, id))
//			errors.rejectValue("name", "name.unique",
//					localeMessageSourceService.getMessage("device.name.isUsed", new Object[] { name }));
//		if (!deviceService.validateCodeUnique(code, depart, id))
//			errors.rejectValue("code", "code.unique",
//					localeMessageSourceService.getMessage("device.code.isUsed", new Object[] { code }));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
