package io.jianxun.web.dto;

/**
 * 
 * @author tongtn
 *
 */
public class ReturnDto {

	private static ReturnDto OK = new ReturnDto(StatusCode.OK);
	private static ReturnDto ERROR = new ReturnDto(StatusCode.ERROR);
	private static ReturnDto TIMEOUT = new ReturnDto(StatusCode.TIMEOUT);

	private Integer statusCode;
	private String message;
	private String tabid;
	private String dialogid;
	private boolean closeCurrent = false;
	private String divid;

	private ReturnDto(StatusCode statusCode) {
		super();
		this.statusCode = statusCode.code;
	}

	enum StatusCode {
		OK(200), ERROR(300), TIMEOUT(301);
		private Integer code;

		private StatusCode(Integer code) {
			this.code = code;
		}

		public Integer getCode() {
			return code;
		}

	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTabid() {
		return tabid;
	}

	public void setTabid(String tabid) {
		this.tabid = tabid;
	}

	public String getDialogid() {
		return dialogid;
	}

	public void setDialogid(String dialogid) {
		this.dialogid = dialogid;
	}

	public String getDivid() {
		return divid;
	}

	public void setDivid(String divid) {
		this.divid = divid;
	}

	public static ReturnDto ok(String message) {
		ReturnDto.OK.setMessage(message);
		ReturnDto.OK.setCloseCurrent(true);
		return ReturnDto.OK;
	}

	public static ReturnDto ok(String message, boolean closeCurrent) {
		ReturnDto.OK.setMessage(message);
		ReturnDto.OK.setCloseCurrent(closeCurrent);
		return ReturnDto.OK;
	}

	public static ReturnDto ok(String message, boolean closeCurrent, String tabId) {
		ReturnDto.OK.setMessage(message);
		ReturnDto.OK.setCloseCurrent(closeCurrent);
		ReturnDto.OK.setTabid(tabId);
		return ReturnDto.OK;
	}

	public static ReturnDto ok(String message, boolean closeCurrent, String tabId, String divId) {
		ReturnDto.OK.setMessage(message);
		ReturnDto.OK.setCloseCurrent(closeCurrent);
		ReturnDto.OK.setTabid(tabId);
		ReturnDto.OK.setDivid(divId);
		return ReturnDto.OK;
	}

	public static ReturnDto tabSuccessReturn(String message, String tabid) {
		ok(message).setTabid(tabid);
		return ReturnDto.OK;
	}

	public static ReturnDto error(String message) {
		ReturnDto.ERROR.setMessage(message);
		return ReturnDto.ERROR;
	}

	public static ReturnDto timeout(String message) {
		ReturnDto.TIMEOUT.setMessage(message);
		return ReturnDto.TIMEOUT;
	}

	/**
	 * @return the closeCurrent
	 */
	public boolean isCloseCurrent() {
		return closeCurrent;
	}

	/**
	 * @param closeCurrent
	 *            the closeCurrent to set
	 */
	public void setCloseCurrent(boolean closeCurrent) {
		this.closeCurrent = closeCurrent;
	}

}
