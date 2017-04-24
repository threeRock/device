package io.jianxun.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.jeecgframework.poi.excel.annotation.Excel;

import io.jianxun.domain.business.Depart;

@MappedSuperclass
public abstract class AbstractBusinessDepartEntity extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1584297997705180649L;

	/**
	 * 所属机构
	 */
	@ManyToOne
	@JoinColumn(name = "depart_id")
	@NotNull(message = "{depart.notnull}")
	private Depart depart;
	@Transient
	@Excel(name = "所属机构", orderNum = "0", mergeVertical = true)
	private String departName;

	/**
	 * @return the depart
	 */
	public Depart getDepart() {
		return depart;
	}

	/**
	 * @param depart
	 *            the depart to set
	 */
	public void setDepart(Depart depart) {
		this.depart = depart;
	}

	public String getDepartName() {
		if (this.depart != null)
			return this.depart.getName();
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

}
