package io.jianxun.service.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jianxun.domain.business.role.Role;
import io.jianxun.service.AbstractBaseService;

@Service
public class RoleService extends AbstractBaseService<Role> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
