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

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.domain.business.SparePartSubType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypeService;
import io.jianxun.service.business.SparePartSubTypeService;

public class SparePartSubTypeControllerITest extends AbstractIT {

	private static final String SUBTYPE_NAME = "测试名称";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SparePartSubTypeService sparePartSubTypeService;
	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private SparePartSubType sparePartSubType;

	private SparePartMainType sparePartMainType;

	@Before
	public void setUp() {
		// inti data
		sparePartMainType = new SparePartMainType();
		sparePartMainType.setName("备件主类");
		sparePartMainTypeService.save(sparePartMainType);
		sparePartSubType = new SparePartSubType();
		sparePartSubType.setName(SUBTYPE_NAME);
		sparePartSubType.setMainType(sparePartMainType);
		sparePartSubTypeService.save(sparePartSubType);
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/subtype/")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/subtype/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/device/subtype/").with(securityContext(initSecurityContext("SUBTYPELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(SUBTYPE_NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/subtype/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/subtype/create").with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("SUBTYPECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("subtype/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/device/subtype/create").param("name", "tt001")
						.param("mainType.id", sparePartMainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SUBTYPECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

	}

	@Test
	public void create_save_not_unique() throws Exception {
		// 验证名称重复
		this.mockMvc
				.perform(post("/device/subtype/create").param("name", SUBTYPE_NAME)
						.param("mainType.id", sparePartMainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SUBTYPECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message")
						.value(containsString((localeMessageSourceService.getMessage("subtype.name.isUsed",
								new Object[] { sparePartMainType.getName(), SUBTYPE_NAME })))));
	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/subtype/modify/{id}", sparePartSubType.getId())
						.with(securityContext(initSecurityContext("SUBTYPEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("subtype"))
				.andExpect(view().name("subtype/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/device/subtype/modify").param("name", "tt")
						.param("id", sparePartSubType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SUBTYPEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("备件子类保存成功"));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/subtype/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("SUBTYPEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/subtype/remove/{id}", sparePartSubType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("SUBTYPEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("备件子类删除成功"));

	}

}
