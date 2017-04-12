package io.jianxun.web.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.User;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final User user = (User) target;
		final String username = user.getUsername();
		final Long id = user.getId();
		if (!userService.validateUsernameUnique(username, id))
			errors.rejectValue("username", "username.unique",
					localeMessageSourceService.getMessage("user.username.isUsed", new Object[] { username }));

	}

}
