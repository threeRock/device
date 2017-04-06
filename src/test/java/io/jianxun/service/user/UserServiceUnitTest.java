package io.jianxun.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jianxun.domain.business.user.User;
import io.jianxun.repository.user.UserRepository;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.web.dto.PasswordDto;
import io.jianxun.web.utils.CurrentLoginInfo;

public class UserServiceUnitTest {

	private static final String CURRENT_USER_ENCODE_PASSWOED = "encodepassword";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private UserRepository userRepository;

	@Mock
	private LocaleMessageSourceService messageSourceService;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private CurrentLoginInfo currentLoginInfo;

	@InjectMocks
	private UserService userService;

	User user, user1, loginUser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		loginUser = new User();
		loginUser.setUsername("currentuser");
		loginUser.setPassword(CURRENT_USER_ENCODE_PASSWOED);
	}

	@Test
	public void should_save_success() {
		User expertUser = new User();
		expertUser.setUsername("XXX");
		user = new User();
		user.setUsername("TT");
		when(userRepository.save(user)).thenReturn(expertUser);
		User saveUser = userService.save(user);
		assertThat(saveUser.getUsername()).isEqualTo("XXX");
	}

	@Test
	public void regester_user() {
		String expertPassword = "y";
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(expertPassword);
		User expertUser = new User();
		expertUser.setUsername("t");
		expertUser.setPassword("x");
		when(userRepository.save(any(User.class))).thenReturn(expertUser);
		User saveUser = userService.register(expertUser);
		assertThat(saveUser.getPassword()).isEqualTo(expertPassword);

		User exceptUser = new User();
		exceptUser.setId(1L);
		exceptUser.setUsername("exception");
		when(messageSourceService.getMessage(anyString(), anyObject())).thenReturn("message");
		thrown.expect(BusinessException.class);
		thrown.expectMessage("message");
		saveUser = userService.register(exceptUser);
	}

	@Test
	public void changeCurrentLoginUserPassword_loginUserIsNull() {
		when(currentLoginInfo.currentLoginUser()).thenReturn(null);
		when(messageSourceService.getMessage("user.loginUserIsNull")).thenReturn("loginUserisnull");
		thrown.expect(BusinessException.class);
		thrown.expectMessage("loginUserisnull");
		userService.changePassword(new PasswordDto());

	}

	@Test
	public void changeCurrentLoginUserPassword_encodedPasswordIsNull() {
		when(currentLoginInfo.currentLoginUser()).thenReturn(loginUser);
		when(messageSourceService.getMessage("user.encodedPasswordIsNull")).thenReturn("encodedPasswordIsNull");
		PasswordDto password = new PasswordDto();
		loginUser.setPassword(null);
		thrown.expect(BusinessException.class);
		thrown.expectMessage("encodedPasswordIsNull");
		userService.changePassword(password);
	}

	@Test
	public void changeCurrentLoginUserPassword_validateError() {
		when(currentLoginInfo.currentLoginUser()).thenReturn(loginUser);
		when(messageSourceService.getMessage("user.passwordValidateError")).thenReturn("validateError");
		PasswordDto password = new PasswordDto();
		password.setOldPassword("yyy");
		password.setNewPassword("xxx");
		thrown.expect(BusinessException.class);
		thrown.expectMessage("validateError");
		userService.changePassword(password);
	}

	@Test
	public void changeCurrentLoginUserPassword_success() {
		when(currentLoginInfo.currentLoginUser()).thenReturn(loginUser);
		when(bCryptPasswordEncoder.matches("yyy", CURRENT_USER_ENCODE_PASSWOED)).thenReturn(false);
		PasswordDto password = new PasswordDto();
		password.setOldPassword("yyy");
		password.setNewPassword("xxx");
		userService.changePassword(password);
		verify(bCryptPasswordEncoder, times(1)).encode("xxx");
		verify(userRepository).save(loginUser);
	}

}
