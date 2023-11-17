package maanyuba.api.models;

import jakarta.persistence.Column;

public class ResponseModel {
	@Column
	private boolean status;

	@Column
	private String msg;
	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
