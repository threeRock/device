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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.user.User;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private UserService userService;

	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	// test data
	private User testUser1, testUser2;

	@Before
	public void setUp() {
		userService.deleteAll();
	}

	@Test
	public void should_save_success() {
		testUser1 = new User();
		testUser1.setUsername("TT");
		userService.save(testUser1);
		assertThat(testUser1).isNotNull();
		assertThat(testUser1.getCreatedDate()).isNotNull();
		assertThat(testUser1.getUsername()).isEqualTo("TT");
	}

	@Test(expected = BusinessException.class)
	public void should_activefalse_save_failure() {
		testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setActive(false);
		userService.save(testUser1);
	}

	@Test
	public void should_activefalse_save_failure1() {
		testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setActive(false);
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.notactive", new Object[] { testUser1 }));
		userService.save(testUser1);
	}

	@Test
	public void should_activefalse_batchsave_failure() {
		List<User> users = Lists.newArrayList();
		testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setActive(false);
		users.add(testUser1);
		User testUser2 = new User();
		testUser2.setUsername("TTT");
		testUser2.setActive(true);
		users.add(testUser2);
		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.notactive", new Object[] { testUser1 }));
		userService.save(users);
	}

	@Test
	public void find_active() {
		initData();
		List<User> user_db = userService.findActiveAll();
		assertThat(user_db).isNotNull();
		assertThat(user_db.size()).isEqualTo(1);
		assertThat(user_db).contains(testUser2);

		thrown.expect(BusinessException.class);
		thrown.expectMessage(localeMessageSourceService.getMessage("entity.isnull"));
		User searchUser = userService.findActiveOne(testUser1.getId());
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
		initData();
		long count = userService.countActiveAll();
		assertThat(count).isEqualTo(1);
		count = userService.countActiveAll(UserPredicates.usernamePredicate(testUser1.getUsername()));
		assertThat(count).isEqualTo(0);
		count = userService.countActiveAll(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void exists_active() {
		initData();
		boolean exist = userService.exists(UserPredicates.usernamePredicate(testUser1.getUsername()));
		assertThat(exist).isFalse();
		exist = userService.exists(UserPredicates.usernamePredicate(testUser2.getUsername()));
		assertThat(exist).isTrue();

	}

	private void initData() {
		testUser1 = new User();
		testUser1.setUsername("TT");
		testUser1.setActive(false);
		userService.directSave(testUser1);
		testUser2 = new User();
		testUser2.setUsername("TTT");
		testUser2.setActive(true);
		userService.save(testUser2);
	}

}
