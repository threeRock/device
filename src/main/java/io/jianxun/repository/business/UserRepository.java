package io.jianxun.repository.business;

import java.util.Optional;

import io.jianxun.domain.business.User;
import io.jianxun.repository.BusinessBaseRepository;

public interface UserRepository extends BusinessBaseRepository<User> {

	Optional<User> findByUsernameAndActive(String username,boolean active);

}
