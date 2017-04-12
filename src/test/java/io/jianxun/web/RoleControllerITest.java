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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Role;
import io.jianxun.domain.business.User;
import io.jianxun.service.business.RoleService;
import io.jianxun.service.business.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoleControllerITest {

	private static final String ROLE_NAME = "测试用户";
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;

	private Role role;
	private User loginUser;

	@Before
	public void setUp() {
		// inti data
		role = new Role();
		role.setName(ROLE_NAME);
		role.setPermissions(Lists.newArrayList("USERLIST", "ROLELIST"));
		role = roleService.save(role);

		loginUser = new User();
		loginUser.setUsername("loginUser");
		loginUser.setDisplayName("loginUser");
		loginUser.setPassword("tt");
		loginUser.setRoles(Lists.newArrayList(role));
		loginUser = userService.register(loginUser);
	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/role/")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/role/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {

		this.mockMvc
				.perform(get("/role/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLELIST"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("测试用户")));
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/role/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/role/create").with(
						user("userUser").authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("role/form"));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/role/create").param("name", "tt").with(csrf())
						.with(securityContext(initSecurityContext("ROLECREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		// 验证名称重复
		this.mockMvc
				.perform(post("/role/create").param("name", ROLE_NAME).with(csrf())
						.with(securityContext(initSecurityContext("ROLECREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message")
						.value("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;测试用户 角色名称已经存在，不能重复使用<br />"));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(
						get("/role/modify/{id}", role.getId()).with(securityContext(initSecurityContext("ROLEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("role"))
				.andExpect(view().name("role/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/role/modify").param("name", "tt").param("id", role.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("ROLEMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("角色保存成功"));

		// 角色名称为空 请求失败
		this.mockMvc
				.perform(post("/role/modify").param("name", "").with(csrf())
						.with(securityContext(initSecurityContext("ROLEMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError());

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(post("/role/remove/{id}", role.getId()).with(csrf())
						.with(securityContext(initSecurityContext("ROLEREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("角色删除成功"));

	}

	private SecurityContext initSecurityContext(String permission) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, "x",
				Lists.newArrayList(new SimpleGrantedAuthority(permission))));
		return securityContext;
	}

}
