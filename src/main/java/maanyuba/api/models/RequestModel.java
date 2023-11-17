package maanyuba.api.models;

import jakarta.persistence.Column;

public class RequestModel {
	@Column
	private String parameter;
	
	public String getParameter() {
		return parameter;
	}

	public void getParameter(String parameter) {
		this.parameter = parameter;
	}

}
