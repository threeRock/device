package io.jianxun.domain;

import javax.validation.Validator;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class AbstractValidator {

	protected Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

}
