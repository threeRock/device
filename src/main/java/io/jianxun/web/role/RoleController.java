package io.jianxun.web.role;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.role.Role;
import io.jianxun.domain.business.user.User;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.role.RoleService;
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
	
	@ModelAttribute(name = "role")
	private void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
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
