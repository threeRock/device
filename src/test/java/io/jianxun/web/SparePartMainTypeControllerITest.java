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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.jianxun.domain.business.SparePartMainType;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.SparePartMainTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SparePartMainTypeControllerITest extends AbstractIT {

	private static final String MAINTYPE_NAME = "测试名称";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SparePartMainTypeService sparePartMainTypeService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	private SparePartMainType sparePartMainType;

	@Before
	public void setUp() {
		// inti data
		super.setUp();
		sparePartMainType = new SparePartMainType();
		sparePartMainType.setName(MAINTYPE_NAME);
		sparePartMainTypeService.save(sparePartMainType);
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/device/maintype/")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/device/maintype/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/device/maintype/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("MAINTYPELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(MAINTYPE_NAME)));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/device/maintype/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/device/maintype/create").with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("MAINTYPECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("maintype/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/device/maintype/create").param("name", "tt001").with(csrf())
						.with(securityContext(initSecurityContext("MAINTYPECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/device/maintype/create").param("name", MAINTYPE_NAME).with(csrf())
						.with(securityContext(initSecurityContext("MAINTYPECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value(containsString(localeMessageSourceService
						.getMessage("maintype.name.isUsed", new Object[] { MAINTYPE_NAME }))));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/device/maintype/modify/{id}", sparePartMainType.getId())
						.with(securityContext(initSecurityContext("MAINTYPEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("maintype"))
				.andExpect(view().name("maintype/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/device/maintype/modify").param("name", "tt")
						.param("id", sparePartMainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("MAINTYPEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("备件大类保存成功"));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/device/maintype/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("MAINTYPEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/device/maintype/remove/{id}", sparePartMainType.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("MAINTYPEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("备件大类删除成功"));

	}


}
