package io.jianxun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.jianxun.repository.BusinessBaseRepositoryImpl;

/*
设备管理
*/
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BusinessBaseRepositoryImpl.class)
public class DeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}
}
