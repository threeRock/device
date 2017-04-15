package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypeService;

@Component
public class SparePartMainTypeValidator implements Validator {

	@Autowired
	private SparePartMainTypeService mainTypeService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return SparePartMainType.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final SparePartMainType mainType = (SparePartMainType) target;
		final String name = mainType.getName();
		final Long id = mainType.getId();
		if (!mainTypeService.validateNameUnique(name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("maintype.name.isUsed", new Object[] { name }));

	}

}
