package mule.ci.tool.app.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown=true)
public class PoliciesResponse {

	Map<String, Integer> policymap = new HashMap<String, Integer>();
	
	@JsonProperty("policies")
    private List<Map<String, Object>> policies;

	@JsonProperty("tiers")
    private Map<String, Object> tiers;
	
	public Integer get(String assetId) {
		
		if (this.policymap.isEmpty()) {
			for (Map<String, Object> policy : policies) {
				Integer policyId = (Integer) policy.get("policyId");
				@SuppressWarnings("unchecked")
				Map<String, Object> template = (Map<String, Object>) policy.get("template");
				String tAssetId = (String) template.get("assetId");
				this.policymap.put(tAssetId, policyId);
			}
		}
		return this.policymap.get(assetId);
	}

	public List<Map<String, Object>> getPolicies() {
		return policies;
	}

	public void setPolicies(List<Map<String, Object>> policies) {
		this.policies = policies;
	}
}
