package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessDepartEntity;

/**
 * 生产线定义
 * 
 * @author tt
 *
 */
@Entity
@Table(name = "jx_device_production_line")
public class ProductionLine extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = 132769689369371519L;

	// 生产线名称
	@NotBlank(message = "{productionline.name.notblank}")
	private String name;

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

	@Override
	public String toString() {
		return "生产线 [名称=" + name + "]";
	}
	
	

}
