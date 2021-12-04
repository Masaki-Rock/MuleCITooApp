package mule.ci.tool.app.api.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import mule.ci.tool.app.util.Const;

public class APIAssetsResponse {

	@JsonProperty("total")
	private Integer total;
	
	@JsonProperty("assets")
	private List<Map<String,Object>> assets;
	
	public Integer getId() {
		if (Const.API_ID == null && getAPIInstance() != null) {
			Const.API_ID = (Integer) getAPIInstance().get("id");
		}
		return Const.API_ID;
	}
	
	public Boolean exist() {
		if(getAPIInstance() == null) {
			return false;
		}
		return true;
	}
	
	public Map<String,Object> getAPIInstance() {
		if (this.assets == null || this.assets.isEmpty()) {
			return null;
		}
		Map<String,Object> asset = (Map<String,Object>) this.assets.get(0);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> apis = (List<Map<String,Object>>) asset.get("apis");
		Map<String,Object> target = null;
		for (Map<String,Object> api : apis) {
			if (StringUtils.equals(Const.API_INSTANCE_LABEL, (String) api.get("instanceLabel"))) {
				target = api;
			}
		}
		return target;
	}
	
	public String getInstanceLabel() {
		return "";
	}
}
