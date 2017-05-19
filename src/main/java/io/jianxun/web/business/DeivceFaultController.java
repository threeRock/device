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
import io.jianxun.domain.business.DeviceFault;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceFaultPredicates;
import io.jianxun.service.business.DeviceFaultService;
import io.jianxun.web.business.validator.DeviceFaultValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/fault")
public class DeivceFaultController {

	@InitBinder("deviceFault")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(deviceFaultValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page" })
	@PreAuthorize("hasAuthority('DEVICEFAULTLIST')")
	String page(Model model, @QuerydslPredicate(root = DeviceFault.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(this.currentLoginInfo.currentLoginUser().getDepart().getId());
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		Predicate searchPredicate = null;
		if (!depart.isRoot())
			searchPredicate = DeviceFaultPredicates.departPredicate(depart);
		if (searchPredicate == null && predicate != null) {
			searchPredicate = predicate;
		}
		Page<DeviceFault> page = null;
		if (searchPredicate != null)
			page = deviceFaultService.findActivePage(searchPredicate, pageable);
		else
			page = deviceFaultService.findActivePage(pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('DEVICEFAULTCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("deviceFault", new DeviceFault());
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
	@PreAuthorize("hasAuthority('DEVICEFAULTCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid DeviceFault deviceFault,
			@RequestParam MultiValueMap<String, String> parameters) {
		deviceFaultService.save(deviceFault);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.save.successd",
				new Object[] { deviceFault.getDevice().toString() }), true, "deviceFault-page", "");
	}

	/**
	 * 修改表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('DEVICEFAULTMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		DeviceFault deviceFault = deviceFaultService.findActiveOne(id);
		model.addAttribute("deviceFault", deviceFault);
		model.addAttribute("deviceinfo", deviceFault.getDevice().toString());
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
	@PreAuthorize("hasAuthority('DEVICEFAULTMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "deviceFault") DeviceFault deviceFault,
			Model model) {
		deviceFaultService.save(deviceFault);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.save.successd",
				new Object[] { deviceFault.getDevice().toString() }), true, "deviceFault-page", "");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('DEVICEFAULTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		deviceFaultService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.checkinfo.remove.successd"));
	}
	
	@GetMapping("detail/{id}")
	@PreAuthorize("hasAuthority('DEVICECHECKINFOLIST')")
	public String detail(@PathVariable("id") Long id, Model model) {
		DeviceFault deviceFault = deviceFaultService.findActiveOne(id);
		if (deviceFault == null)
			throw new BusinessException(localeMessageSourceService.getMessage("deviceFault.notfound"));
		if (deviceFault.getDevice() == null)
			throw new BusinessException(localeMessageSourceService.getMessage("dvice.notfound"));
		if (!currentLoginInfo.validateCurrentUserDepart(deviceFault.getDevice().getDepart()))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		model.addAttribute("deviceFault", deviceFault);
		return templatePrefix() + "detail";
	}

	@ModelAttribute(name = "deviceFault")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			DeviceFault deviceFault = deviceFaultService.findActiveOne(id);
			if (deviceFault != null)
				model.addAttribute("deviceFault", deviceFault);
		}
	}

	private String templatePrefix() {
		return "fault/";
	}

	@Autowired
	private DeviceFaultService deviceFaultService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private DeviceFaultValidator deviceFaultValidator;

}
