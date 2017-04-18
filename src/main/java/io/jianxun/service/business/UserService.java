package io.jianxun.service.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Role;
import io.jianxun.domain.business.User;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.web.dto.ChangePasswordDto;
import io.jianxun.web.dto.ResetPasswordDto;
import io.jianxun.web.utils.CurrentLoginInfo;

@Service
@Transactional(readOnly = true)
public class UserService extends AbstractBaseService<User> implements UserDetailsService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CurrentLoginInfo currentLoginInfo;

	@Autowired
	private RoleService roleService;

	/**
	 * 修改当前登录用户密码
	 * 
	 * @param newPassword
	 */
	@Transactional(readOnly = false)
	public void resetPassword(ResetPasswordDto password) {
		User current = currentLoginInfo.currentLoginUser();
		if (current == null)
			throw new BusinessException(messageSourceService.getMessage("loginUser.IsNull"));
		logger.info("操作人:{},操作内容:修改登录用户{}密码", new Object[] { current, current });
		if (validateOldePassword(password.getOldPassword(), currentLoginInfo.currentLoginUser().getPassword())) {
			User u = this.findActiveOne(current.getId());
			u.setPassword(bCryptPasswordEncoder.encode(password.getNewPassword()));
			save(u);
			return;
		}
		throw new BusinessException(messageSourceService.getMessage("user.passwordValidateError"));

	}

	@Transactional(readOnly = false)
	public void changePassword(ChangePasswordDto password) {
		User selected = this.findActiveOne(password.getUserId());
		if (selected == null)
			throw new BusinessException(messageSourceService.getMessage("user.notfound"));
		logger.info("操作人:{},操作内容:修改登录用户{}密码", new Object[] { currentLoginInfo.currentLoginUser(), selected });
		selected.setPassword(bCryptPasswordEncoder.encode(password.getNewPassword()));
		save(selected);
		return;

	}

	/**
	 * 验证密码是否匹配
	 * 
	 * @param rawPassword
	 *            密码明文
	 * @param encodedPassword
	 *            加密后密码
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
		logger.debug("操作人:{},操作内容:修改用户{}密码", new Object[] { currentLoginInfo.currentLoginUser(), user });
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
			return save(user);
		}
		logger.debug("注册操作失败，用户 {} 已经注册", user);
		throw new BusinessException(
				messageSourceService.getMessage("user.isRegistered", new Object[] { user.getUsername() }));
	}

	/**
	 * 通过登录名加载用户
	 */
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User loginUser = this.repository.findActiveOne(UserPredicates.usernamePredicate(username));
		if (loginUser == null)
			throw new BusinessException(messageSourceService.getMessage("user.notfound"));
		return loginUser;

	}

	/**
	 * 验证用户名是否重复
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	public boolean validateUsernameUnique(String username, Long id) {
		Long count = countActiveAll(UserPredicates.usernameAndIdNotPredicate(username, id));
		return count == 0;

	}

	/**
	 * 验证用户是否是超级管理员
	 * 
	 * @param id
	 * @param model
	 */
	public boolean validateIsSuperAdmin(User user) {
		if (user != null && user.getId() != null)
			return 1L == user.getId();
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public <S extends User> S save(S user) {
		if (validateIsSuperAdmin(user))
			throw new BusinessException(messageSourceService.getMessage("cannot.modify.superadmin"));
		return super.save(user);
	}

	@Override
	@Transactional(readOnly = false)
	public User delete(User user) {
		if (validateIsSuperAdmin(user))
			throw new BusinessException(messageSourceService.getMessage("cannot.modify.superadmin"));
		return super.delete(user);
	}

	@Transactional(readOnly = false)
	public User createAdminIfInit(Depart depart) {
		if (this.repository.findAll().isEmpty())
			return initAdminUser(depart);
		return this.repository.findActiveOne(1L);

	}

	private User initAdminUser(Depart root) {
		logger.debug("创建超级管理员用户");
		Role role = roleService.createSuperRole();
		if (role == null)
			throw new BusinessException(messageSourceService.getMessage("user.admininit.error"));
		User user = new User();
		user.setUsername(User.SUPER_ADMIN_USERNAME);
		user.setDisplayName(User.SUPER_ADMIN_DISPLAYNAME);
		user.setPassword(User.SUPER_ADMIN_PASSWORD);
		user.setDepart(root);
		user.setRoles(Lists.newArrayList(role));
		return this.register(user);
	}
}
