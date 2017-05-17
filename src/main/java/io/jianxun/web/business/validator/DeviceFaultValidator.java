package io.jianxun.web.business.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceFault;
import io.jianxun.service.LocaleMessageSourceService;

@Component
public class DeviceFaultValidator implements Validator {


	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return DeviceFault.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final DeviceFault daj = (DeviceFault) target;
		final Device device = daj.getDevice();
		final String reason = daj.getReason();
		if (device == null)
			errors.rejectValue("device", "device.notnull",
					localeMessageSourceService.getMessage("device.fault.device.notnull"));
		if (StringUtils.isBlank(reason))
			errors.rejectValue("reason", "reason.notblank",
					localeMessageSourceService.getMessage("device.fault.reason.notblank"));

	}

}
