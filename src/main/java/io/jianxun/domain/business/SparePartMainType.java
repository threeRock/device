package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessEntity;

/**
 * 备件大类
 * 
 * @author tt
 *
 */
@Entity
@Table(name = "jx_device_sp_maintype")
public class SparePartMainType extends AbstractBusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1822150643989692018L;

	// 类别名称
	@NotBlank(message = "{maintype.name.notblank}")
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
		return "设备类别 [名称=" + name + "]";
	}
	
	

}
