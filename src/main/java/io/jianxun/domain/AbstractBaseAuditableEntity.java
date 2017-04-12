package io.jianxun.domain;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.jianxun.domain.business.User;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseAuditableEntity extends AbstractBaseEntity implements Auditable {

	private static final long serialVersionUID = 3042728613468697208L;

	@CreatedBy
	@ManyToOne
	private User createdBy;
	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedBy
	@ManyToOne
	private User lastModifieBy;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public User getCreatedBy() {
		return this.createdBy;
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public User getLastModifiedBy() {
		return this.lastModifieBy;
	}

	@Override
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifieBy = lastModifiedBy;
	}

}
