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

import io.jianxun.domain.business.ProductionLine;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.ProductionLineService;

public class ProductionLineControllerITest extends AbstractIT {

	private static final String NAME = "测试名称";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductionLineService productionLineService;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private ProductionLine productionLine;

	@Before
	public void setUp() {
		// inti data
		super.setUp();
		productionLine = new ProductionLine();
		productionLine.setName(NAME);
		productionLine.setDepart(root);
		productionLineService.save(productionLine);
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/productionline/tree")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/productionline/tree").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(
						get("/device/productionline/page/{id}", root.getId()).with(securityContext(initSecurityContext("PRODUCTIONLINELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/productionline/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/productionline/create/{id}", root.getId()).with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("PRODUCTIONLINECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("productionline/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/device/productionline/create").param("name", "tt001")
						.param("depart.id", root.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("PRODUCTIONLINECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/device/productionline/create").param("name", NAME)
						.param("depart.id", root.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("PRODUCTIONLINECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value(containsString(
						localeMessageSourceService.getMessage("productionLine.name.isUsed", new Object[] { NAME }))));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/productionline/modify/{id}", productionLine.getId())
						.with(securityContext(initSecurityContext("PRODUCTIONLINEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("productionLine"))
				.andExpect(view().name("productionline/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/device/productionline/modify").param("name", "tt")
						.param("id", productionLine.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("PRODUCTIONLINEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message")
						.value(containsString(localeMessageSourceService.getMessage("productionLine.save.successd"))));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/productionline/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("PRODUCTIONLINEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/productionline/remove/{id}", productionLine.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("PRODUCTIONLINEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value(
						containsString(localeMessageSourceService.getMessage("productionLine.remove.successd"))));

	}

}
