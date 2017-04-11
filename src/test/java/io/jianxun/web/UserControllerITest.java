package io.jianxun.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

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

import io.jianxun.domain.business.role.Role;
import io.jianxun.domain.business.user.User;
import io.jianxun.service.role.RoleService;
import io.jianxun.service.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITest {

	private static final String USERNAME = "TT";
	private static final String PASSWORD = "tttttt";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	private User user, loginUser, other;
	private Role userManageRoleInfo, roleManageRoleInfo;

	@Before
	public void setUp() {

		userManageRoleInfo = new Role();
		userManageRoleInfo.setName("用户管理角色");
		userManageRoleInfo.setPermissions(
				Lists.newArrayList("USERLIST", "USERCREATE", "USERMODIFY", "USERREMOVE", "USERCHANGEPASSWROD"));
		roleManageRoleInfo = new Role();
		roleManageRoleInfo.setName("角色管理角色");
		roleManageRoleInfo.setPermissions(Lists.newArrayList("ROLELIST", "ROLECREATE", "ROLEMODIFY", "ROLEREMOVE"));
		List<Role> roles = roleService.save(Lists.newArrayList(userManageRoleInfo, roleManageRoleInfo));

		user = new User();
		user.setUsername(USERNAME);
		user.setDisplayName(USERNAME);
		user.setPassword(PASSWORD);
		user.setRoles(roles);
		user = userService.register(user);
		loginUser = user;

		other = new User();
		other.setUsername(USERNAME + 1);
		other.setDisplayName(USERNAME + 1);
		other.setPassword(PASSWORD);
		other.setRoles(roles);
		other = userService.register(other);

	}

	/**
	 * 用户未登录直接跳转到登录页面
	 * 
	 * @throws Exception
	 */
	@Test
	public void unauthorized() throws Exception {
		this.mockMvc.perform(get("/user/page/")).andDo(print()).andExpect(status().is3xxRedirection());

	}

	@Test
	public void accessDenied() throws Exception {
		this.mockMvc
				.perform(get("/user/page/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

	}

	@Test
	public void page_success() throws Exception {
		this.mockMvc
				.perform(get("/user/page/").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST"))))
				.andDo(print()).andExpect(status().isOk());
	}

	// 重置密码
	@Test
	public void chang_password_form() throws Exception {

		this.mockMvc
				.perform(get("/user/changepassword/current").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/user/changepassword/current").with(user("userUser")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERCHANGEPASSWROD"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/changepassword"));
	}

	@Test
	public void change_password_save() throws Exception {
		this.mockMvc
				.perform(post("/user/changepassword/current").param("oldPassword", PASSWORD)
						.param("newPassword", "ppppppp").with(csrf())
						.with(securityContext(initSecurityContext("USERCHANGEPASSWROD"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));

		this.mockMvc
				.perform(post("/user/changepassword/current").param("oldPassword", "errorpassword")
						.param("newPassword", "p").with(csrf())
						.with(securityContext(initSecurityContext("USERCHANGEPASSWROD"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message").exists());

		this.mockMvc
				.perform(post("/user/changepassword/current").param("oldPassword", "").param("newPassword", "")
						.with(csrf()).with(securityContext(initSecurityContext("USERCHANGEPASSWROD"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message").exists());
	}

	@Test
	public void create_form() throws Exception {

		this.mockMvc
				.perform(get("/user/create").with(user("testUser").password("password")
						.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.statusCode").value(300));

		this.mockMvc
				.perform(get("/user/create").with(
						user("userUser").authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERCREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("user/form"))
				.andExpect(content().string(containsString("密码")));
	}

	@Test
	public void create_save() throws Exception {

		this.mockMvc
				.perform(post("/user/create").param("username", "tt").param("password", "x").with(csrf())
						.with(securityContext(initSecurityContext("USERCREATE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));
		this.mockMvc
				.perform(post("/user/create").param("username", "").param("password", "").with(csrf())
						.with(securityContext(initSecurityContext("USERCREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message").exists());
		// 验证重复登录名称
		this.mockMvc
				.perform(post("/user/create").param("username", USERNAME).param("password", "x").with(csrf())
						.with(securityContext(initSecurityContext("USERCREATE"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message")
						.value("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TT 用户名已存在，不能重复使用  <br />"));

	}

	@Test
	public void modify_form() throws Exception {

		this.mockMvc
				.perform(get("/user/modify/{id}", user.getId()).with(
						user("userUser").authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("USERMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(model().attributeExists("user"))
				.andExpect(view().name("user/form"));
	}

	@Test
	public void modify_save() throws Exception {

		this.mockMvc
				.perform(post("/user/modify").param("username", "tt").param("id", user.getId().toString()).with(csrf())
						.with(securityContext(initSecurityContext("USERMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("用户保存成功"));

		// 登录名为空 请求失败
		this.mockMvc
				.perform(post("/user/create").param("username", "").with(csrf())
						.with(securityContext(initSecurityContext("USERMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message").exists());

		// 验证ID和登录名称相同也可以保存
		this.mockMvc
				.perform(post("/user/modify").param("username", USERNAME).param("id", user.getId().toString())
						.with(csrf()).with(securityContext(initSecurityContext("USERMODIFY"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("用户保存成功"));
		// 验证ID不同登录名称相同不可以保存
		this.mockMvc
				.perform(post("/user/modify").param("username", USERNAME).param("id", other.getId().toString())
						.with(csrf()).with(securityContext(initSecurityContext("USERMODIFY"))))
				.andDo(print()).andExpect(status().is4xxClientError()).andExpect(jsonPath("$.message")
						.value("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TT 用户名已存在，不能重复使用  <br />"));

	}

	@Test
	public void delete_save() throws Exception {

		this.mockMvc
				.perform(get("/user/remove/{id}", user.getId()).with(csrf())
						.with(securityContext(initSecurityContext("USERREMOVE"))))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200))
				.andExpect(jsonPath("$.message").value("用户删除成功"));

	}

	// 管理员修改密码

	private SecurityContext initSecurityContext(String permission) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, "x",
				Lists.newArrayList(new SimpleGrantedAuthority(permission))));
		return securityContext;
	}

}
