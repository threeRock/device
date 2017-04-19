package io.jianxun.web;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.User;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractIT {

	protected Depart root;

	protected User admin;

	protected User testUser;

	@Before
	public void setUp() {
		try {
			root = departService.findActiveOne(1L);
		} catch (Exception e) {
			root = null;
		}
		if (root == null)
			root = departService.initRoot();
		try {
			admin = userService.findActiveOne(1L);
		} catch (Exception e) {
			admin = null;
		}
		if (admin == null)
			admin = userService.createAdminIfInit(root);

		testUser = new User();
		testUser.setUsername("测试用户");
		testUser.setPassword("ppppppp");
		testUser.setDepart(root);
		testUser.setDisplayName("测试用户");
		userService.register(testUser);

	}

	protected SecurityContext initSecurityContext(String permission) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(testUser, "x",
				Lists.newArrayList(new SimpleGrantedAuthority(permission))));
		return securityContext;
	}

	protected SecurityContext initSecurityContext(User loginUser, String permission) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, "x",
				Lists.newArrayList(new SimpleGrantedAuthority(permission))));
		return securityContext;
	}

	@Autowired
	protected UserService userService;
	@Autowired
	protected DepartService departService;

}
