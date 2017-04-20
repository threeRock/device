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
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.web.business.validator.SparePartMainTypeValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/maintype")
public class SparePartMainTypeController {

	@InitBinder("maintype")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(maintypeValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "", "/page" })
	@PreAuthorize("hasAuthority('MAINTYPELIST')")
	String page(Model model, @QuerydslPredicate(root = SparePartMainType.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Page<SparePartMainType> page = maintypeService.findActivePage(predicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('MAINTYPECREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("maintype", new SparePartMainType());
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param maintype
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('MAINTYPECREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid @ModelAttribute(name = "maintype") SparePartMainType maintype,
			@RequestParam MultiValueMap<String, String> parameters) {
		maintypeService.save(maintype);
		return ReturnDto.ok(localeMessageSourceService.getMessage("maintype.save.success"), true, "maintype-page");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('MAINTYPEMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		SparePartMainType maintype = maintypeService.findActiveOne(id);
		model.addAttribute("maintype", maintype);
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
	@PreAuthorize("hasAuthority('MAINTYPEMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "maintype") SparePartMainType maintype, Model model) {
		maintypeService.save(maintype);
		return ReturnDto.ok(localeMessageSourceService.getMessage("maintype.save.successd"), true, "maintype-page");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('MAINTYPEREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		maintypeService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("maintype.remove.successd"), false);
	}

	/**
	 * 验证用户名称是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("check/nameunique")
	@ResponseBody
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("id") Long id) {
		if (!this.maintypeService.validateNameUnique(name, id))
			return localeMessageSourceService.getMessage("maintype.name.isUsed", new Object[] { name });
		return "";
	}

	@ModelAttribute(name = "maintype")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			SparePartMainType maintype = maintypeService.findOne(id);
			if (maintype != null)
				model.addAttribute("maintype", maintype);
		}
	}

	private String templatePrefix() {
		return "maintype/";
	}

	@Autowired
	private SparePartMainTypeService maintypeService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;

	@Autowired
	private SparePartMainTypeValidator maintypeValidator;

}
