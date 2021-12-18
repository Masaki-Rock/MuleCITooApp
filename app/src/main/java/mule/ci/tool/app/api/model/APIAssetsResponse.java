package mule.ci.tool.app.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIAssetsResponse {

	@JsonProperty("total")
	private Integer total;

	@JsonProperty("assets")
	private List<Map<String, Object>> assets;

	@JsonIgnore
	Map<String, String> apiIds;

	/**
	 * API ID取得機能
	 * 
	 * @param instanceName APIインスタンスネーム
	 * @return APIID
	 * @throws AppException アプリケーション例外
	 */
	public String getId(String apiInstanceName) throws AppException {

		if (this.apiIds == null) {
			getApiIds();
		}
		if (this.apiIds == null || this.apiIds.isEmpty())
			return null;
		return this.apiIds.get(apiInstanceName);
	}

	/**
	 * API ID存在チェック機能
	 * 
	 * @return 存在チェック結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean exist() throws AppException {

		if (this.apiIds == null) {
			getApiIds();
		}
		if (this.apiIds == null || this.apiIds.isEmpty())
			return false;
		return true;
	}

	/**
	 * API ID取得機能
	 * 
	 * @return API IDマップ
	 * @throws AppException アプリケーション例外
	 */
	public Map<String, String> getApiIds() throws AppException {

		this.apiIds = new HashMap<String, String>();

		if (this.assets == null || this.assets.isEmpty())
			return null;

		Map<String, Map<String, String>> assetmap = new HashMap<String, Map<String, String>>();
		for (Map<String, Object> asset : this.assets) {

			Map<String, String> apimap = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> apis = (List<Map<String, Object>>) asset.get("apis");
			for (Map<String, Object> api : apis) {
				apimap.put((String) api.get("instanceLabel"), api.get("id").toString());
			}
			assetmap.put((String) asset.get("assetId"), apimap);
		}
		for (String key : Const.API_INSTANCES.keySet()) {
			Map<String, String> ins = Const.API_INSTANCES.get(key);
			Map<String, String> insmap = assetmap.get((String) ins.get("assetId"));
			if (insmap == null)
				throw new AppException();
			String apiId = insmap.get((String) ins.get("apiInstanceLabel"));
			if (apiId == null)
				throw new AppException();
			apiIds.put(key, apiId);
		}
		return this.apiIds;
	}
}
