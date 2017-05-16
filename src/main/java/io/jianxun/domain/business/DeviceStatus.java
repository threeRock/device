package io.jianxun.domain.business;

public enum DeviceStatus {
	DISCARD("报废"), MAINTENANC("检修中");

	private String name;

	private DeviceStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
