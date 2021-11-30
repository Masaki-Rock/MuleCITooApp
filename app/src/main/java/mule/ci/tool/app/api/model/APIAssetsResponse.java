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
	
	@SuppressWarnings("unchecked")
	public Integer getId() {
		Map<String,Object> asset = (Map<String,Object>) this.assets.get(0);
		List<Map<String,Object>> apis = (List<Map<String,Object>>) asset.get("apis");
		Map<String,Object> target = null;
		for (Map<String,Object> api : apis) {
			if (StringUtils.equals(Const.API_INSTANCE_LABEL, (String) api.get("instanceLabel"))) {
				target = api;
			}
		}
		return (Integer) target.get("id");
	}
	
	public String getInstanceLabel() {
		return "";
	}
}
