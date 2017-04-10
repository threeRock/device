package io.jianxun.web.role;

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

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.role.Role;
import io.jianxun.domain.business.user.User;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.role.RoleService;
import io.jianxun.web.utils.ReturnDto;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/", "/page" })
	@PreAuthorize("hasAuthority('ROLELIST')")
	String page(Model model, @QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Page<Role> page = roleService.findActivePage(predicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('ROLECREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("role", new Role());
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param role
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('ROLECREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid Role role, @RequestParam MultiValueMap<String, String> parameters) {
		roleService.save(role);
		return ReturnDto.ok(localeMessageSourceService.getMessage("role.save.success"));
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('ROLEMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		Role role = roleService.findActiveOne(id);
		model.addAttribute("role", role);
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
	@PreAuthorize("hasAuthority('ROLEMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "role") Role role, Model model) {
		roleService.save(role);
		return ReturnDto.ok(localeMessageSourceService.getMessage("role.save.successd"));
	}

	@GetMapping("remove/{id}")
	@PreAuthorize("hasAuthority('ROLEREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		roleService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("role.remove.successd"));
	}

	@ModelAttribute(name = "role")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			Role role = roleService.findOne(id);
			if (role != null)
				model.addAttribute("role", role);
		}
	}

	private String templatePrefix() {
		return "role/";
	}

}
