package io.jianxun.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.jianxun.domain.business.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private User loginUser;

	@Before
	public void setUp() {
		loginUser = new User();
		loginUser.setId(1L);
		loginUser.setUsername("User");
		loginUser.setDisplayName("测试用户");
	}

	@Test
	public void mainReturn() throws Exception {

		this.mockMvc.perform(get("/main").with(user(loginUser))).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("马钢气体销售公司设备管理系统")));
	}

}
