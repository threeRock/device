package io.jianxun.web.user;

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

import io.jianxun.domain.business.user.User;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.role.RoleService;
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

	@InitBinder("user")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(userValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/", "/page" })
	@PreAuthorize("hasAuthority('USERLIST')")
	String page(Model model, @QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Page<User> page = userService.findActivePage(predicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('USERCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("user", new User());
		addRoleList(model);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('USERCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid User user, @RequestParam MultiValueMap<String, String> parameters) {
		userService.register(user);
		return getOptionReturn("user.save.successd");
	}

	/**
	 * 修改登录密碼表单
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("resetpassword/current")
	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
	String changePasswordForm(Model model) {
		model.addAttribute("pwd", new PasswordDto());
		return templatePrefix() + "resetpassword";
	}

	/**
	 * 修改登录密码保存
	 * 
	 * @param password
	 * @param parameters
	 * @return
	 */
	@PostMapping("resetpassword/current")
	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
	@ResponseBody
	ReturnDto changePasswordSave(@Valid PasswordDto password, @RequestParam MultiValueMap<String, String> parameters) {
		userService.resetPassword(password);
		return ReturnDto.ok(localeMessageSourceService.getMessage("user.resetpassword.successed"));
	}

	/**
	 * 修改用户表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('USERMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		User user = userService.findActiveOne(id);
		model.addAttribute("user", user);
		addRoleList(model);
		util.addModifyFormAction(model);
		return templatePrefix() + "form";

	}

	/**
	 * 修改用户保存
	 * 
	 * @param entity
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/modify")
	@PreAuthorize("hasAuthority('USERMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "user") User entity, Model model) {
		userService.save(entity);
		return ReturnDto.ok(localeMessageSourceService.getMessage("user.save.successd"));
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/remove/{id}")
	@PreAuthorize("hasAuthority('USERREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		userService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("user.remove.successd"));
	}

	/**
	 * 验证用户名称是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("check/usernameunique")
	@ResponseBody
	public String checkUsernameIsUnique(@RequestParam("username") String username, @RequestParam("id") Long id) {
		if (!this.userService.validateUsernameUnique(username, id))
			return localeMessageSourceService.getMessage("user.username.isUsed", new Object[] { username });
		return "";
	}

	private void addRoleList(Model model) {
		model.addAttribute("roleList", roleService.findActiveAll(new Sort("name")));
	}

	@ModelAttribute(name = "user")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			User user = userService.findOne(id);
			if (user != null)
				model.addAttribute("user", user);
		}
	}

	private ReturnDto getOptionReturn(String messagekey) {
		return ReturnDto.ok(localeMessageSourceService.getMessage(messagekey), true, "user-page");
	}

	private String templatePrefix() {
		return "user/";
	}

	@Autowired
	private UserService userService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserValidator userValidator;

}
