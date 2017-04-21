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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import io.jianxun.domain.business.ProductionLine;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.ProductionLinePredicates;
import io.jianxun.service.business.ProductionLineService;
import io.jianxun.web.business.validator.ProductionLineValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/productionline")
public class ProductionLineController {

	@InitBinder("productionLine")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(productionlineValidator);

	}

	@GetMapping(value = "tree")
	@PreAuthorize("hasAuthority('PRODUCTIONLINELIST')")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(productionlineService.getProductionLineDepartTree()));
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
	@PreAuthorize("hasAuthority('PRODUCTIONLINELIST')")
	String page(@PathVariable("depart") Long departId, Model model,
			@QuerydslPredicate(root = ProductionLine.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		//TODO 是否有查看仓库权限
		Predicate productionlinePredicate = ProductionLinePredicates.departPredicate(depart);
		if (predicate != null)
			productionlinePredicate = ExpressionUtils.and(productionlinePredicate, predicate);
		Page<ProductionLine> page = productionlineService.findActivePage(productionlinePredicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addParentProductionLineInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addParentProductionLineInfo(Model model, Depart depart) {
		model.addAttribute("depart", depart);
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{departId}")
	@PreAuthorize("hasAuthority('PRODUCTIONLINECREATE')")
	String createForm(@PathVariable("departId") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("productionLine", new ProductionLine());
		model.addAttribute("departId", departId);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param productionline
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('PRODUCTIONLINECREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid ProductionLine productionline, @RequestParam MultiValueMap<String, String> parameters) {
		productionlineService.save(productionline);
		return ReturnDto.ok(localeMessageSourceService.getMessage("productionline.save.successd"), true, "",
				"productionline-page-layout");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('PRODUCTIONLINEMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		ProductionLine productionline = productionlineService.findActiveOne(id);
		model.addAttribute("productionLine", productionline);
		model.addAttribute("departId", productionline.getDepart() != null ? productionline.getDepart().getId() : null);
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
	@PreAuthorize("hasAuthority('PRODUCTIONLINEMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "productionLine") ProductionLine productionline, Model model) {
		productionlineService.save(productionline);
		return ReturnDto.ok(localeMessageSourceService.getMessage("productionline.save.successd"), true, "",
				"productionline-page-layout");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('PRODUCTIONLINEREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		productionlineService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("productionline.remove.successd"));
	}

	/**
	 * 验证机构名称是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("check/nameunique")
	@ResponseBody
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("depart.id") Long departId,
			@RequestParam("id") Long id) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!this.productionlineService.validateNameUnique(name, depart, id))
			return localeMessageSourceService.getMessage("productionline.name.isUsed", new Object[] { name });
		return "";
	}

	@ModelAttribute(name = "productionLine")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			ProductionLine productionline = productionlineService.findActiveOne(id);
			if (productionline != null)
				model.addAttribute("productionLine", productionline);
		}
	}

	private String templatePrefix() {
		return "productionline/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ProductionLineService productionlineService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;

	@Autowired
	private ProductionLineValidator productionlineValidator;

}
