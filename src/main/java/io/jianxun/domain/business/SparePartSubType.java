package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessEntity;

@Entity
@Table(name = "jx_deivce_sp_subtype")
public class SparePartSubType extends AbstractBusinessEntity {

	private static final long serialVersionUID = 548820421233515693L;

	// 所属大类
	@ManyToOne
	@JoinColumn(name = "maintype_id")
	@NotNull(message = "{subtype.maintype.notnull}")
	private SparePartMainType mainType;

	@NotBlank(message = "{subtype.name.notblank}")
	private String name;

	/**
	 * @return the mainType
	 */
	public SparePartMainType getMainType() {
		return mainType;
	}

	/**
	 * @param mainType
	 *            the mainType to set
	 */
	public void setMainType(SparePartMainType mainType) {
		this.mainType = mainType;
	}

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

}
