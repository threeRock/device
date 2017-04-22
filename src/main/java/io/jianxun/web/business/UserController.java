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
import io.jianxun.domain.business.User;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.RoleService;
import io.jianxun.service.business.UserPredicates;
import io.jianxun.service.business.UserService;
import io.jianxun.web.business.validator.UserValidator;
import io.jianxun.web.dto.ChangePasswordDto;
import io.jianxun.web.dto.ResetPasswordDto;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
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

	@GetMapping(value = "tree")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(departService.getUserDepartTree()));
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
	@PreAuthorize("hasAuthority('USERLIST')")
	String page(@PathVariable("depart") long departId, Model model,
			@QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));

		if (!currentLoginInfo.validateCurrentUserDepart(depart))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate searchPredicate = null;
		Page<User> page = null;
		if (predicate == null && depart.isRoot()) {
			page = userService.findActivePage(pageable);
		} else {
			searchPredicate = ExpressionUtils.and(UserPredicates.departSubPredicate(depart), predicate);
			page = userService.findActivePage(searchPredicate, pageable);
		}

		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		model.addAttribute("departId", departId);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{depart}")
	@PreAuthorize("hasAuthority('USERCREATE')")
	String createForm(@PathVariable("depart") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("departId", departId);
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
	 * 重置登录密碼表单
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("resetpassword/current")
	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
	String resetPasswordForm(Model model) {
		model.addAttribute("pwd", new ResetPasswordDto());
		return templatePrefix() + "resetpassword";
	}

	/**
	 * 重置登录密码保存
	 * 
	 * @param password
	 * @param parameters
	 * @return
	 */
	@PostMapping("resetpassword/current")
	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
	@ResponseBody
	ReturnDto resetPasswordSave(@Valid ResetPasswordDto password,
			@RequestParam MultiValueMap<String, String> parameters) {
		userService.resetPassword(password);
		return ReturnDto.ok(localeMessageSourceService.getMessage("user.resetpassword.successed"));
	}

	@GetMapping("changepassword/{id}")
	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
	String changePasswordForm(@PathVariable("id") Long userId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		User selectedUser = this.userService.findActiveOne(userId);
		if (selectedUser == null)
			throw new BusinessException(localeMessageSourceService.getMessage("user.notfound"));
		ChangePasswordDto pwd = new ChangePasswordDto();
		pwd.setUserId(userId);
		pwd.setUserInfo(selectedUser.toString());
		model.addAttribute("pwd", pwd);
		return templatePrefix() + "changepassword";
	}

	@PostMapping("changepassword")
	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
	@ResponseBody
	ReturnDto changePasswordSave(@Valid ChangePasswordDto password,
			@RequestParam MultiValueMap<String, String> parameters) {
		userService.changePassword(password);
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
		model.addAttribute("departId", user.getDepart().getId());
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
	@PostMapping("/remove/{id}")
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

	@ModelAttribute(name = "user")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			User user = userService.findOne(id);
			if (user != null)
				model.addAttribute("user", user);
		}
	}

	private ReturnDto getOptionReturn(String messagekey) {
		return ReturnDto.ok(localeMessageSourceService.getMessage(messagekey), true, "", "user-page-layout");
	}

	private void addRoleList(Model model) {
		model.addAttribute("roleList", roleService.findActiveAll(new Sort("name")));
	}

	private String templatePrefix() {
		return "user/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserService userService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartService departService;

	@Autowired
	private UserValidator userValidator;

}
