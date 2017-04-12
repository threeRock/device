package io.jianxun.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.querydsl.core.types.Predicate;

import io.jianxun.domain.AbstractBusinessEntity;
import io.jianxun.repository.BusinessBaseRepository;

@Transactional(readOnly = true)
public abstract class AbstractBaseService<T extends AbstractBusinessEntity> {

	@Autowired
	protected BusinessBaseRepository<T> repository;
	@Autowired
	protected LocaleMessageSourceService messageSourceService;

	/**
	 * 单个可用对象
	 * 
	 * @param id
	 * @return
	 */
	public T findActiveOne(Long id) {
		Assert.notNull("id", messageSourceService.getMessage("id.notnull"));
		T entity = repository.findActiveOne(id);
		entityIsNullAndThrowExcption(entity);
		return entity;
	}

	public T findOne(Long id) {
		Assert.notNull("id", messageSourceService.getMessage("id.notnull"));
		return this.repository.findOne(id);
	}

	public T findActiveOne(Predicate predicate) {
		T entity = this.repository.findActiveOne(predicate);
		entityIsNullAndThrowExcption(entity);
		return entity;
	}

	public long countActiveAll() {
		return this.repository.countActive();
	}

	public long countActiveAll(Predicate predicate) {
		return this.repository.countActive(predicate);
	}

	public boolean exists(Predicate predicate) {
		return this.repository.existsActive(predicate);
	}

	public boolean exists(Long id) {
		return this.repository.exists(id);
	}

	/**
	 * 获取可用
	 * 
	 * @return
	 */
	public List<T> findActiveAll() {
		return this.repository.findActiveAll();
	}

	public List<T> findActiveAll(Predicate predicate, Sort sort) {
		return this.repository.findActiveAll(predicate, sort);
	}

	public List<T> findActiveAll(Sort sort) {
		return this.repository.findActiveAll(sort);
	}

	public Page<T> findActivePage(Pageable pageable) {
		return this.repository.findActiveAll(pageable);
	}

	public Page<T> findActivePage(Predicate predicate, Pageable pageable) {
		return this.repository.findActiveAll(predicate, pageable);
	}

	/**
	 * 单个对象保存
	 * 
	 * @param entity
	 * @return
	 */

	@Transactional(readOnly = false)
	public <S extends T> S save(S entity) {
		if (isActive(entity))
			return this.repository.save(entity);
		throw notActiveExcepiton(entity);
	}

	/**
	 * 不验证是否可用直接保存
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public <S extends T> S directSave(S entity) {
		return this.repository.save(entity);
	}

	/**
	 * 批量保存 验证是否为可用状态
	 * 
	 * @param entities
	 * @return
	 */
	@Transactional(readOnly = false)
	public <S extends T> List<S> save(Iterable<S> entities) {
		List<S> result = new ArrayList<S>();
		for (S entity : entities) {
			result.add(save(entity));
		}
		return result;
	}

	@Transactional(readOnly = false)
	public T delete(Long id) {
		Assert.notNull("id", messageSourceService.getMessage("id.notnull"));
		T entity = repository.findActiveOne(id);
		entityIsNullAndThrowExcption(entity);
		return delete(entity);
	}

	@Transactional(readOnly = false)
	public T delete(T entity) {
		if (isActive(entity)) {
			entity.setActive(false);
			return this.repository.save(entity);
		}
		throw notActiveExcepiton(entity);
	}

	@Transactional(readOnly = false)
	public void delete(Iterable<? extends T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
	}

	@Transactional(readOnly = false)
	public void deleteAll() {
		for (T element : findActiveAll()) {
			delete(element);
		}
	}

	@Transactional(readOnly = false)
	public void directDelete(T entity) {
		this.repository.delete(entity);
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

	private void entityIsNullAndThrowExcption(T entity) {
		if (null == entity)
			throw nullEntityExcepiton();
	}

	private BusinessException nullEntityExcepiton() {
		return new BusinessException(messageSourceService.getMessage("entity.isnull"));
	}

	private BusinessException notActiveExcepiton(T entity) {
		return new BusinessException(messageSourceService.getMessage("entity.notactive", new Object[] { entity }));
	}

}
