package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;

public class ValidatorSubTypeTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		SparePartSubType subtype = new SparePartSubType();
		subtype.setName(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePartSubType>> constraintViolations = validator.validate(subtype);

		assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<SparePartSubType> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isIn("mainType", "name");
		assertThat(violation.getMessage()).isIn("备件子类名称不能为空", "所属备件大类不能为空");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		SparePartSubType subtype = new SparePartSubType();
		subtype.setName("tt");
		subtype.setMainType(new SparePartMainType());
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePartSubType>> constraintViolations = validator.validate(subtype);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
