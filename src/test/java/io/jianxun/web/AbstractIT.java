package io.jianxun.web;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.User;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.UserService;

public abstract class AbstractIT {

	protected Depart root;

	protected User admin;

	@Before
	public void setUp() {

		root = departService.initRoot();
		admin = userService.createAdminIfInit(root);

	}

	protected SecurityContext initSecurityContext(String permission) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(admin, "x",
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
