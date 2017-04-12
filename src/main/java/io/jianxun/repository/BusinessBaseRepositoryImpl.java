package io.jianxun.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;

import io.jianxun.domain.AbstractBusinessEntity;

@NoRepositoryBean
public class BusinessBaseRepositoryImpl<T extends AbstractBusinessEntity> extends QueryDslJpaRepository<T, Long>
		implements BusinessBaseRepository<T> {

	private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

	private final EntityPath<T> path;
	private final PathBuilder<T> builder;

	public BusinessBaseRepositoryImpl(JpaEntityInformation<T, Long> entityInformation, EntityManager entityManager) {
		this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
	}

	public BusinessBaseRepositoryImpl(JpaEntityInformation<T, Long> entityInformation, EntityManager entityManager,
			EntityPathResolver resolver) {
		super(entityInformation, entityManager, resolver);

		this.path = resolver.createPath(entityInformation.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
	}

	@Override
	public List<T> findActiveAll() {
		return findAll(getActivePredicate(null));
	}

	@Override
	public List<T> findActiveAll(Sort sort) {
		return findAll(getActivePredicate(null), sort);
	}

	@Override
	public List<T> findActiveAll(Predicate predicate, Sort sort) {
		return findAll(getActivePredicate(predicate), sort);
	}

	@Override
	public Page<T> findActiveAll(Predicate predicat, Pageable pageable) {
		return findAll(getActivePredicate(predicat), pageable);
	}

	@Override
	public Page<T> findActiveAll(Pageable pageable) {
		return findAll(getActivePredicate(null), pageable);
	}

	@Override
	public T findActiveOne(Long id) {
		T t = findOne(id);
		if (!t.isActive())
			return null;
		return t;
	}

	@Override
	public T findActiveOne(Predicate predicate) {
		return findOne(getActivePredicate(predicate));
	}

	@Override
	public long countActive() {
		return count(getActivePredicate(null));
	}

	@Override
	public long countActive(Predicate predicate) {
		return count(getActivePredicate(predicate));
	}

	@Override
	public boolean existsActive(Predicate predicate) {
		return exists(getActivePredicate(predicate));
	}

	@Override
	public boolean existsActive(Long id) {
		return existsActive(getIdPredicate(id));
	}

	private Predicate getActivePredicate(Predicate predicate) {
		BooleanPath activePath = builder.getBoolean(AbstractBusinessEntity.ACTIVE);
		BooleanExpression activeExpression = activePath.eq(true);
		if (predicate != null)
			activeExpression = activeExpression.and(predicate);
		return activeExpression;
	}

	private Predicate getIdPredicate(long id) {
		SimplePath<Long> idPath = builder.getSimple(AbstractBusinessEntity.ID_NAME, Long.class);
		return idPath.eq(id);
	}

}
