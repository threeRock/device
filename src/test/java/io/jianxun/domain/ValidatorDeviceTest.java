package io.jianxun.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.ProductionLine;
import io.jianxun.domain.business.SparePartMainType;

public class ValidatorDeviceTest extends AbstractValidator {

	@Test
	public void shouldNotValidateWhenNameBlank() {

		// LocaleContextHolder.setLocale(Locale.ENGLISH);
		Device device = new Device();
		device.setName(" ");
		device.setCode(" ");
		Validator validator = createValidator();
		Set<ConstraintViolation<Device>> constraintViolations = validator.validate(device);

		assertThat(constraintViolations.size()).isEqualTo(5);
		ConstraintViolation<Device> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isIn("depart", "name", "code", "productionLine", "mainType");
	}

	@Test
	public void shouldValidateWhenNameNotEmpty() {
		Device device = new Device();
		device.setDepart(new Depart());
		device.setName("tt");
		device.setCode("tt");
		device.setMainType(new SparePartMainType());
		device.setProductionLine(new ProductionLine());
		Validator validator = createValidator();
		Set<ConstraintViolation<Device>> constraintViolations = validator.validate(device);

		assertThat(constraintViolations.size()).isEqualTo(0);
	}

}
