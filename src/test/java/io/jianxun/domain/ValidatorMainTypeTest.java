package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.SparePartMainType;

public class ValidatorMainTypeTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		SparePartMainType maintype = new SparePartMainType();
		maintype.setName(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePartMainType>> constraintViolations = validator.validate(maintype);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<SparePartMainType> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("备件大类名称不能为空");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		SparePartMainType maintype = new SparePartMainType();
		maintype.setName("tt");
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePartMainType>> constraintViolations = validator.validate(maintype);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
