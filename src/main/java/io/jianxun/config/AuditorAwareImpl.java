package io.jianxun.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jianxun.domain.business.user.User;

public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public User getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return ((User) authentication.getPrincipal());
	}

}
