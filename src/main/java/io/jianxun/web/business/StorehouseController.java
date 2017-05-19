package io.jianxun.web.business;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Storehouse;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceStorageService;
import io.jianxun.service.business.StorehousePredicates;
import io.jianxun.service.business.StorehouseService;
import io.jianxun.web.business.validator.StorehouseValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.dto.UploadPicReturnDto;
import io.jianxun.web.utils.CurrentLoginInfo;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/storehouse")
public class StorehouseController {

	@InitBinder("storehouse")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(storehouseValidator);

	}

	@GetMapping(value = "tree")
	@PreAuthorize("hasAuthority('STOREHOUSELIST')")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(storehouseService.getStorehouseTree()));
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
	@PreAuthorize("hasAuthority('STOREHOUSELIST')")
	String page(@PathVariable("depart") Long departId, Model model,
			@QuerydslPredicate(root = Storehouse.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!currentLoginInfo.validateCurrentUserDepart(depart))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate searchPredicate = null;
		Page<Storehouse> page = null;
		if (predicate == null && depart.isRoot()) {
			page = storehouseService.findActivePage(pageable);
		} else {
			searchPredicate = ExpressionUtils.and(StorehousePredicates.departSubPredicate(depart), predicate);
			page = storehouseService.findActivePage(searchPredicate, pageable);
		}

		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addParentStorehouseInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addParentStorehouseInfo(Model model, Depart depart) {
		model.addAttribute("depart", depart);
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{departId}")
	@PreAuthorize("hasAuthority('STOREHOUSECREATE')")
	String createForm(@PathVariable("departId") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		model.addAttribute("storehouse", new Storehouse());
		model.addAttribute("departId", departId);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	/**
	 * 新增保存
	 * 
	 * @param storehouse
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('STOREHOUSECREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid Storehouse storehouse, @RequestParam MultiValueMap<String, String> parameters) {
		storehouseService.save(storehouse);
		return ReturnDto.ok(localeMessageSourceService.getMessage("storehouse.save.successd"), true, "",
				"storehouse-page-layout");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('STOREHOUSEMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		Storehouse storehouse = storehouseService.findActiveOne(id);
		model.addAttribute("storehouse", storehouse);
		model.addAttribute("departId", storehouse.getDepart() != null ? storehouse.getDepart().getId() : null);
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
	@PreAuthorize("hasAuthority('STOREHOUSEMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "storehouse") Storehouse storehouse, Model model) {
		storehouseService.save(storehouse);
		return ReturnDto.ok(localeMessageSourceService.getMessage("storehouse.save.successd"), true, "",
				"storehouse-page-layout");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('STOREHOUSEREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		storehouseService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("storehouse.remove.successd"));
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
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("depart.id") Long departId,
			@RequestParam("id") Long id) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!this.storehouseService.validateNameUnique(name, depart, id))
			return localeMessageSourceService.getMessage("storehouse.name.isUsed", new Object[] { name });
		return "";
	}

	@RequestMapping("check/codeunique")
	@ResponseBody
	public String checkCodeIsUnique(@RequestParam("code") String code, @RequestParam("depart.id") Long departId,
			@RequestParam("id") Long id) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!this.storehouseService.validateCodeUnique(code, depart, id))
			return localeMessageSourceService.getMessage("storehouse.code.isUsed", new Object[] { code });
		return "";
	}

	@PostMapping("pic/up")
	@ResponseBody
	public UploadPicReturnDto uploadPic(@RequestParam("file") MultipartFile file) {
		return new UploadPicReturnDto(200, "", deviceStorageService.store(UPLOAD_FOLDER_NAME, file));
	}

	@GetMapping("/pic/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = deviceStorageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ModelAttribute(name = "storehouse")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			Storehouse storehouse = storehouseService.findOne(id);
			if (storehouse != null)
				model.addAttribute("storehouse", storehouse);
		}
	}

	private String templatePrefix() {
		return "storehouse/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private StorehouseService storehouseService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;

	@Autowired
	private StorehouseValidator storehouseValidator;

	@Autowired
	private DeviceStorageService deviceStorageService;
	private static final String UPLOAD_FOLDER_NAME = "cangku";

}
