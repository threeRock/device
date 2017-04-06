package io.jianxun.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import io.jianxun.domain.business.user.User;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.web.dto.PasswordDto;
import io.jianxun.web.utils.CurrentLoginInfo;

@Service
public class UserService extends AbstractBaseService<User> implements UserDetailsService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CurrentLoginInfo currentLoginInfo;

	/**
	 * 修改当前登录用户密码
	 * 
	 * @param newPassword
	 */
	@Transactional(readOnly = false)
	public void changePassword(PasswordDto password) {
		logger.info("操作人:{},操作内容:修改登录用户{}密码",
				new Object[] { currentLoginInfo.currentLoginUser(), currentLoginInfo.currentLoginUser() });
		User current = currentLoginInfo.currentLoginUser();
		if (current == null)
			throw new BusinessException(messageSourceService.getMessage("loginUser.IsNull"));
		if (validateOldePassword(password.getOldPassword(), currentLoginInfo.currentLoginUser().getPassword())) {
			current.setPassword(bCryptPasswordEncoder.encode(password.getNewPassword()));
			super.save(current);
		}
		throw new BusinessException(messageSourceService.getMessage("user.passwordValidateError"));

	}

	/**
	 * 验证密码是否匹配
	 * 
	 * @param rawPassword  密码明文
	 * @param encodedPassword   加密后密码
	 * @return
	 */
	public boolean validateOldePassword(String rawPassword, String encodedPassword) {
		if (StringUtils.isEmpty(encodedPassword)) {
			logger.debug("密码为空,验证失败");
			throw new BusinessException(messageSourceService.getMessage("user.encodedPasswordIsNull"));
		}
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 * @param newPassword
	 */
	@Transactional(readOnly = false)
	public void changePassword(User user, String newPassword) {
		logger.debug("操作人:{},操作内容:修改用户{}密码",
				new Object[] { currentLoginInfo.currentLoginUser(), currentLoginInfo.currentLoginUser() });
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		save(user);

	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public User register(User user) {
		logger.debug("操作人:{},操作内容:注册用户{}", new Object[] { currentLoginInfo.currentLoginUser(), user });
		if (user.isNew()) {
			// 密码加密
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			return super.save(user);
		}
		logger.debug("注册操作失败，用户 {} 已经注册", user);
		throw new BusinessException(
				messageSourceService.getMessage("user.isRegistered", new Object[] { user.getUsername() }));
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.repository.findActiveOne(UserPredicates.usernamePredicate(username));
	}

}
