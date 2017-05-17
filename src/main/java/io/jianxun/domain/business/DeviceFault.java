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

/*
 * 设备故障（事故）信息
 */
@Entity
@Table(name = "jx_device_fault")
public class DeviceFault extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8495574562832238081L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	// 发生日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate occurrenceDate;

	// 停机时间
	private String duration;

	// 原因分析
	@Column(length = 1000)
	@NotBlank(message = "{device.fault.reason.notblank}")
	private String reason;

	// 纠正预防措施
	private String measures;

	// 结果影响
	private String effect;

	// 事故性质
	private String nature;

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

	public LocalDate getOccurrenceDate() {
		return occurrenceDate;
	}

	public void setOccurrenceDate(LocalDate occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
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
