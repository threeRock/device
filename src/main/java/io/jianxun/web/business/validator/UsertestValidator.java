package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depttest;
import io.jianxun.domain.business.Usertest;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepttestService;
import io.jianxun.service.business.UsertestService;

@Component
public class UsertestValidator implements Validator {

	@Autowired
	private UsertestService usertestService;
	@Autowired
	private DepttestService depttestService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Usertest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final Usertest usertest = (Usertest) target;
		final String username = usertest.getUsername();
		final Long id = usertest.getId();
		if (!usertestService.validateUsernameUnique(username, id))
			errors.rejectValue("username", "username.unique",
					localeMessageSourceService.getMessage("user.username.isUsed", new Object[] { username }));
		final Depttest depart = usertest.getDepttest();
		if (depart == null || depart.getId() == null) {
			errors.rejectValue("depttest", "depttest.notfound", localeMessageSourceService.getMessage("depttest.notfound"));
			return;
		}
		Depttest d = depttestService.findActiveOne(depart.getId());
		if (d == null)
			errors.rejectValue("depttest", "depttest.notfound", localeMessageSourceService.getMessage("depttest.notfound"));
	}

}
