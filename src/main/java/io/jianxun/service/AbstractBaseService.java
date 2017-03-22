package io.jianxun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import io.jianxun.domain.business.BusinessEntity;
import io.jianxun.repository.BusinessBaseRepository;

@Transactional(readOnly = true)
public abstract class AbstractBaseService<T extends BusinessEntity> {

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

	public T findActiveOne(Long id) {
		Assert.notNull("id", messageSourceService.getMessage("id.notnull"));
		T entity = findOne(id);
		if (entity == null)
			throw new BusinessException(messageSourceService.getMessage("entity.isnull"));
		if (!entity.isActive())
			throw new BusinessException(messageSourceService.getMessage("entity.notactive", new Object[] { entity }));
		return entity;
	}

	@Transactional(readOnly = false)
	public T save(T entity) {
		return this.repository.save(entity);
	}

}
