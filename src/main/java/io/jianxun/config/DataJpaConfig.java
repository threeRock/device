package io.jianxun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import io.jianxun.domain.business.user.User;

@Configuration
@EnableJpaAuditing
@EnableSpringDataWebSupport
public class DataJpaConfig {

	@Bean
	public AuditorAware<User> auditorProvider() {
		return new AuditorAwareImpl();
	}

}
