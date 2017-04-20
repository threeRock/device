package io.jianxun.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;

import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.ProductionLine;
import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DevicePredicates;
import io.jianxun.service.business.DeviceService;
import io.jianxun.service.business.ProductionLinePredicates;
import io.jianxun.service.business.ProductionLineService;
import io.jianxun.service.business.SparePartMainTypePredicates;
import io.jianxun.service.business.SparePartMainTypeService;

public class DeviceControllerITest extends AbstractIT {

	private static final String NAME = "测试名称";
	private static final String MAIN_TYPE_NAME = "设备类别";
	private static final String PRODUCTION_LINE_NAME = "产线";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ProductionLineService productionLineService;
	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private Device device;
	private SparePartMainType mainType;
	private ProductionLine productionLine;

	@Before
	public void setUp() {
		// inti data
		super.setUp();
		try {
			mainType = sparePartMainTypeService
					.findActiveOne(SparePartMainTypePredicates.namePredicate(MAIN_TYPE_NAME));
		} catch (Exception e) {
			mainType = null;
		}
		if (mainType == null) {
			mainType = new SparePartMainType();
			mainType.setName(MAIN_TYPE_NAME);
			mainType = sparePartMainTypeService.save(mainType);
		}
		try {
			productionLine = productionLineService
					.findActiveOne(ProductionLinePredicates.namePredicate(PRODUCTION_LINE_NAME));
		} catch (Exception e) {
			productionLine = null;
		}

		if (productionLine == null) {
			productionLine = new ProductionLine();
			productionLine.setName(PRODUCTION_LINE_NAME);
			productionLine.setDepart(root);
			productionLine = productionLineService.save(productionLine);
		}

		try {
			device = deviceService.findActiveOne(DevicePredicates.namePredicate(NAME));
		} catch (Exception e) {
			device = null;
		}

		if (device == null) {
			device = new Device();
			device.setName(NAME);
			device.setCode(NAME);
			device.setDepart(root);
			device.setProductionLine(productionLine);
			device.setMainType(mainType);
			device = deviceService.save(device);
		}
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/tree")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/tree").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/device/page/{id}", root.getId()).with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("DEVICELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/create/{id}", root.getId()).with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/create/{id}", root.getId()).with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("DEVICECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("device/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/device/create").param("name", "tt001").param("code", "tt001")
						.param("depart.id", root.getId().toString())
						.param("productionLine.id", productionLine.getId().toString())
						.param("mainType.id", mainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("DEVICECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/device/create").param("name", NAME).param("code", NAME + "code")
						.param("depart.id", root.getId().toString())
						.param("productionLine.id", productionLine.getId().toString())
						.param("mainType.id", mainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("DEVICECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value(containsString(
						localeMessageSourceService.getMessage("device.name.isUsed", new Object[] { NAME }))));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/modify/{id}", device.getId())
						.with(securityContext(initSecurityContext("DEVICEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("device"))
				.andExpect(view().name("device/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/device/modify").param("name", "tt").param("code", "xx")
						.param("id", device.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("DEVICEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("device.save.successd"))));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("DEVICEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/remove/{id}", device.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("DEVICEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("device.remove.successd"))));

	}

}
