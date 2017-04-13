package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import io.jianxun.domain.business.Depart;

public class ValidatorDepartTest {

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
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		Depart depart = new Depart();
		depart.setName(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<Depart>> constraintViolations = validator.validate(depart);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Depart> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("机构名称不能为空");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		Depart depart = new Depart();
		depart.setName("tt");
		Validator validator = createValidator();
		Set<ConstraintViolation<Depart>> constraintViolations = validator.validate(depart);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
