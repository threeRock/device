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

import io.jianxun.domain.business.Depttest;
import io.jianxun.domain.business.Usertest;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepttestService;
//import io.jianxun.service.business.RoleService;
import io.jianxun.service.business.UsertestPredicates;
import io.jianxun.service.business.UsertestService;
import io.jianxun.web.business.validator.UsertestValidator;
import io.jianxun.web.dto.ChangePasswordDto;
import io.jianxun.web.dto.ResetPasswordDto;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("usertest")
public class UsertestController {

	public UsertestController() {
		super();
	}

	@InitBinder("usertest")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(usertestValidator);
	}

	@GetMapping(value = "tree")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(depttestService.getUserDepartTree()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new BusinessException(localeMessageSourceService.getMessage("depttest.tree.error"));
		}
		return templatePrefix() + "tree";

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page/{depttest}" })
	@PreAuthorize("hasAuthority('USERLIST')")
	String page(@PathVariable("depttest") long departId, Model model,
			@QuerydslPredicate(root = Usertest.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depttest depart = this.depttestService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depttest.notfound"));

//		if (!currentLoginInfo.validateCurrentUserDepart(depart))
//			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate searchPredicate = null;
		Page<Usertest> page = null;
		if (predicate == null && depart.isRoot()) {
			page = usertestService.findActivePage(pageable);
		} else {
			searchPredicate = ExpressionUtils.and(UsertestPredicates.departSubPredicate(depart), predicate);
			page = usertestService.findActivePage(searchPredicate, pageable);
		}

		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		model.addAttribute("depttestId", departId);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{depttest}")
	@PreAuthorize("hasAuthority('USERCREATE')")
	String createForm(@PathVariable("depttest") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		Usertest user = new Usertest();
		model.addAttribute("usertest", user);
		model.addAttribute("depttestId", departId);
		//addRoleList(model);
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
	ReturnDto createSave(@Valid Usertest user, @RequestParam MultiValueMap<String, String> parameters) {
		usertestService.save(user);
		return ReturnDto.ok(localeMessageSourceService.getMessage("usertest.save.successd"), true, "",
				"usertest-page-layout");
	}
//
//	/**
//	 * 重置登录密碼表单
//	 * 
//	 * @param model
//	 * @return
//	 */
//	@GetMapping("resetpassword/current")
//	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
//	String resetPasswordForm(Model model) {
//		model.addAttribute("pwd", new ResetPasswordDto());
//		return templatePrefix() + "resetpassword";
//	}
//
//	/**
//	 * 重置登录密码保存
//	 * 
//	 * @param password
//	 * @param parameters
//	 * @return
//	 */
//	@PostMapping("resetpassword/current")
//	@PreAuthorize("hasAuthority('USERRESETPASSWROD')")
//	@ResponseBody
//	ReturnDto resetPasswordSave(@Valid ResetPasswordDto password,
//			@RequestParam MultiValueMap<String, String> parameters) {
//		userService.resetPassword(password);
//		return ReturnDto.ok(localeMessageSourceService.getMessage("user.resetpassword.successed"));
//	}

//	@GetMapping("changepassword/{id}")
//	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
//	String changePasswordForm(@PathVariable("id") Long userId, Model model,
//			@RequestParam MultiValueMap<String, String> parameters) {
//		User selectedUser = this.userService.findActiveOne(userId);
//		if (selectedUser == null)
//			throw new BusinessException(localeMessageSourceService.getMessage("user.notfound"));
//		ChangePasswordDto pwd = new ChangePasswordDto();
//		pwd.setUserId(userId);
//		pwd.setUserInfo(selectedUser.toString());
//		model.addAttribute("pwd", pwd);
//		return templatePrefix() + "changepassword";
//	}
//
//	@PostMapping("changepassword")
//	@PreAuthorize("hasAuthority('USERCHANGEPASSWROD')")
//	@ResponseBody
//	ReturnDto changePasswordSave(@Valid ChangePasswordDto password,
//			@RequestParam MultiValueMap<String, String> parameters) {
//		userService.changePassword(password);
//		return ReturnDto.ok(localeMessageSourceService.getMessage("user.resetpassword.successed"));
//	}

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
		Usertest user = usertestService.findActiveOne(id);
		model.addAttribute("usertest", user);
//		addRoleList(model);
		model.addAttribute("depttestId", user.getDepttest().getId());
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
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "usertest") Usertest entity, Model model) {
		usertestService.save(entity);
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
		usertestService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("usertest.remove.successd"));
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
		if (!this.usertestService.validateUsernameUnique(username, id))
			return localeMessageSourceService.getMessage("usertest.username.isUsed", new Object[] { username });
		return "";
	}

	@ModelAttribute(name = "usertest")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			Usertest user = usertestService.findOne(id);
			if (user != null)
				model.addAttribute("usertest", user);
		}
	}

//	private ReturnDto getOptionReturn(String messagekey) {
//		return ReturnDto.ok(localeMessageSourceService.getMessage(messagekey), true, "", "usertest-page-layout");
//	}

//	private void addRoleList(Model model) {
//		model.addAttribute("roleList", roleService.findActiveAll(new Sort("name")));
//	}

	private String templatePrefix() {
		return "usertest/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UsertestService usertestService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;
//	@Autowired
//	private RoleService roleService;
	@Autowired
	private DepttestService depttestService;

	@Autowired
	private UsertestValidator usertestValidator;

}
