package io.jianxun.web.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.User;

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
		try {
			return ((User) authentication.getPrincipal());
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 验证当前登录用户单位查询权限
	 */
	public boolean validateCurrentUserDepart(Depart depart) {
		User user = currentLoginUser();
		if (user != null) {
			Depart userDepart = user.getDepart();
			if (userDepart.isRoot())
				return true;
			if (depart.isRoot())
				return false;
			String userDepartLevelCode = userDepart.getLevelCode();
			String departLevelCode = depart.getLevelCode();
			if (StringUtils.isBlank(userDepartLevelCode) || StringUtils.isBlank(departLevelCode))
				return false;
			return departLevelCode.startsWith(userDepartLevelCode) || departLevelCode.equals(departLevelCode);

		}
		return false;
	}

}
