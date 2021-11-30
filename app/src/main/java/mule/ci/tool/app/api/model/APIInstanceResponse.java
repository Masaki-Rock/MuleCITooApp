package mule.ci.tool.app.api.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIInstanceResponse {
	
	@JsonProperty("environmentId")
	private String environmentId;
	
	@JsonProperty("instanceLabel")
	private String instanceLabel;
	
	@JsonProperty("echnology")
	private String echnology;
	
	@JsonProperty("assetVersion")
	private String assetVersion;
	
	@JsonProperty("productVersion")
	private String productVersion;
	
	@JsonProperty("order")
	private String order;
	
	@JsonProperty("stage")
	private String stage;
	
	@JsonProperty("audit")
	private Map<String, Object> audit;
	
	@JsonProperty("masterOrganizationId")
	private String masterOrganizationId;
	
	@JsonProperty("organizationId")
	private String organizationId;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("groupId")
	private String groupId;
	
	@JsonProperty("assetId")
	private String assetId;
	
	@JsonProperty("tags")
	private List<String> tags;
	
	@JsonProperty("endpoint")
	private Map<String, Object> endpoint;
	
	@JsonProperty("autodiscoveryInstanceName")
	private String autodiscoveryInstanceName;
}
