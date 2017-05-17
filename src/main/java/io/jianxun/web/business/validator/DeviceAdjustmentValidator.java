package io.jianxun.web.business.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceAdjustment;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DeviceAdjustmentService;

@Component
public class DeviceAdjustmentValidator implements Validator {

	@Autowired
	private DeviceAdjustmentService deviceAdjustmentService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return DeviceAdjustment.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final DeviceAdjustment daj = (DeviceAdjustment) target;
		final Device device = daj.getDevice();
		final String name = daj.getName();
		final String content = daj.getContent();
		final Long id = daj.getId();
		if (device == null)
			errors.rejectValue("device", "device.notnull",
					localeMessageSourceService.getMessage("device.adjustment.device.notnull"));
		if (StringUtils.isBlank(name))
			errors.rejectValue("name", "name.notblank",
					localeMessageSourceService.getMessage("device.adjustment.name.notblank"));
		if (StringUtils.isBlank(content))
			errors.rejectValue("content", "content.notblank",
					localeMessageSourceService.getMessage("device.adjustment.content.notblank"));
		if (!deviceAdjustmentService.validateNameUnique(device, name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("device.adjustment.name.isUsed", new Object[] { name }));

	}

}
