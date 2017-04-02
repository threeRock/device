package io.jianxun.web.user;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.user.User;
import io.jianxun.service.user.UserService;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("user")
public class UserController {

	public UserController() {
		super();
	}

	@Autowired
	private UserService userService;

	@Autowired
	private Utils util;

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/", "/page" })
	@PreAuthorize("hasAuthority('USERLIST')")
	String page(Model model, @QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Page<User> page = userService.findActivePage(predicate, pageable);
		model.addAllAttributes(util.getPageMap(parameters, page));
		model.addAllAttributes(util.getSearchMap(parameters));
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("user", new User());
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	private String templatePrefix() {
		return "user/";
	}

}
