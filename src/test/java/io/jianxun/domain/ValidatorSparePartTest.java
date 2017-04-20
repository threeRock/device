package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.domain.business.Storehouse;

public class ValidatorSparePartTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		SparePart sparePart = new SparePart();
		sparePart.setName(" ");
		sparePart.setCode(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePart>> constraintViolations = validator.validate(sparePart);

		assertThat(constraintViolations.size()).isEqualTo(4);
		ConstraintViolation<SparePart> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isIn("depart", "name", "code", "storehouse");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		SparePart sparePart = new SparePart();
		sparePart.setDepart(new Depart());
		sparePart.setName("tt");
		sparePart.setCode("tt");
		sparePart.setStorehouse(new Storehouse());
		Validator validator = createValidator();
		Set<ConstraintViolation<SparePart>> constraintViolations = validator.validate(sparePart);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
