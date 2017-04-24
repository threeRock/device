package io.jianxun.domain.business;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessEntity;

@Entity
@Table(name = "jx_sys_depart")
public class Depart extends AbstractBusinessEntity {

	public static final String LEVEL_CODE_SEPARATOR = "-";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3341305069366548864L;

	@NotBlank(message = "{depart.name.notblank}")
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Depart parent;

	// 层级代码 由所有上级id+"-"组成
	private String levelCode;

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

	/**
	 * @return the levelCode
	 */
	public String getLevelCode() {
		return levelCode;
	}

	/**
	 * @param levelCode
	 *            the levelCode to set
	 */
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	@Transient
	public boolean isRoot() {
		return this.parent == null;
	}

}
