package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import io.jianxun.domain.business.user.User;

public class ValidatorUserTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void shouldNotValidateWhenUserNameEmpty() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		User sysUser = new User();
		sysUser.setUsername(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(sysUser);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<User> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
		assertThat(violation.getMessage()).isEqualTo("用户名不能为空");
	}

	@Test
	public void shouldValidateWhenUserNameNotEmpty() {
		User sysUser = new User();
		sysUser.setUsername("tt");
		Validator validator = createValidator();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(sysUser);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
