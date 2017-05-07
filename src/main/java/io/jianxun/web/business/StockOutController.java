package io.jianxun.web.business;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.StockOut;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.StockOutPredicates;
import io.jianxun.service.business.StockOutService;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("stockout")
public class StockOutController {

	@GetMapping(value = "tree")
	@PreAuthorize("hasAuthority('STOCKOUTLIST')")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(stockOutService.getStockOutTree()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new BusinessException(localeMessageSourceService.getMessage("depart.tree.error"));
		}
		return templatePrefix() + "tree";

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page/{depart}" })
	@PreAuthorize("hasAuthority('STOCKOUTLIST')")
	String page(@PathVariable("depart") Long departId, Model model,
			@QuerydslPredicate(root = Depart.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		// 查看仓库查询权限
		if (!currentLoginInfo.validateCurrentUserDepart(depart))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate departPredicate = StockOutPredicates.departPredicate(depart);
		if (predicate != null)
			departPredicate = ExpressionUtils.and(departPredicate, predicate);
		Page<StockOut> page = stockOutService.findActivePage(departPredicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addDepartInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addDepartInfo(Model model, Depart depart) {
		model.addAttribute("depart", depart);
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{departId}")
	@PreAuthorize("hasAuthority('STOCKOUTCREATE')")
	String createForm(@PathVariable("departId") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		model.addAttribute("stockout", new StockOut());
		model.addAttribute("departId", departId);
		model.addAttribute("departmentName", depart.getName());
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param sparePart
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('STOCKOUTCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid StockOut stockOut, @RequestParam MultiValueMap<String, String> parameters) {
		this.stockOutService.save(stockOut);
		return ReturnDto.ok(localeMessageSourceService.getMessage("stockOut.save.successd"), true, "",
				"stockOut-page-layout");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('STOCKOUTMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		StockOut stockout = this.stockOutService.findActiveOne(id);
		model.addAttribute("stockout", stockout);
		model.addAttribute("departId", stockout.getDepart() != null ? stockout.getDepart().getId() : null);
		model.addAttribute("departmentName", stockout.getDepart() != null ? stockout.getDepart().getName() : null);
		model.addAttribute("sparepartinfo", stockout.getSparePart().toString());
		util.addModifyFormAction(model);
		return templatePrefix() + "form";

	}

	/**
	 * 修改角色保存
	 * 
	 * @param entity
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/modify")
	@PreAuthorize("hasAuthority('STOCKOUTMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "stockout") StockOut stockout, Model model) {
		this.stockOutService.save(stockout);
		return ReturnDto.ok(localeMessageSourceService.getMessage("stockout.save.successd"), true, "",
				"stockout-page-layout");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('STOCKOUTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		//TODO 库存判断
		stockOutService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("stockout.remove.successd"));
	}

	@ModelAttribute(name = "stockout")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			StockOut stockout = stockOutService.findActiveOne(id);
			if (stockout != null)
				model.addAttribute("stockout", stockout);
		}
	}

	private String templatePrefix() {
		return "stockout/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StockOutService stockOutService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;

}
