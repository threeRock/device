package io.jianxun.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.jianxun.domain.business.user.User;
import io.jianxun.repository.user.UserRepository;

public class UserServiceUnitTest {

	@Mock
	private UserRepository userRepository;

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
}
