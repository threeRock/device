package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.ProductionLine;

public class ValidatorProductionLineTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		ProductionLine productionLine = new ProductionLine();
		productionLine.setName(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<ProductionLine>> constraintViolations = validator.validate(productionLine);

		assertThat(constraintViolations.size()).isEqualTo(2);
		ConstraintViolation<ProductionLine> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isIn("depart", "name");
		assertThat(violation.getMessage()).isIn("所属机构不能为空","生产线名称不能为空");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		ProductionLine productionLine = new ProductionLine();
		productionLine.setDepart(new Depart());
		productionLine.setName("tt");
		Validator validator = createValidator();
		Set<ConstraintViolation<ProductionLine>> constraintViolations = validator.validate(productionLine);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
