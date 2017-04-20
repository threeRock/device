package io.jianxun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import io.jianxun.config.AuditorAwareImpl;
import io.jianxun.domain.business.User;
import io.jianxun.repository.BusinessBaseRepositoryImpl;
import io.jianxun.service.business.StorageProperties;

/*
设备管理
*/
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableJpaAuditing
@EnableSpringDataWebSupport
@EnableJpaRepositories(repositoryBaseClass = BusinessBaseRepositoryImpl.class)
public class DeviceApplication {

	@Bean
	public AuditorAware<User> auditorProvider() {
		return new AuditorAwareImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}
}
