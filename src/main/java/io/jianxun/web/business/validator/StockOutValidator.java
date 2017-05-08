package io.jianxun.web.business.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.StockOut;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.StockOutService;

@Component
public class StockOutValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return StockOut.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final StockOut stockout = (StockOut) target;
		final Integer capacity = stockout.getCapacity();
		final Depart depart = stockout.getDepart();

		if (depart == null || depart.getId() == null || !departService.exists(depart.getId()))
			errors.rejectValue("depart", "depart.notfound",
					localeMessageSourceService.getMessage("depart.notfound"));
		if (capacity <= 0)
			errors.rejectValue("capacity", "capacity.incorrect",
					localeMessageSourceService.getMessage("stockout.capacity.incorrect"));
		if (!stockOutService.validateCapacity(stockout))
			errors.rejectValue("capacity", "capacity.notenough",
					localeMessageSourceService.getMessage("stockout.capacity.notenough"));

	}

	@Autowired
	private DepartService departService;
	@Autowired
	private StockOutService stockOutService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}
