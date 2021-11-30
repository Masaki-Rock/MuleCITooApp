package mule.ci.tool.app.api.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TiersResponse {

	@JsonProperty("total")
	private Integer total;
	
	@JsonProperty("tiers")
	private List<Map<String,Object>> tiers;

	public List<Map<String, Object>> getTiers() {
		return tiers;
	}

	public void setTiers(List<Map<String, Object>> tiers) {
		this.tiers = tiers;
	}
}
