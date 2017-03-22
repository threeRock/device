package io.jianxun.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.jianxun.config.AuditorAwareImpl;
import io.jianxun.domain.business.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String USER_USERNAME = "User";
	private static final String ADMIN_USERNAME = "Admin";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	AuditorAwareImpl auditorAware;

	// 初始化测试数据
	User user, admin;
	UsernamePasswordAuthenticationToken userAuth, adminAuth;

	@Before
	public void setUp() {

		logger.debug("%s", "测试");
		assertThat(userRepository).isNotNull();
		// init user
		user = new User();
		user.setUsername(USER_USERNAME);
		user.setPassowrd("x");
		user.setDisplayName(USER_USERNAME);

		admin = new User();
		admin.setUsername(ADMIN_USERNAME);
		user.setPassowrd("x");
		admin.setDisplayName(ADMIN_USERNAME);
		userRepository.save(Lists.newArrayList(user, admin));

		// init auth

		userAuth = new UsernamePasswordAuthenticationToken(user, "x",
				Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER")));
		adminAuth = new UsernamePasswordAuthenticationToken(admin, "x",
				Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMIN")));
	}

	@Test
	public void should_find_a_user_by_name() {
		User findUser = userRepository.findByUsernameAndActive(USER_USERNAME, true).get();
		assertThat(findUser).isNotNull();
		assertThat(findUser.getUsername()).isEqualTo(USER_USERNAME);
		assertThat(findUser.isActive()).isEqualTo(true);
	}

	@Test
	public void auditUserCreation() {
		assertThat(this.auditorAware).isNotNull();
		SecurityContextHolder.getContext().setAuthentication(userAuth);
		assertThat(this.auditorAware.getCurrentAuditor()).isNotNull();
		User custom = new User();
		custom.setUsername("t");

		custom = userRepository.save(custom);

		assertThat(custom.getCreatedBy()).isNotNull();
		assertThat(custom.getCreatedBy()).isEqualTo(user);
		assertThat(custom.getCreatedDate()).isNotNull();
		assertThat(custom.getLastModifiedDate()).isNotNull();

	}

}
