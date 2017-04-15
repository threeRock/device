package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartSubTypeService;

@Component
public class SparePartSubTypeValidator implements Validator {

	@Autowired
	private SparePartSubTypeService subTypeService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return SparePartSubType.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final SparePartSubType subType = (SparePartSubType) target;
		final String name = subType.getName();
		final SparePartMainType mainType = subType.getMainType();
		final Long id = subType.getId();
		if (!subTypeService.validateNameUnique(name, mainType, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("maintype.name.isUsed", new Object[] { name }));

	}

}
