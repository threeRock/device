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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceAdjustment;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceAdjustmentPredicates;
import io.jianxun.service.business.DeviceAdjustmentService;
import io.jianxun.service.business.DeviceService;
import io.jianxun.web.business.validator.DeviceAdjustmentValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/adjustment")
public class DeivceAdjustController {

	@InitBinder("deviceAdjustment")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(deviceAdjustmentValidator);

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page" })
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTLIST')")
	String page(Model model, @QuerydslPredicate(root = DeviceAdjustment.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(this.currentLoginInfo.currentLoginUser().getDepart().getId());
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		Predicate searchPredicate = null;
		if (!depart.isRoot())
			searchPredicate = DeviceAdjustmentPredicates.departPredicate(depart);
		if (searchPredicate == null && predicate != null) {
			searchPredicate = predicate;
		}
		Page<DeviceAdjustment> page = null;
		if (searchPredicate != null)
			page = deviceAdjustmentService.findActivePage(searchPredicate, pageable);
		else
			page = deviceAdjustmentService.findActivePage(pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create")
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("deviceAdjustment", new DeviceAdjustment());
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
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid DeviceAdjustment deviceAdjustment,
			@RequestParam MultiValueMap<String, String> parameters) {
		deviceAdjustmentService.save(deviceAdjustment);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.technical.param.save.successd",
				new Object[] { deviceAdjustment.getDevice().toString() }), true, "deviceAdjustment-page", "");
	}

	/**
	 * 修改表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		DeviceAdjustment deviceAdjustment = deviceAdjustmentService.findActiveOne(id);
		model.addAttribute("deviceAdjustment", deviceAdjustment);
		model.addAttribute("deviceinfo", deviceAdjustment.getDevice().toString());
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
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(
			@Valid @ModelAttribute(name = "deviceAdjustment") DeviceAdjustment deviceAdjustment,
			Model model) {
		deviceAdjustmentService.save(deviceAdjustment);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.technical.param.save.successd",
				new Object[] { deviceAdjustment.getDevice().toString() }), true, "deviceAdjustment-page", "");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('DEVICEADJUSTMENTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		deviceAdjustmentService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("device.technical.param.remove.successd"));
	}

	/**
	 * 验证名称是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@RequestMapping("check/nameunique")
	@ResponseBody
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("device.id") Long deviceId,
			@RequestParam("id") Long id) {
		if (deviceId == null)
			throw new BusinessException(localeMessageSourceService.getMessage("device.technical.param.device.notnull"));
		Device device = this.deviceService.findActiveOne(deviceId);
		if (device == null)
			throw new BusinessException(localeMessageSourceService.getMessage("device.technical.param.device.notnull"));
		if (!this.deviceAdjustmentService.validateNameUnique(device, name, id))
			return localeMessageSourceService.getMessage("device.technical.param.name.isUsed", new Object[] { name });
		return "";
	}

	@ModelAttribute(name = "deviceAdjustment")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			DeviceAdjustment deviceAdjustment = deviceAdjustmentService.findActiveOne(id);
			if (deviceAdjustment != null)
				model.addAttribute("deviceAdjustment", deviceAdjustment);
		}
	}

	private String templatePrefix() {
		return "adjustment/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private DeviceAdjustmentService deviceAdjustmentService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private DeviceAdjustmentValidator deviceAdjustmentValidator;

}
