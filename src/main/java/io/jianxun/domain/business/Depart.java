package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBaseEntity;

@Entity
@Table(name = "jx_sys_depart")
public class Depart extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3341305069366548864L;

	@NotBlank(message = "{depart.name.notblank}")
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Depart parent;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public Depart getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Depart parent) {
		this.parent = parent;
	}

}