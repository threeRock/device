package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depttest;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepttestService;

@Component
public class DepttestValidator implements Validator {

	@Autowired
	private DepttestService depttestService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Depttest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Depttest depart = (Depttest) target;
		final String name = depart.getName();
		final Long id = depart.getId();
		if (!depttestService.validateNameUnique(name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("depttest.name.isUsed", new Object[] { name }));
		if (depart.getParent() == null || depart.getParent().getId() == null)
			errors.rejectValue("parent", "depttest.parent.notfound", localeMessageSourceService.getMessage("depttest.notfound"));
		final Depttest parent = depttestService.findActiveOne(depart.getParent().getId());

		if (parent == null || !depttestService.exists(parent.getId()))
			errors.rejectValue("parent", "depttest.parent.notfound", localeMessageSourceService.getMessage("depttest.notfound"));

	}

}
