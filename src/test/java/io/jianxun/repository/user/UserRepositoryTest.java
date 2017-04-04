package io.jianxun.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.Predicate;

import io.jianxun.config.AuditorAwareImpl;
import io.jianxun.domain.business.user.QUser;
import io.jianxun.domain.business.user.User;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class UserRepositoryTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String USER_USERNAME = "User";
	private static final String ADMIN_USERNAME = "Admin";
	private static final String UNACTIVE_USERNAME = "Unactive";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	AuditorAwareImpl auditorAware;

	// 初始化测试数据
	User user, admin, unActiveUser;
	UsernamePasswordAuthenticationToken userAuth, adminAuth;

	@Before
	public void setUp() {

		logger.debug("%s", "测试");
		assertThat(userRepository).isNotNull();
		// init user
		user = new User();
		user.setUsername(USER_USERNAME);
		user.setPassword("x");
		user.setDisplayName(USER_USERNAME);

		admin = new User();
		admin.setUsername(ADMIN_USERNAME);
		admin.setPassword("x");
		admin.setDisplayName(ADMIN_USERNAME);

		unActiveUser = new User();
		unActiveUser.setUsername(UNACTIVE_USERNAME);
		unActiveUser.setPassword("x");
		unActiveUser.setDisplayName(UNACTIVE_USERNAME);
		unActiveUser.setActive(false);
		userRepository.save(Lists.newArrayList(user, admin, unActiveUser));

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

	@Test
	public void should_not_find_unactive_users() {
		List<User> users = userRepository.findActiveAll();
		assertThat(users.size()).isEqualTo(2);
		assertThat(users).contains(user, admin);
		users = userRepository.findAll();
		assertThat(users.size()).isEqualTo(3);
		assertThat(users).contains(user, admin, unActiveUser);
	}

	@Test
	public void find_active_users_page() {
		Page<User> users = userRepository.findActiveAll(usernamePredicate(USER_USERNAME), new PageRequest(0, 3));
		assertThat(users.getNumberOfElements()).isEqualTo(1);
		assertThat(users.getContent().get(0).getUsername()).isEqualTo(USER_USERNAME);
		users = userRepository.findActiveAll(usernamePredicate(UNACTIVE_USERNAME), new PageRequest(0, 10));
		assertThat(users.getNumberOfElements()).isEqualTo(0);
		users = userRepository.findAll(usernamePredicate(UNACTIVE_USERNAME), new PageRequest(0, 10));
		assertThat(users.getNumberOfElements()).isEqualTo(1);
		assertThat(users.getContent().get(0).getUsername()).isEqualTo(UNACTIVE_USERNAME);
	}

	@Test
	public void find_active_one() {
		User user = userRepository.findActiveOne(usernamePredicate(USER_USERNAME));
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo(USER_USERNAME);
		user = userRepository.findActiveOne(usernamePredicate(UNACTIVE_USERNAME));
		assertThat(user).isNull();
	}

	@Test
	public void count_active() {
		assertThat(userRepository.countActive()).isEqualTo(2);
		assertThat(userRepository.countActive(usernamePredicate(USER_USERNAME))).isEqualTo(1);
		assertThat(userRepository.count(usernamePredicate(UNACTIVE_USERNAME))).isEqualTo(1);
		assertThat(userRepository.countActive(usernamePredicate(UNACTIVE_USERNAME))).isEqualTo(0);
		assertThat(userRepository.countActive(usernamePredicate("unknow"))).isEqualTo(0);
	}

	private Predicate usernamePredicate(String username) {
		QUser user = QUser.user;
		return user.username.eq(username);
	}
}
