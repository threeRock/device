package io.jianxun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.jianxun.domain.AbstractBaseEntity;

/**
 * 
 * @author tongtn
 *
 * @param <T>
 *            createDate: 2017-03-15
 */
@NoRepositoryBean
public interface BusinessBaseRepository<T extends AbstractBaseEntity>
		extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>, QueryDslPredicateExecutor<T> {

}
