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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.user.User;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.user.UserService;
import io.jianxun.web.dto.PasswordDto;
import io.jianxun.web.utils.ReturnDto;
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
	private LocaleMessageSourceService localeMessageSourceService;

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
	@GetMapping("create")
	@PreAuthorize("hasAuthority('USERCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("user", new User());
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 修改登录密碼
	 * 
	 * @param model
	 * @param parameters
	 * @return
	 */
	@GetMapping("changepassword/current")
	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
	String changePasswordForm(Model model) {
		model.addAttribute("pwd", new PasswordDto());
		return templatePrefix() + "changepassword";
	}

	@PostMapping("changepassword/current")
	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
	@ResponseBody
	ReturnDto changePasswordSave(PasswordDto password, @RequestParam MultiValueMap<String, String> parameters) {
		userService.changePassword(password);
		return ReturnDto.ok(localeMessageSourceService.getMessage("user.changepassword.success"));
	}

	private String templatePrefix() {
		return "user/";
	}

}
