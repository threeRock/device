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

import io.jianxun.domain.business.SparePart;
import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;
import io.jianxun.domain.business.Storehouse;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypePredicates;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.service.business.SparePartPredicates;
import io.jianxun.service.business.SparePartService;
import io.jianxun.service.business.SparePartSubTypePredicates;
import io.jianxun.service.business.SparePartSubTypeService;
import io.jianxun.service.business.StorehousePredicates;
import io.jianxun.service.business.StorehouseService;

public class SparePartControllerITest extends AbstractIT {

	private static final String NAME = "测试名称";
	private static final String SUBTYPE_NAME = "设备子类别";
	private static final String MAINTYPE_NAME = "设备主类别";
	private static final String STOREHOUSE_NAME = "仓库";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SparePartService sparePartService;
	@Autowired
	private SparePartSubTypeService sparePartSubTypeService;
	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private StorehouseService storehouseService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private SparePart sparePart;
	private SparePartSubType subType;
	private SparePartMainType mainType;
	private Storehouse storehouse;

	@Before
	public void setUp() {
		// inti data
		super.setUp();

		try {
			mainType = sparePartMainTypeService.findActiveOne(SparePartMainTypePredicates.namePredicate(MAINTYPE_NAME));
		} catch (Exception e) {
			mainType = null;
		}
		if (mainType == null) {
			mainType = new SparePartMainType();
			mainType.setName(MAINTYPE_NAME);
			mainType = sparePartMainTypeService.save(mainType);
		}

		try {
			subType = sparePartSubTypeService
					.findActiveOne(SparePartSubTypePredicates.namePredicate(SUBTYPE_NAME, mainType));
		} catch (Exception e) {
			subType = null;
		}
		if (subType == null) {
			subType = new SparePartSubType();
			subType.setName(SUBTYPE_NAME);
			subType.setMainType(mainType);
			subType = sparePartSubTypeService.save(subType);
		}
		try {
			storehouse = storehouseService.findActiveOne(StorehousePredicates.namePredicate(STOREHOUSE_NAME));
		} catch (Exception e) {
			storehouse = null;
		}

		if (storehouse == null) {
			storehouse = new Storehouse();
			storehouse.setName(STOREHOUSE_NAME);
			storehouse.setCode(STOREHOUSE_NAME);
			storehouse.setDepart(root);
			storehouse = storehouseService.save(storehouse);
		}

		try {
			sparePart = sparePartService.findActiveOne(SparePartPredicates.namePredicate(NAME));
		} catch (Exception e) {
			sparePart = null;
		}

		if (sparePart == null) {
			sparePart = new SparePart();
			sparePart.setName(NAME);
			sparePart.setCode(NAME);
			sparePart.setDepart(root);
			sparePart.setStorehouse(storehouse);
			sparePart.setSubType(subType);
			sparePart = sparePartService.save(sparePart);
		}
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/sparepart/tree")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/sparepart/tree").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/device/sparepart/page/{id}", root.getId()).with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("SPAREPARTLIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/sparepart/create/{id}", root.getId()).with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/sparepart/create/{id}", root.getId()).with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("SPAREPARTCREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("sparePart/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc.perform(post("/device/sparepart/create").param("name", "tt001").param("code", "tt001")
				.param("depart.id", root.getId().toString()).param("storehouse.id", storehouse.getId().toString())
				.param("subType.id", subType.getId().toString()).with(csrf())
				.with(securityContext(initSecurityContext("SPAREPARTCREATE")))).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/device/sparepart/create").param("name", NAME).param("code", NAME + "code")
						.param("depart.id", root.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SPAREPARTCREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value(containsString(
						localeMessageSourceService.getMessage("sparePart.name.isUsed", new Object[] { NAME }))));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/sparepart/modify/{id}", sparePart.getId())
						.with(securityContext(initSecurityContext("SPAREPARTMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("sparePart"))
				.andExpect(view().name("sparePart/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/device/sparepart/modify").param("name", "tt").param("code", "xx")
						.param("id", sparePart.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SPAREPARTMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("sparePart.save.successd"))));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/sparepart/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("SPAREPARTMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/sparepart/remove/{id}", sparePart.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SPAREPARTREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("sparePart.remove.successd"))));

	}

}
