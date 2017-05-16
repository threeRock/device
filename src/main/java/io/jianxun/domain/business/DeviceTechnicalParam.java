package io.jianxun.domain.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 设备技术参数
 * 
 * @author tt
 *
 */
@Entity
@Table(name = "jx_device_technical_param")
public class DeviceTechnicalParam extends AbstractBusinessEntity {

	private static final long serialVersionUID = -5043491568232867300L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	//名称
	@NotBlank(message = "{device.technical.param.name.notblank}")
	private String name;

	//相关参数
	@NotBlank(message = "{device.technical.param.params.notblank}")
	@Column(length = 2000)
	private String params;

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
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

}
