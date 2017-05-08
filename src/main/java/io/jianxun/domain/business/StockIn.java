package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.jianxun.domain.AbstractBusinessDepartEntity;

@Entity
@Table(name = "jx_device_stockin")
public class StockIn extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = 8366464276775587307L;
	// 备件
	@ManyToOne
	@JoinColumn(name = "part_id")
	@NotNull(message = "{stockin.part.notnull}")
	private SparePart sparepart;
	// 申请数量
	private Integer capacity = 1;

	// 备注
	private String remark;

	public SparePart getSparepart() {
		return sparepart;
	}

	public void setSparepart(SparePart sparepart) {
		this.sparepart = sparepart;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
