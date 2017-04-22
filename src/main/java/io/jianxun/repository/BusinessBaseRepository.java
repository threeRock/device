package io.jianxun.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 
 * @author tongtn
 *
 * @param <T>
 *            createDate: 2017-03-15
 */
@NoRepositoryBean
public interface BusinessBaseRepository<T extends AbstractBusinessEntity> extends JpaRepository<T, Long>,
		JpaSpecificationExecutor<T>, QueryDslPredicateExecutor<T>, QuerydslBinderCustomizer<EntityPathBase<T>> {

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

	/* (non-Javadoc)
	 * @see org.springframework.data.querydsl.binding.QuerydslBinderCustomizer#customize(org.springframework.data.querydsl.binding.QuerydslBindings, com.querydsl.core.types.EntityPath)
	 */
	@Override
	default void customize(QuerydslBindings bindings, EntityPathBase<T> root) {
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
	
	

}
