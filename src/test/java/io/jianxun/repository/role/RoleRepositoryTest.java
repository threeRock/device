package io.jianxun.repository.role;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.jianxun.domain.business.role.Role;
import io.jianxun.service.role.RolePredicates;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class RoleRepositoryTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String ROLE_NAME = "测试角色";

	@Autowired
	private RoleRepository roleRepository;

	// 初始化测试数据
	Role role;

	@Before
	public void setUp() {

		logger.debug("%s", "角色模型测试");
		assertThat(roleRepository).isNotNull();
		// init role
		role = new Role();
		role.setName(ROLE_NAME);
		role.setPermissions(Lists.newArrayList("ROLELIST", "ROLECREATE"));
		roleRepository.save(role);
	}

	@Test
	public void should_find_a_role_by_name() {
		Role findRole = roleRepository.findActiveOne(RolePredicates.namePredicate(ROLE_NAME));
		assertThat(findRole).isNotNull();
		assertThat(findRole.getName()).isEqualTo(ROLE_NAME);
		assertThat(findRole.isActive()).isEqualTo(true);
		assertThat(findRole.getPermissions().size()).isEqualTo(2);
		assertThat(findRole.getPermissions()).contains("ROLELIST");
		assertThat(findRole.getPermissions()).contains("ROLECREATE");
		assertThat(findRole.getPermissions()).doesNotContain("xx");
	}

}
