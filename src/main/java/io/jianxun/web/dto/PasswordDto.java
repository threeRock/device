package io.jianxun.web.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 密码修改dto
 * 
 * @author tongtn
 *
 *         createDate: 2016-08-16
 */
public class PasswordDto {
	@NotNull(message = "{oldPassword.notNull}")
	@Length(min = 6, message = "{password.minlength}")
	private String oldPassword;
	@NotNull(message = "{newPassword.notNull}")
	@Length(min = 6, message = "{password.minlength}")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
