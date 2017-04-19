package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Storehouse;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.StorehouseService;

@Component
public class StorehouseValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Storehouse.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Storehouse storeshouese = (Storehouse) target;
		final String name = storeshouese.getName();
		final Depart depart = storeshouese.getDepart();
		final String code = storeshouese.getCode();
		final Long id = storeshouese.getId();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId()))
			errors.rejectValue("depart", "storehouse.depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));

		if (!storehouseService.validateNameUnique(name, depart, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("storehouse.name.isUsed", new Object[] { name }));
		if (!storehouseService.validateCodeUnique(code, depart, id))
			errors.rejectValue("code", "code.unique",
					localeMessageSourceService.getMessage("storehouse.code.isUsed", new Object[] { code }));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private StorehouseService storehouseService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
