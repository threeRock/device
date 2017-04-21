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
import io.jianxun.domain.business.SparePart;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DeviceStorageService;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.service.business.SparePartPredicates;
import io.jianxun.service.business.SparePartService;
import io.jianxun.service.business.StorehousePredicates;
import io.jianxun.service.business.StorehouseService;
import io.jianxun.web.business.validator.DeviceValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.dto.UploadPicReturnDto;
import io.jianxun.web.utils.Utils;

@Controller
@RequestMapping("device/sparepart")
public class SparePartController {

	@InitBinder("sparepart")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(deviceValidator);

	}

	@GetMapping(value = "tree")
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	public String tree(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		try {
			model.addAttribute("tree", mapper.writeValueAsString(sparePartService.getSparePartTree()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new BusinessException(localeMessageSourceService.getMessage("depart.tree.error"));
		}
		return templatePrefix() + "/tree";

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page/{depart}" })
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	String page(@PathVariable("depart") Long departId, Model model,
			@QuerydslPredicate(root = SparePart.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		// TODO 是否有查看仓库权限
		Predicate sparePartPredicate = SparePartPredicates.departPredicate(depart);
		if (predicate != null)
			sparePartPredicate = ExpressionUtils.and(sparePartPredicate, predicate);
		Page<SparePart> page = sparePartService.findActivePage(sparePartPredicate, pageable);
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addParentSparePartInfo(model, depart);
		addStorehouseAndTypeInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addParentSparePartInfo(Model model, Depart depart) {
		model.addAttribute("depart", depart);
	}

	/**
	 * 新增表单页面
	 */
	@GetMapping("create/{departId}")
	@PreAuthorize("hasAuthority('SPAREPARTCREATE')")
	String createForm(@PathVariable("departId") Long departId, Model model,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		model.addAttribute("sparePart", new SparePart());
		model.addAttribute("departId", departId);
		addStorehouseAndTypeInfo(model, depart);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	private void addStorehouseAndTypeInfo(Model model, Depart depart) {
		model.addAttribute("lines",
				storehouseService.findActiveAll(StorehousePredicates.departPredicate(depart), new Sort("name")));
		model.addAttribute("types", sparePartMainTypeService.findActiveAll(new Sort("name")));
	}

	/**
	 * 新增保存
	 * 
	 * @param sparePart
	 * @param parameters
	 * @return
	 */
	@PostMapping("create")
	@PreAuthorize("hasAuthority('SPAREPARTCREATE')")
	@ResponseBody
	ReturnDto createSave(@Valid SparePart sparePart, @RequestParam MultiValueMap<String, String> parameters) {
		sparePartService.save(sparePart);
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparePart.save.successd"), true, "",
				"sparePart-page-layout");
	}

	/**
	 * 修改角色表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/modify/{id}")
	@PreAuthorize("hasAuthority('SPAREPARTMODIFY')")
	public String modify(@PathVariable("id") Long id, Model model) {
		SparePart sparePart = sparePartService.findActiveOne(id);
		model.addAttribute("sparePart", sparePart);
		model.addAttribute("departId", sparePart.getDepart() != null ? sparePart.getDepart().getId() : null);
		util.addModifyFormAction(model);
		addStorehouseAndTypeInfo(model, sparePart.getDepart());
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
	@PreAuthorize("hasAuthority('SPAREPARTMODIFY')")
	@ResponseBody
	public ReturnDto modifySave(@Valid @ModelAttribute(name = "sparePart") SparePart sparePart, Model model) {
		sparePartService.save(sparePart);
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparePart.save.successd"), true, "",
				"sparePart-page-layout");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('SPAREPARTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		sparePartService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparePart.remove.successd"));
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
		if (!this.sparePartService.validateNameUnique(name, depart, id))
			return localeMessageSourceService.getMessage("sparePart.name.isUsed", new Object[] { name });
		return "";
	}

	@RequestMapping("check/codeunique")
	@ResponseBody
	public String checkCodeIsUnique(@RequestParam("code") String code, @RequestParam("depart.id") Long departId,
			@RequestParam("id") Long id) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!this.sparePartService.validateCodeUnique(code, depart, id))
			return localeMessageSourceService.getMessage("sparePart.code.isUsed", new Object[] { code });
		return "";
	}

	@PostMapping("pic/up")
	@ResponseBody
	public UploadPicReturnDto uploadPic(@RequestParam("file") MultipartFile file) {
		deviceStorageService.store(UPLOAD_FOLDER_NAME, file);
		return new UploadPicReturnDto(200, "", deviceStorageService.getFilePathString(UPLOAD_FOLDER_NAME, file));
	}

	@GetMapping("/pic/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = deviceStorageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ModelAttribute(name = "sparePart")
	public void getMode(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != null && id != -1L) {
			SparePart sparePart = sparePartService.findOne(id);
			if (sparePart != null)
				model.addAttribute("sparePart", sparePart);
		}
	}

	private String templatePrefix() {
		return "sparePart/";
	}

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private SparePartService sparePartService;
	@Autowired
	private DepartService departService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;
	@Autowired
	private DeviceStorageService deviceStorageService;
	@Autowired
	private StorehouseService storehouseService;
	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private Utils util;

	@Autowired
	private DeviceValidator deviceValidator;
	private static final String UPLOAD_FOLDER_NAME = "beijian";

}
