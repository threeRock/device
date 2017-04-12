package io.jianxun.domain;

import java.time.LocalDateTime;

import io.jianxun.domain.business.User;

public interface Auditable {
	
	/**
	 * Returns the user who created this entity.
	 * 
	 * @return the createdBy
	 */
	User getCreatedBy();

	/**
	 * Sets the user who created this entity.
	 * 
	 * @param createdBy the creating entity to set
	 */
	void setCreatedBy(final User createdBy);

	/**
	 * Returns the creation date of the entity.
	 * 
	 * @return the createdDate
	 */
	LocalDateTime getCreatedDate();

	/**
	 * Sets the creation date of the entity.
	 * 
	 * @param creationDate the creation date to set
	 */
	void setCreatedDate(final LocalDateTime creationDate);

	/**
	 * Returns the user who modified the entity lastly.
	 * 
	 * @return the lastModifiedBy
	 */
	User getLastModifiedBy();

	/**
	 * Sets the user who modified the entity lastly.
	 * 
	 * @param lastModifiedBy the last modifying entity to set
	 */
	void setLastModifiedBy(final User lastModifiedBy);

	/**
	 * Returns the date of the last modification.
	 * 
	 * @return the lastModifiedDate
	 */
	LocalDateTime getLastModifiedDate();

	/**
	 * Sets the date of the last modification.
	 * 
	 * @param lastModifiedDate the date of the last modification to set
	 */
	void setLastModifiedDate(final LocalDateTime lastModifiedDate);

}
