package io.jianxun.domain.business;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 设备调整记录
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "jx_device_adjustment")
public class DeviceAdjustment extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5345168923699596859L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	// 名称
	@NotBlank(message="{device.adjustment.name.notblank}")
	private String name;

	// 日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate adjustDate = LocalDate.now();

	// 内容
	@Column(length = 1000)
	@NotBlank(message="{device.adjustment.content.notblank}")
	private String content;

	// 实施人或单位
	private String implementation;

	// 备注
	@Column(length = 1000)
	private String description;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getAdjustDate() {
		return adjustDate;
	}

	public void setAdjustDate(LocalDate adjustDate) {
		this.adjustDate = adjustDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
