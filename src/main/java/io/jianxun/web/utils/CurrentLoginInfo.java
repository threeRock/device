package io.jianxun.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jianxun.domain.business.user.User;

@Component
public class CurrentLoginInfo {

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	public User currentLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		return ((User) authentication.getPrincipal());
	}

}
