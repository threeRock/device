package io.jianxun.domain.business;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.collect.Lists;

import io.jianxun.domain.AbstractBusinessDepartEntity;

//设备信息
@Entity
@Table(name = "jx_device")
public class Device extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = 6906314156200686291L;

	// 名称
	@NotBlank(message = "{device.name.notblank}")
	private String name;
	// 首要图片地址
	private String mainPic;
	// 所属产线(根据机构过滤)
	@NotNull(message = "device.productionLine.notnull")
	@ManyToOne
	@JoinColumn(name="productionline_id")
	private ProductionLine productionLine;
	// 型号
	private String typeInfo;
	// 编码
	@NotBlank(message = "device.code.notblank")
	private String code;

	// 设备类别
	@NotNull(message = "device.mainType.notnull")
	@ManyToOne
	@JoinColumn(name="mainType_id")
	private SparePartMainType mainType;

	// 生产厂家
	private String manufacturer;

	// 出厂编号
	private String serialNumber;

	// 日期信息
	private String dateInfo;

	// 重量信息
	private String weightInfo;

	// 技术参数
	private String description;

	// 图片地址
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "jx_device_device_pics")
	private List<String> pics = Lists.newArrayList();

	// 设备状态
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMainPic() {
		return mainPic;
	}

	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}

	public ProductionLine getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(ProductionLine productionLine) {
		this.productionLine = productionLine;
	}

	public String getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SparePartMainType getMainType() {
		return mainType;
	}

	public void setMainType(SparePartMainType mainType) {
		this.mainType = mainType;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDateInfo() {
		return dateInfo;
	}

	public void setDateInfo(String dateInfo) {
		this.dateInfo = dateInfo;
	}

	public String getWeightInfo() {
		return weightInfo;
	}

	public void setWeightInfo(String weightInfo) {
		this.weightInfo = weightInfo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
