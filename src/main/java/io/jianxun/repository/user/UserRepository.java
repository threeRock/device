package io.jianxun.repository.user;

import java.util.Optional;

import io.jianxun.domain.business.user.User;
import io.jianxun.repository.BusinessBaseRepository;

public interface UserRepository extends BusinessBaseRepository<User> {

	Optional<User> findByUsernameAndActive(String username,boolean active);

}
