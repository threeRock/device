package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;

@Component
public class DepartValidator implements Validator {

	@Autowired
	private DepartService departService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Depart.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Depart depart = (Depart) target;
		final String name = depart.getName();
		final Long id = depart.getId();
		if (!departService.validateNameUnique(name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("depart.name.isUsed", new Object[] { name }));
		if (depart.getParent() == null || depart.getParent().getId() == null)
			errors.rejectValue("parent", "depart.parent.notfound", localeMessageSourceService.getMessage("depart.notfound"));
		final Depart parent = departService.findActiveOne(depart.getParent().getId());

		if (parent == null || !departService.exists(parent.getId()))
			errors.rejectValue("parent", "depart.parent.notfound", localeMessageSourceService.getMessage("depart.notfound"));

	}

}
