package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.ProductionLine;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.ProductionLineService;

@Component
public class ProductionLineValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ProductionLine.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final ProductionLine productionLine = (ProductionLine) target;
		final String name = productionLine.getName();
		final Depart depart = productionLine.getDepart();
		final Long id = productionLine.getId();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId())) {
			errors.rejectValue("depart", "productionLine.depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));
			return;
		}

		if (!productionLineService.validateNameUnique(name, depart, id))
			errors.rejectValue("name", "name.unique",
					localeMessageSourceService.getMessage("productionline.name.isUsed", new Object[] { name }));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private ProductionLineService productionLineService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
