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
	@NotNull(message="{stockin.part.notnull}")
	private SparePart sparepart;
	// 申请数量
	private Integer capacity = 1;

	// 使用位置
	private String location;
	// 货架
	private String shelf;
	// 备注
	private String remark;
	// 制造商厂家
	private String manufacturer;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

}
