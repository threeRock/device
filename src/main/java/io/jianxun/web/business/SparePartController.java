package io.jianxun.web.business;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
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
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.SparePart;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.DevicePredicates;
import io.jianxun.service.business.DeviceService;
import io.jianxun.service.business.DeviceStorageService;
import io.jianxun.service.business.SparePartPredicates;
import io.jianxun.service.business.SparePartService;
import io.jianxun.service.business.SparePartSubTypeService;
import io.jianxun.service.business.StorehousePredicates;
import io.jianxun.service.business.StorehouseService;
import io.jianxun.web.business.validator.DeviceValidator;
import io.jianxun.web.dto.ReturnDto;
import io.jianxun.web.dto.UploadPicReturnDto;
import io.jianxun.web.dto.ValueLabelDto;
import io.jianxun.web.utils.CurrentLoginInfo;
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
		return templatePrefix() + "tree";

	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page/{departId}" })
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	String page(@PathVariable("departId") Long departId, Model model,
			@QuerydslPredicate(root = SparePart.class) Predicate predicate,
			@PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!currentLoginInfo.validateCurrentUserDepart(depart))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Page<SparePart> page = null;
		if (predicate == null && depart.isRoot()) {
			page = sparePartService.findActivePage(pageable);
		} else {
			if (!depart.isRoot())
				predicate = ExpressionUtils.and(SparePartPredicates.departSubPredicate(depart), predicate);
			page = sparePartService.findActivePage(predicate, pageable);
		}
		// 计算库存
		sparePartService.getStock(page.getContent());
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		addParentSparePartInfo(model, depart);
		model.addAttribute("url", "page/" + departId);
		model.addAttribute("createUrl", "device/sparepart/create/" + depart.getId());
		addStorehouseAndTypeInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	/**
	 * 分页列表 支持 查询 分页 及 排序
	 */
	@RequestMapping(value = { "/page" })
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	String page(Model model, @QuerydslPredicate(root = SparePart.class) Predicate predicate,
			@PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(name = "searchDepart", required = false) Long searchDepart,
			@RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.currentLoginInfo.currentLoginUser().getDepart();
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		Predicate searchPredicate = SparePartPredicates.departSubPredicate(depart);
		Page<SparePart> page = null;
		if (searchDepart != null) {
			Depart search = departService.findActiveOne(searchDepart);
			if (search != null)
				searchPredicate = ExpressionUtils.and(SparePartPredicates.departSubPredicate(search), searchPredicate);
		}
		if (predicate != null)
			searchPredicate = ExpressionUtils.and(predicate, searchPredicate);
		page = sparePartService.findActivePage(searchPredicate, pageable);
		sparePartService.getStock(page.getContent());
		util.addPageInfo(model, parameters, page);
		util.addSearchInfo(model, parameters);
		model.addAttribute("url", "page");
		model.addAttribute("createUrl", "device/sparepart/create");
		addParentSparePartInfo(model, depart);
		addStorehouseAndTypeInfo(model, depart);
		return templatePrefix() + Utils.PAGE_TEMPLATE_SUFFIX;
	}

	private void addParentSparePartInfo(Model model, Depart depart) {
		model.addAttribute("departId", depart.getId());
		List<Depart> departs = Lists.newArrayList(depart);
		departService.getSubDeparts(departs, depart);
		model.addAttribute("departs", departs);
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

	@GetMapping("create")
	@PreAuthorize("hasAuthority('SPAREPARTCREATE')")
	String createForm(Model model, @RequestParam MultiValueMap<String, String> parameters) {
		Depart depart = this.currentLoginInfo.currentLoginUser().getDepart();
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		model.addAttribute("sparePart", new SparePart());
		addParentSparePartInfo(model, depart);
		addStorehouseAndTypeInfo(model, depart);
		util.addCreateFormAction(model);
		return templatePrefix() + Utils.SAVE_TEMPLATE_SUFFIX;
	}

	private void addStorehouseAndTypeInfo(Model model, Depart depart) {
		if (depart.isRoot())
			model.addAttribute("storehouses", storehouseService.findActiveAll(new Sort("name")));
		else
			model.addAttribute("storehouses",
					storehouseService.findActiveAll(StorehousePredicates.departPredicate(depart), new Sort("name")));
		model.addAttribute("types", sparePartSubTypeService.findActiveAll(new Sort("name")));

		if (depart.isRoot())
			model.addAttribute("devices", deviceService.findActiveAll(new Sort("name")));
		else
			model.addAttribute("devices",
					deviceService.findActiveAll(DevicePredicates.departPredicate(depart), new Sort("name")));

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
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparepart.save.successd"), true, "sparePart-page");
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
		addParentSparePartInfo(model, this.currentLoginInfo.currentLoginUser().getDepart());
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
		if (!sparePartService.modifiable(sparePart))
			throw new BusinessException(localeMessageSourceService.getMessage("sparePart.cannotmodify"));
		sparePartService.save(sparePart);
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparepart.save.successd"), true, "sparePart-page");
	}

	@PostMapping("remove/{id}")
	@PreAuthorize("hasAuthority('SPAREPARTREMOVE')")
	@ResponseBody
	public ReturnDto remove(@PathVariable("id") Long id) {
		SparePart sparePart = this.sparePartService.findActiveOne(id);
		if (sparePart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("sparepart.notfound"));
		if (!sparePartService.modifiable(sparePart))
			throw new BusinessException(localeMessageSourceService.getMessage("sparePart.cannotmodify"));
		sparePartService.delete(id);
		return ReturnDto.ok(localeMessageSourceService.getMessage("sparepart.remove.successd"));
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
	public String checkNameIsUnique(@RequestParam("name") String name, @RequestParam("depart") Long departId,
			@RequestParam("id") Long id) {
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!this.sparePartService.validateNameUnique(name, depart, id))
			return localeMessageSourceService.getMessage("sparepart.name.isUsed", new Object[] { name });
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

	@RequestMapping("export")
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	public void export(@RequestParam("d.id") Long departId, Model model,
			@QuerydslPredicate(root = SparePart.class) Predicate predicate, HttpServletResponse response)
			throws Exception {
		// 告诉浏览器用什么软件可以打开此文件
		response.setHeader("content-Type", "application/vnd.ms-excel");
		// 下载文件的默认名称
		response.setHeader("Content-Disposition", "attachment;filename=备件导出.xls");
		Depart depart = this.departService.findActiveOne(departId);
		if (depart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notfound"));
		if (!currentLoginInfo.validateCurrentUserDepart(depart))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		Predicate searchPredicate = null;
		List<SparePart> list = null;
		if (predicate == null && depart.isRoot()) {
			list = sparePartService.findActiveAll(new Sort("name", "id"));
		} else {
			searchPredicate = ExpressionUtils.and(SparePartPredicates.departSubPredicate(depart), predicate);
			list = this.sparePartService.findActiveAll(searchPredicate, new Sort("name", "id"));
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SparePart.class, list);
		workbook.write(response.getOutputStream());
	}

	@RequestMapping(value = "/search/{currentDeparrId}")
	@ResponseBody
	public List<ValueLabelDto> getParentDepartStock(@PathVariable("currentDeparrId") Long pId,
			@RequestParam("term") String name, Model model) {
		Depart currentDepart = departService.findOne(pId);
		if (currentDepart == null)
			throw new BusinessException("获取机构信息失败,无法获取对应备件");
		if (StringUtils.isBlank(name))
			return Lists.newArrayList();
		List<SparePart> parts = sparePartService.findActiveAll(SparePartPredicates.nameContainsPredicate(name),
				new Sort("name"));
		return getDto(parts);

	}

	@RequestMapping(value = "/use/{id}")
	public String getUserList(@PathVariable("id") Long id, @RequestParam(value = "year", required = false) Integer year,
			Model model) {
		SparePart spartPart = this.sparePartService.findActiveOne(id);
		if (spartPart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("sparepart.notfound"));
		model.addAttribute("use", this.sparePartService.getUse(year, spartPart));
		return templatePrefix() + "uselist";

	}

	@GetMapping("detail/{id}")
	@PreAuthorize("hasAuthority('SPAREPARTLIST')")
	public String detail(@PathVariable("id") Long id, Model model) {
		SparePart sparePart = sparePartService.findOne(id);
		if (sparePart == null)
			throw new BusinessException(localeMessageSourceService.getMessage("sparepart.notfound"));
		if (!currentLoginInfo.validateCurrentUserDepart(sparePart.getDepart()))
			throw new BusinessException(localeMessageSourceService.getMessage("depart.notview"));
		model.addAttribute("sparePart", sparePart);
		return templatePrefix() + "detail";
	}

	private List<ValueLabelDto> getDto(List<SparePart> parts) {
		List<ValueLabelDto> vls = Lists.newArrayList();
		for (SparePart part : parts) {
			ValueLabelDto d = new ValueLabelDto();
			d.setLabel(part.toString());
			d.setValue(part.getId().toString());
			vls.add(d);
		}
		return vls;
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
	private DeviceService deviceService;
	@Autowired
	private StorehouseService storehouseService;
	@Autowired
	private SparePartSubTypeService sparePartSubTypeService;

	@Autowired
	private Utils util;
	@Autowired
	private CurrentLoginInfo currentLoginInfo;

	@Autowired
	private DeviceValidator deviceValidator;
	private static final String UPLOAD_FOLDER_NAME = "beijian";

}
