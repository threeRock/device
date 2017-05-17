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
 * 设备检修（技改）信息
 */
@Entity
@Table(name = "jx_device_checkinfo")
public class DeviceCheckInfo extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8435620691227665598L;

	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	// 检修日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkDate = LocalDate.now();

	// 性质
	private String nature;

	// 项目编号
	private String projectCode;

	// 项目内容
	@NotBlank(message = "{device.checkinfo.content.notblank}")
	private String content;

	// 更换设备
	@Column(length = 1000)
	private String replacement;

	// 质量评价
	private String evaluation;

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

	public LocalDate getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(LocalDate checkDate) {
		this.checkDate = checkDate;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
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
