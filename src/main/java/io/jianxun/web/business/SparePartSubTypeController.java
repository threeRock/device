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

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.service.business.SparePartSubTypeService;
import io.jianxun.web.business.validator.SparePartSubTypeValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/subtype")
public class SparePartSubTypeController {

	@InitBinder("subtype")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(subtypeValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "", "/page" })
	@PreAuthorize("hasAuthority('SUBTYPELIST')")
	String page(Model model, @QuerydslPredicate(root = SparePartSubType.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Page<SparePartSubType> page = subtypeService.findActivePage(predicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addMaitypes(model);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('SUBTYPECREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("subtype", new SparePartSubType());
		util.addCreateFormAction(model);
		addMaitypes(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	private void addMaitypes(Model model) {
		model.addAttribute("maintypes", mainTypeService.findActiveAll(new Sort("name")));
	}

	/**
	 * 新增保存
	 * 
	 * @param subtype
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('SUBTYPECREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid @ModelAttribute(name = "subtype") SparePartSubType subtype,
			@RequestParam MultiValueMap<String, String> parameters) {
		subtypeService.save(subtype);
		return ReturnDto.ok(localeMessageSourceService.getMessage("subtype.save.success"), true, "subtype-page");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('SUBTYPEMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		SparePartSubType subtype = subtypeService.findActiveOne(id);
		model.addAttribute("subtype", subtype);
		util.addModifyFormAction(model);
		addMaitypes(model);
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
	@PreAuthorize("hasAuthority('SUBTYPEMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "subtype") SparePartSubType subtype, Model model) {
		subtypeService.save(subtype);
		return ReturnDto.ok(localeMessageSourceService.getMessage("subtype.save.successd"), true, "subtype-page");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('SUBTYPEREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		subtypeService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("subtype.remove.successd"), false);
	}

	/**
	 * 验证名称是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("check/nameunique")
	@ResponseBody
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("mainType") Long maintypeId,
			@RequestParam("id") Long id) {
		SparePartMainType mainType = mainTypeService.findActiveOne(maintypeId);
		if (mainType == null)
			return localeMessageSourceService.getMessage("subtype.maintype,isnull");
		if (!this.subtypeService.validateNameUnique(name, mainType, id))
			return localeMessageSourceService.getMessage("subtype.name.isUsed",
					new Object[] { mainType.getName(), name });
		return "";
	}

	@ModelAttribute(name = "subtype")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			SparePartSubType subtype = subtypeService.findOne(id);
			if (subtype != null)
				model.addAttribute("subtype", subtype);
		}
	}

	private String templatePrefix() {
		return "subtype/";
	}

	@Autowired
	private SparePartSubTypeService subtypeService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;

	@Autowired
	private SparePartSubTypeValidator subtypeValidator;

	@Autowired
	private SparePartMainTypeService mainTypeService;

}
