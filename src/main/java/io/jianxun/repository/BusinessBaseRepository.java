package io.jianxun.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 
 * @author tongtn
 *
 * @param <T>
 *            createDate: 2017-03-15
 */
@NoRepositoryBean
public interface BusinessBaseRepository<T extends AbstractBusinessEntity>
		extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>, QueryDslPredicateExecutor<T> {

	List<T> findActiveAll();

	List<T> findActiveAll(Sort sort);

	List<T> findActiveAll(Predicate predicate, Sort sort);

	Page<T> findActiveAll(Predicate predicat, Pageable pageable);

	Page<T> findActiveAll(Pageable pageable);

	T findActiveOne(Long id);

	T findActiveOne(Predicate predicate);

	long countActive();

	long countActive(Predicate predicate);

	boolean existsActive(Predicate predicate);

	boolean existsActive(Long id);

}
