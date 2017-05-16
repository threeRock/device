package io.jianxun.web.business.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceTechnicalParam;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DeviceTechnicalParamService;

@Component
public class DeviceTechnicalParamValidator implements Validator {

	@Autowired
	private DeviceTechnicalParamService deviceTechnicalParamService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return DeviceTechnicalParam.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final DeviceTechnicalParam dtp = (DeviceTechnicalParam) target;
		final Device device = dtp.getDevice();
		final String name = dtp.getName();
		final String params = dtp.getParams();
		final Long id = dtp.getId();
		if (device == null)
			errors.rejectValue("device", "device.notnull",
					localeMessageSourceService.getMessage("device.technical.param.device.notnull"));
		if (StringUtils.isBlank(name))
			errors.rejectValue("name", "name.notblank",
					localeMessageSourceService.getMessage("device.technical.param.name.notblank"));
		if (StringUtils.isBlank(params))
			errors.rejectValue("params", "params.notblank",
					localeMessageSourceService.getMessage("device.technical.param.params.notblank"));
		if (!deviceTechnicalParamService.validateNameUnique(device, name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("device.technical.param.name.isUsed", new Object[] { name }));

	}

}
