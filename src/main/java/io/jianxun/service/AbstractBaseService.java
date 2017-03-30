package io.jianxun.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.AbstractBusinessEntity;
import io.jianxun.repository.BusinessBaseRepository;
import io.jianxun.service.spec.ActiveSpecification;

@Transactional(readOnly = true)
public abstract class AbstractBaseService<T extends AbstractBusinessEntity> {

	@Autowired
	protected BusinessBaseRepository<T> repository;
	@Autowired
	private LocaleMessageSourceService messageSourceService;

	/**
	 * 读取单个对象
	 * 
	 * @param id
	 * @return
	 */
	public T findOne(Long id) {
		return repository.findOne(id);
	}

	/**
	 * 单个可用对象
	 * 
	 * @param id
	 * @return
	 */
	public T findActiveOne(Long id) {
		Assert.notNull("id", messageSourceService.getMessage("id.notnull"));
		T entity = findOne(id);
		if (entity == null)
			throw nullEntityExcepiton();
		if (!isActive(entity))
			throw notActiveExcepiton(entity);
		return entity;
	}

	/**
	 * 单个对象保存
	 * 
	 * @param entity
	 * @return
	 */

	@Transactional(readOnly = false)
	public T save(T entity) {
		if (isActive(entity))
			return this.repository.save(entity);
		throw notActiveExcepiton(entity);
	}

	/**
	 * 批量保存 验证是否为可用状态
	 * 
	 * @param entities
	 * @return
	 */
	@Transactional(readOnly = false)
	public Collection<T> save(Collection<T> entities) {
		for (T entity : entities) {
			save(entity);
		}
		return entities;
	}

	/**
	 * 批量保存(不验证是否为可用)
	 * 
	 * @param entities
	 * @return
	 */
	public Collection<T> batchSave(Collection<T> entities) {
		return this.repository.save(entities);
	}

	/**
	 * 获取可用
	 * 
	 * @return
	 */
	public List<T> findActiveAll() {
		return this.repository.findAll(activeSpec());
	}

	public long countActiveAll() {
		return this.repository.count(activeSpec());
	}

	public List<T> findAll() {
		return this.repository.findAll();
	}

	public long count() {
		return this.repository.count();
	}

	public Page<T> findActivePage(Pageable pageable) {
		return this.repository.findAll(activeSpec(), pageable);
	}

	public Page<T> findByPage(Predicate predicate, Pageable pageable) {
		return this.repository.findAll(predicate, pageable);
	}

	/**
	 * 是否为可用对象
	 * 
	 * @param entity
	 * @return
	 */
	public boolean isActive(T entity) {
		return entity.isActive();
	}

	@SuppressWarnings("unchecked")
	private Specification<T> activeSpec() {
		return (Specification<T>) ActiveSpecification.activeSpec();
	}

	private BusinessException nullEntityExcepiton() {
		return new BusinessException(messageSourceService.getMessage("entity.isnull"));
	}

	private BusinessException notActiveExcepiton(T entity) {
		return new BusinessException(messageSourceService.getMessage("entity.notactive", new Object[] { entity }));
	}

}
