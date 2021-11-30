package mule.ci.tool.app.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class APIAlertResponse {
	
	private String id;
	
	private String name;
	
	private String apiAlertsVersion;
	
	private String environmentId;
	
	private String type;
	
	private String severity;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApiAlertsVersion() {
		return apiAlertsVersion;
	}
	public void setApiAlertsVersion(String apiAlertsVersion) {
		this.apiAlertsVersion = apiAlertsVersion;
	}
	public String getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
}
