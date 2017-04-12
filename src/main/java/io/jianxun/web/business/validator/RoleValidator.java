package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Role;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.RoleService;

@Component
public class RoleValidator implements Validator {

	@Autowired
	private RoleService roleService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Role.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Role role = (Role) target;
		final String name = role.getName();
		final Long id = role.getId();
		if (!roleService.validateNameUnique(name, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("role.name.isUsed", new Object[] { name }));

	}

}
