package io.jianxun.web.business.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceCheckInfo;
import io.jianxun.service.LocaleMessageSourceService;

@Component
public class DeviceCheckInfoValidator implements Validator {


	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return DeviceCheckInfo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final DeviceCheckInfo daj = (DeviceCheckInfo) target;
		final Device device = daj.getDevice();
		final String content = daj.getContent();
		if (device == null)
			errors.rejectValue("device", "device.notnull",
					localeMessageSourceService.getMessage("device.checkinfo.device.notnull"));
		if (StringUtils.isBlank(content))
			errors.rejectValue("content", "content.notblank",
					localeMessageSourceService.getMessage("device.checkinfo.content.notblank"));

	}

}
