package io.jianxun.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.jianxun.config.SecurityConfig;
import io.jianxun.service.user.UserService;
import io.jianxun.web.user.UserController;
import io.jianxun.web.utils.Utils;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerUT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private Utils utils;

	@Before
	public void setUp() {
	}

	@Test
	public void get_page_should_unauthorized() throws Exception {
		// unauthorized
		this.mockMvc.perform(get("/user/page/")).andDo(print()).andExpect(status().isUnauthorized());

	}

	@Test
	public void get_page_should_forbidden() throws Exception {
		// forbidden
		this.mockMvc
				.perform(get("/user/page/")
						.with(user("testUser").authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(""))))
				.andDo(print()).andExpect(status().isForbidden());
	}

	@Test
	public void get_page_should_success() throws Exception {
		// 没有配置 MethodSecurityInterceptor
		this.mockMvc
				.perform(get("/user/page/").with(authentication(new UsernamePasswordAuthenticationToken("user",
						"password", AuthorityUtils.commaSeparatedStringToAuthorityList("USERLIST")))))
				.andDo(print()).andExpect(status().isOk());
	}

}
