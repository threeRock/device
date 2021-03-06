package io.jianxun.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.User;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.service.business.DepartService;
import io.jianxun.service.business.UserPredicates;
import io.jianxun.service.business.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceITest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private UserService userService;
	@Autowired
	private DepartService departService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	// test data
	private User testUser1, testUser2;

	@Before
	public void setUp() {
		userService.createAdminIfInit(departService.initRoot());
		initData();
	}

	@Test
	@Rollback
	public void should_save_success() {
		User testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setDepart(departService.initRoot());
		userService.save(testUser1);
		assertThat(testUser1).isNotNull();
		assertThat(testUser1.getCreatedDate()).isNotNull();
		assertThat(testUser1.getUsername()).isEqualTo("TT");
	}

	@Test(expected = BusinessException.class)
	public void should_activefalse_save_failure() {
		User testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setDepart(departService.initRoot());
		testUser1.setActive(false);
		userService.save(testUser1);
	}

	@Test
	public void should_activefalse_save_failure1() {
		User testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setActive(false);
		testUser1.setDepart(departService.initRoot());
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.notactive", new Object[] { testUser1 }));
		userService.save(testUser1);
	}

	@Test
	public void should_activefalse_batchsave_failure() {
		List<User> users = Lists.newArrayList();
		User testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setDepart(departService.initRoot());
		testUser1.setActive(false);
		users.add(testUser1);
		User testUser2 = new User();
		testUser2.setUsername("TTT");
		testUser2.setActive(true);
		testUser2.setDepart(departService.initRoot());
		users.add(testUser2);
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.notactive", new Object[] { testUser1 }));
		userService.save(users);
	}

	@Test
	public void find_active() {
		List<User> user_db = userService.findActiveAll();
		assertThat(user_db).isNotNull();
		assertThat(user_db).contains(testUser2);

		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.isnull"));
		User searchUser = userService.findActiveOne(testUser1.getId());
		searchUser = userService.findOne(testUser1.getId());
		assertThat(searchUser).isNotNull();
		assertThat(searchUser.isActive()).isFalse();
		searchUser = userService.findActiveOne(testUser2.getId());
		assertThat(searchUser).isEqualTo(testUser2);

		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.isnull"));
		searchUser = userService.findActiveOne(UserPredicates.usernamePredicate(testUser1.getUsername()));
		searchUser = userService.findActiveOne(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(searchUser).isEqualTo(testUser2);

		Page<User> users = userService.findActivePage(UserPredicates.usernamePredicate(testUser1.getUsername()),
				new PageRequest(0, 2));
		assertThat(users.hasContent()).isFalse();
		users = userService.findActivePage(UserPredicates.usernamePredicate(testUser2.getUsername()),
				new PageRequest(0, 2));
		assertThat(users.hasContent()).isFalse();
		assertThat(users.getNumberOfElements()).isEqualTo(1);
		assertThat(users.getContent().get(0).getUsername()).isEqualTo(testUser2.getUsername());

		users = userService.findActivePage(new PageRequest(0, 2));
		assertThat(users.hasContent()).isTrue();
		assertThat(users.getNumberOfElements()).isEqualTo(1);
		assertThat(users.getContent().get(0).getUsername()).isEqualTo(testUser2.getUsername());

	}

	@Test
	public void count_active() {
		long count = userService.countActiveAll(UserPredicates.usernamePredicate(testUser1.getUsername()));
		assertThat(count).isEqualTo(0);
		count = userService.countActiveAll(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void exists_active() {
		boolean exist = userService.exists(UserPredicates.usernamePredicate(testUser1.getUsername()));
		assertThat(exist).isFalse();
		exist = userService.exists(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(exist).isTrue();

	}

	@Test
	@Rollback
	public void delete() {

		User u = userService.findActiveOne(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(u).isNotNull();
		userService.delete(u.getId());
		assertThat(userService.findOne(u.getId())).isNotNull();
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.isnull"));
		userService.findActiveOne(u.getId());

		u = new User();
		u.setUsername("T");
		u.setDisplayName("T");
		userService.save(u);
		User searchUser = userService.findActiveOne(UserPredicates.usernamePredicate("T"));
		assertThat(searchUser.getUsername()).isEqualTo("T");
		userService.delete(searchUser);
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.isnull"));
		userService.findActiveOne(UserPredicates.usernamePredicate("T"));
	}

	@Test
	public void register() {
		User testUser1 = new User();
		testUser1.setUsername("test");
		testUser1.setPassword("xxxxxxxx");
		testUser1.setDepart(departService.initRoot());
		userService.register(testUser1);
		User searchUser = userService.findActiveOne(UserPredicates.usernamePredicate("test"));
		assertThat(searchUser.getUsername()).isEqualTo("test");
		assertThat(userService.validateOldePassword("xxxxxxxx", searchUser.getPassword()));

		thrown.expect(BusinessException.class);
		thrown.expectMessage(
				localeMessageSourceService.getMessage("user.isRegistered", new Object[] { searchUser.getUsername() }));
		userService.register(searchUser);
	}

	private void initData() {
		List<User> users = userService.findActiveAll();
		for (User user : users) {
			if (userService.validateIsSuperAdmin(user))
				continue;
			userService.delete(user);
		}
		testUser1 = new User();
		testUser1.setUsername("TT004");
		testUser1.setActive(false);
		testUser1.setDepart(departService.initRoot());
		testUser1 = userService.directSave(testUser1);

		testUser2 = new User();
		testUser2.setUsername("TTT887");
		testUser2.setActive(true);
		testUser2.setDepart(departService.initRoot());
		testUser2 = userService.save(testUser2);
	}

}
