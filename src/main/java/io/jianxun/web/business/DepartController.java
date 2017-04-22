package io.jianxun.web.business;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartPredicates;
import io.jianxun.service.business.DepartService;
import io.jianxun.web.business.validator.DepartValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("depart")
public class DepartController {

	@InitBinder("depart")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(departValidator);

	}

	@GetMapping(value = "tree")
	@PreAuthorize("hasAuthority('DEPARTLIST')")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(departService.getDepartTree()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new BusinessException(localeMessageSourceService.getMessage("depart.tree.error"));
		}
		return templatePrefix() + "tree";

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page/{parent}" })
	@PreAuthorize("hasAuthority('DEPARTLIST')")
	String page(@PathVariable("parent") Long departId, Model model,
			@QuerydslPredicate(root = Depart.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart parent = this.departService.findActiveOne(departId);
		if (parent == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		// 查看仓库查询权限
		if (!currentLoginInfo.validateCurrentUserDepart(parent))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate departPredicate = DepartPredicates.parentPredicate(parent);
		if (predicate != null)
			departPredicate = ExpressionUtils.and(departPredicate, predicate);
		Page<Depart> page = departService.findActivePage(departPredicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addParentDepartInfo(model, parent);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addParentDepartInfo(Model model, Depart parent) {
		model.addAttribute("parent", parent);
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{parentId}")
	@PreAuthorize("hasAuthority('DEPARTCREATE')")
	String createForm(@PathVariable("parentId") Long parentId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("depart", new Depart());
		model.addAttribute("parentId", parentId);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param depart
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('DEPARTCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid Depart depart, @RequestParam MultiValueMap<String, String> parameters) {
		Long parentId = depart.getParent().getId();
		Depart parent = departService.findActiveOne(parentId);
		StringBuilder levelCodeSB = new StringBuilder();
		if (parent != null) {
			if (StringUtils.isNotBlank(parent.getLevelCode()))
				levelCodeSB.append(parent.getLevelCode());
			levelCodeSB.append(parent.getId() + Depart.LEVEL_CODE_SEPARATOR);
			depart.setLevelCode(levelCodeSB.toString());
		}
		departService.save(depart);
		return ReturnDto.ok(localeMessageSourceService.getMessage("depart.save.success"), true, "depart-page",
				"depart-page-layout");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('DEPARTMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		Depart depart = departService.findActiveOne(id);
		model.addAttribute("depart", depart);
		model.addAttribute("parentId", depart.getParent() != null ? depart.getParent().getId() : null);
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
	@PreAuthorize("hasAuthority('DEPARTMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "depart") Depart depart, Model model) {
		departService.save(depart);
		return ReturnDto.ok(localeMessageSourceService.getMessage("depart.save.success"), true, "depart-page",
				"depart-page-layout");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('DEPARTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		departService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("depart.remove.successd"));
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
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("id") Long id) {
		if (!this.departService.validateNameUnique(name, id))
			return localeMessageSourceService.getMessage("depart.name.isUsed", new Object[] { name });
		return "";
	}

	@ModelAttribute(name = "depart")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			Depart depart = departService.findOne(id);
			if (depart != null)
				model.addAttribute("depart", depart);
		}
	}

	private String templatePrefix() {
		return "depart/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;

	@Autowired
	private DepartValidator departValidator;

}
