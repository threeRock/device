package io.jianxun.domain.business;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import io.jianxun.domain.AbstractBusinessDepartEntity;

@Entity
@Table(name = "jx_device_storehouse")
public class Storehouse extends AbstractBusinessDepartEntity {

	private static final long serialVersionUID = -2643386533717724416L;

	@NotBlank(message = "{storehouse.name.notblank}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
