package io.jianxun.web.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import io.jianxun.domain.business.Depart;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.ProductionLinePredicates;
import io.jianxun.service.business.ProductionLineService;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.service.business.StorehousePredicates;
import io.jianxun.service.business.StorehouseService;
import io.jianxun.web.utils.CurrentLoginInfo;

@Controller
public class MainController {

	@RequestMapping("main")
	public String main() {
		return "main";
	}

	@RequestMapping("home")
	public String home(Model model) {
		Depart depart = this.currentLoginInfo.currentLoginUser().getDepart();
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (depart.isRoot())
			model.addAttribute("lines", productionLineService.findActiveAll(new Sort("name")));
		else
			model.addAttribute("lines", productionLineService
					.findActiveAll(ProductionLinePredicates.departPredicate(depart), new Sort("name")));
		model.addAttribute("types", sparePartMainTypeService.findActiveAll(new Sort("name")));
		if (depart.isRoot())
			model.addAttribute("storehouses", storehouseService.findActiveAll(new Sort("name")));
		else
			model.addAttribute("storehouses",
					storehouseService.findActiveAll(StorehousePredicates.departPredicate(depart), new Sort("name")));
		return "home";
	}

	@Autowired
	private ProductionLineService productionLineService;
	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;
	@Autowired
	private StorehouseService storehouseService;

}
