package mule.ci.tool.app.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import mule.ci.tool.app.api.APIManagerAPICaller;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIAssetsResponse {

	private static final Logger log = LoggerFactory.getLogger(APIAssetsResponse.class);

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

		log.debug("getApiIds - 1");
		Map<String, Map<String, String>> assetmap = new HashMap<String, Map<String, String>>();
		for (Map<String, Object> asset : this.assets) {

			Map<String, String> apimap = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> apis = (List<Map<String, Object>>) asset.get("apis");
			log.debug("getApiIds - 2. {}", apis);
			if (apis == null) continue;
			for (Map<String, Object> api : apis) {
				apimap.put((String) api.get("instanceLabel"), api.get("id").toString());
			}
			log.debug("getApiIds - 3. {}", asset.get("assetId"));
			assetmap.put((String) asset.get("assetId"), apimap);
		}
		for (String key : Const.API_INSTANCES.keySet()) {
			log.debug("getApiIds - 4. {}", key);
			Map<String, String> ins = Const.API_INSTANCES.get(key);
			Map<String, String> insmap = assetmap.get((String) ins.get("assetId"));
			log.debug("getApiIds - 5. {}", key);
			if (insmap == null)
				throw new AppException();
			String apiId = insmap.get((String) ins.get("apiInstanceLabel"));
			if (apiId == null)
				throw new AppException();
			apiIds.put(key, apiId);
		}
		log.debug("getApiIds - 6.");
		return this.apiIds;
	}
}
