package io.jianxun.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
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

public class UserServiceUnitTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private UserRepository userRepository;

	@Mock
	private LocaleMessageSourceService messageSourceService;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private UserService userService;

	User user, user1;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
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

}
