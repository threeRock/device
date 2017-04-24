package io.jianxun.web.dto;

public class DepttestTree extends BaseTree {

	private String url = "depttest/page";
	private String divid = "#depttest-page-layout";

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url + "/" + this.getId();
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the divid
	 */
	public String getDivid() {
		return divid;
	}

	/**
	 * @param divid
	 *            the divid to set
	 */
	public void setDivid(String divid) {
		this.divid = divid;
	}

}
