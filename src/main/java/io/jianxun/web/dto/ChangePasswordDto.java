package io.jianxun.web.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 密码修改dto
 * 
 * @author tongtn
 *
 *         createDate: 2016-08-16
 */
public class ChangePasswordDto {

	@NotNull(message = "{user.notnull}")
	private Long userId;

	private String userInfo;
	@NotNull(message = "{newPassword.notNull}")
	@Length(min = 6, message = "{password.minlength}")
	private String newPassword;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
