package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.StockIn;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.StockInService;

@Component
public class StockInValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return StockIn.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final StockIn stockin = (StockIn) target;
		final Integer capacity = stockin.getCapacity();
		final Depart depart = stockin.getDepart();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId()))
			errors.rejectValue("depart", "depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));
		if (capacity <= 0)
			errors.rejectValue("capacity", "capacity.incorrect",
					localeMessageSourceService.getMessage("stockin.capacity.incorrect"));
		if (!stockInService.validateCapacity(stockin))
			errors.rejectValue("capacity", "capacity.notenough",
					localeMessageSourceService.getMessage("stockin.capacity.notenough"));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private StockInService stockInService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
