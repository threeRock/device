package io.jianxun.web.dto;

public class UploadPicReturnDto {

	private Integer statusCode;
	private String message;
	private String filename;

	public UploadPicReturnDto(Integer statusCode, String message, String filename) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.filename = filename;
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
