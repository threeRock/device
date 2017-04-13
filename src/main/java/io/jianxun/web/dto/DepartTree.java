package io.jianxun.web.dto;

public class DepartTree extends BaseTree {

	private String url = "depart/page";
	private String divid = "#depart-page-layout";

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
