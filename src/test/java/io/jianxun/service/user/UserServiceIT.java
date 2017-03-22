package io.jianxun.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.jianxun.domain.business.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

	@Autowired
	private UserService userService;

	// test data
	private User testUser1;

	@Test
	public void should_save_success() {
		testUser1 = new User();
		testUser1.setUsername("TT");
		userService.save(testUser1);
		assertThat(testUser1).isNotNull();
		assertThat(testUser1.getCreatedDate()).isNotNull();
		assertThat(testUser1.getUsername()).isEqualTo("TT");
	}

}
