package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.jianxun.domain.AbstractBusinessDepartEntity;

/**
 * 出库单
 * 
 * @author tt
 *
 */
@Entity
@Table(name = "jx_device_stockout")
public class StockOut extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = -2631351316462567354L;

	// 出库备件
	@ManyToOne
	@JoinColumn(name = "part_id")
	@NotNull(message = "{stockout.part.notnull}")
	private SparePart sparePart;
	// 申请人
	private String requisitionUser;

	// 申请数量
	private Integer capacity = 1;

	private String remark;

	/**
	 * @return the sparePart
	 */
	public SparePart getSparePart() {
		return sparePart;
	}

	/**
	 * @param sparePart
	 *            the sparePart to set
	 */
	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	/**
	 * @return the requisitionUser
	 */
	public String getRequisitionUser() {
		return requisitionUser;
	}

	/**
	 * @param requisitionUser
	 *            the requisitionUser to set
	 */
	public void setRequisitionUser(String requisitionUser) {
		this.requisitionUser = requisitionUser;
	}

	/**
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
