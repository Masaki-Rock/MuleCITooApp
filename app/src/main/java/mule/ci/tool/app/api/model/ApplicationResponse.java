package mule.ci.tool.app.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ApplicationResponse {
	
	@JsonProperty("domain")
	private String domain;
	
	@JsonProperty("fullDomain")
	private String fullDomain;
	
	@JsonProperty("properties")
	private Map<String, Object> properties;
	
	@JsonProperty("status")
    private String status;
    
    @JsonProperty("workers")
	private Map<String, Object> workers;
    
    @JsonProperty("muleVersion")
	private Map<String, Object> muleVersion;
    
    @JsonProperty("trackingSettings")
	private Map<String, Object> trackingSettings;
}
