package io.jianxun.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
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

import io.jianxun.domain.business.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;

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
		User user = new User();
		user.setUsername("ttt");
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, "x",
				Lists.newArrayList(new SimpleGrantedAuthority("USERCHANGEPASSWROD"))));
		this.mockMvc
				.perform(post("/user/changepassword/current").param("oldPassword", "password").param("newPassword", "p")
						.with(csrf()).with(securityContext(securityContext)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(200));
	}

}
