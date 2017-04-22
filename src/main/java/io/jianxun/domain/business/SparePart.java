package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessDepartEntity;

//备件信息
@Entity
@Table(name = "js_device_sparepart")
public class SparePart extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = 5727162012567100772L;
	// 备件名称
	@NotBlank(message = "sparepart.name.notblank")
	private String name;
	// 备件编码
	@NotBlank(message = "sparepart.code.notblank")
	private String code;
	// 首要图片地址
	private String mainPic;

	@ManyToOne
	@JoinColumn(name = "storehouse_id")
	@NotNull(message = "{sparepart.storepart.notnull}")
	private Storehouse storehouse;

	private String description;

	@ManyToOne
	@JoinColumn(name = "subType_id")
	private SparePartSubType subType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

}