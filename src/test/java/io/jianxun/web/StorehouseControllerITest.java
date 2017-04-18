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

import io.jianxun.domain.business.Storehouse;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.StorehouseService;

public class StorehouseControllerITest extends AbstractIT {

	private static final String NAME = "测试名称";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StorehouseService storehouseService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private Storehouse storehouse;

	@Before
	public void setUp() {
		// inti data
		super.setUp();
		storehouse = new Storehouse();
		storehouse.setName(NAME);
		storehouse.setDepart(root);
		storehouseService.save(storehouse);
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/storehouse/tree")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/storehouse/tree").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/device/storehouse/page/{id}", root.getId()).with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("STOREHOUSELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/storehouse/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/storehouse/create/{id}", root.getId()).with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("STOREHOUSECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("storehouse/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/device/storehouse/create").param("name", "tt001")
						.param("depart.id", root.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("STOREHOUSECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/device/storehouse/create").param("name", NAME)
						.param("depart.id", root.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("STOREHOUSECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value(containsString(
						localeMessageSourceService.getMessage("storehouse.name.isUsed", new Object[] { NAME }))));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/storehouse/modify/{id}", storehouse.getId())
						.with(securityContext(initSecurityContext("STOREHOUSEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("storehouse"))
				.andExpect(view().name("storehouse/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(
						post("/device/storehouse/modify").param("name", "tt").param("id", storehouse.getId().toString())
								.with(csrf()).with(securityContext(initSecurityContext("STOREHOUSEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("storehouse.save.successd"))));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/storehouse/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("STOREHOUSEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/storehouse/remove/{id}", storehouse.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("STOREHOUSEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("storehouse.remove.successd"))));

	}

}
