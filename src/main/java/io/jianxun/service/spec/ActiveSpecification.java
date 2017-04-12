package io.jianxun.service.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.jianxun.domain.AbstractBusinessEntity;

public class ActiveSpecification<T extends AbstractBusinessEntity> {

	public static Specification<AbstractBusinessEntity> activeSpec() {
		return new Specification<AbstractBusinessEntity>() {
			@Override
			public Predicate toPredicate(Root<AbstractBusinessEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(AbstractBusinessEntity.ACTIVE), true);
			}

		};
	}
}
