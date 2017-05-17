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

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.DeviceCheckInfo;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceCheckInfoPredicates;
import io.jianxun.service.business.DeviceCheckInfoService;
import io.jianxun.web.business.validator.DeviceCheckInfoValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/checkInfo")
public class DeivceCheckInfoController {

	@InitBinder("deviceCheckInfo")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(deviceCheckInfoValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page" })
	@PreAuthorize("hasAuthority('DEVICECHECKINFOLIST')")
	String page(Model model, @QuerydslPredicate(root = DeviceCheckInfo.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(this.currentLoginInfo.currentLoginUser().getDepart().getId());
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		Predicate searchPredicate = null;
		if (!depart.isRoot())
			searchPredicate = DeviceCheckInfoPredicates.departPredicate(depart);
		if (searchPredicate == null && predicate != null) {
			searchPredicate = predicate;
		}
		Page<DeviceCheckInfo> page = null;
		if (searchPredicate != null)
			page = deviceCheckInfoService.findActivePage(searchPredicate, pageable);
		else
			page = deviceCheckInfoService.findActivePage(pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('DEVICECHECKINFOCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("deviceCheckInfo", new DeviceCheckInfo());
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param device
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('DEVICECHECKINFOCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid DeviceCheckInfo deviceCheckInfo,
			@RequestParam MultiValueMap<String, String> parameters) {
		deviceCheckInfoService.save(deviceCheckInfo);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.save.successd",
				new Object[] { deviceCheckInfo.getDevice().toString() }), true, "deviceCheckInfo-page", "");
	}

	/**
	 * 修改表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('DEVICECHECKINFOMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		DeviceCheckInfo deviceCheckInfo = deviceCheckInfoService.findActiveOne(id);
		model.addAttribute("deviceCheckInfo", deviceCheckInfo);
		model.addAttribute("deviceinfo", deviceCheckInfo.getDevice().toString());
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
	@PreAuthorize("hasAuthority('DEVICECHECKINFOMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "deviceCheckInfo") DeviceCheckInfo deviceCheckInfo,
			Model model) {
		deviceCheckInfoService.save(deviceCheckInfo);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.save.successd",
				new Object[] { deviceCheckInfo.getDevice().toString() }), true, "deviceCheckInfo-page", "");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('DEVICECHECKINFOREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		deviceCheckInfoService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.remove.successd"));
	}

	@ModelAttribute(name = "deviceCheckInfo")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			DeviceCheckInfo deviceCheckInfo = deviceCheckInfoService.findActiveOne(id);
			if (deviceCheckInfo != null)
				model.addAttribute("deviceCheckInfo", deviceCheckInfo);
		}
	}

	private String templatePrefix() {
		return "checkinfo/";
	}

	@Autowired
	private DeviceCheckInfoService deviceCheckInfoService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private DeviceCheckInfoValidator deviceCheckInfoValidator;

}
