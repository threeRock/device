package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jeecgframework.poi.excel.annotation.Excel;

import io.jianxun.domain.AbstractBusinessDepartEntity;

//备件信息
@Entity
@Table(name = "js_device_sparepart")
public class SparePart extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = 5727162012567100772L;
	// 备件名称
	@NotBlank(message = "sparepart.name.notblank")
	@Excel(name = "备件名称", orderNum = "20")
	private String name;
	// 备件号
	@Excel(name = "备件号", orderNum = "30")
	private String partnumber;
	// 规格型号号
	@Excel(name = "规格型号", orderNum = "40")
	private String specification;
	// 备件编码
	@Excel(name = "备件编码", orderNum = "50")
	private String partcode;

	// 首要图片地址
	private String mainPic;

	@ManyToOne
	@JoinColumn(name = "storehouse_id")
	@NotNull(message = "{sparepart.storepart.notnull}")
	private Storehouse storehouse;
	@Transient
	@Excel(name = "所属仓库", orderNum = "10", mergeVertical = true)
	private String storehouseName;

	// 所属设备
	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;
	@Transient
	@Excel(name = "所属设备", orderNum = "11", mergeVertical = true)
	private String deviceName;

	// 使用位置
	private String location;
	// 货架
	private String shelf;

	// 制造商厂家
	private String manufacturer;

	private String description;

	@ManyToOne
	@JoinColumn(name = "subType_id")
	private SparePartSubType subType;
	
	//库存
	@Transient
	private Integer stock =0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the partnumber
	 */
	public String getPartnumber() {
		return partnumber;
	}

	/**
	 * @param partnumber
	 *            the partnumber to set
	 */
	public void setPartnumber(String partnumber) {
		this.partnumber = partnumber;
	}

	/**
	 * @return the specification
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * @param specification
	 *            the specification to set
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	/**
	 * @return the partcode
	 */
	public String getPartcode() {
		return partcode;
	}

	/**
	 * @param partcode
	 *            the partcode to set
	 */
	public void setPartcode(String partcode) {
		this.partcode = partcode;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMainPic() {
		return mainPic;
	}

	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}

	public Storehouse getStorehouse() {
		return storehouse;
	}

	public void setStorehouse(Storehouse storehouse) {
		this.storehouse = storehouse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the subType
	 */
	public SparePartSubType getSubType() {
		return subType;
	}

	/**
	 * @param subType
	 *            the subType to set
	 */
	public void setSubType(SparePartSubType subType) {
		this.subType = subType;
	}

	public String getStorehouseName() {
		if (this.storehouse != null)
			return storehouse.toString();
		return storehouseName;
	}

	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		if (this.device != null)
			return device.toString();
		return deviceName;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("所属仓库:" + this.getStorehouseName() + ";名称:" + this.name);
		if (StringUtils.isNotBlank(this.deviceName))
			sb.append(";所属设备:" + this.deviceName);
		if (StringUtils.isNotBlank(this.partcode))
			sb.append(";备件编码:" + this.partcode);
		if (StringUtils.isNotBlank(this.partnumber))
			sb.append(";备件号:" + this.partnumber);
		if (StringUtils.isNotBlank(this.specification))
			sb.append(";规格型号:" + this.specification);
		return sb.toString();
	}

}
