package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.SparePartService;

@Component
public class SparePartValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return SparePart.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final SparePart sparePart = (SparePart) target;
		final String name = sparePart.getName();
		final Depart depart = sparePart.getDepart();
		final Long id = sparePart.getId();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId()))
			errors.rejectValue("sparePart", "sparePart.depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));
		if (sparePart.getStorehouse() == null || sparePart.getStorehouse().getId() == null)
			errors.rejectValue("sparePart", "sparePart.storehouse.notnull",
					localeMessageSourceService.getMessage("sparePart.storehouse.notnull"));
		if (!sparePartService.validateNameUnique(name, depart, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("sparePart.name.isUsed", new Object[] { name }));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private SparePartService sparePartService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
