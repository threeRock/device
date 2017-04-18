package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Storehouse;

public class ValidatorStorehouseTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		Storehouse storehouse = new Storehouse();
		storehouse.setName(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<Storehouse>> constraintViolations = validator.validate(storehouse);

		assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<Storehouse> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isIn("depart", "name");
		assertThat(violation.getMessage()).contains("仓库名称不能为空");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		Storehouse storehouse = new Storehouse();
		storehouse.setDepart(new Depart());
		storehouse.setName("tt");
		Validator validator = createValidator();
		Set<ConstraintViolation<Storehouse>> constraintViolations = validator.validate(storehouse);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
