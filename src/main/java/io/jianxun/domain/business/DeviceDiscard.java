package io.jianxun.domain.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 报废信息
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "jx_device_discard")
public class DeviceDiscard extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7593950133663460003L;

	// 报废设备
	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	// 报废原因及备注
	@Column(length = 2000)
	private String reason;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
