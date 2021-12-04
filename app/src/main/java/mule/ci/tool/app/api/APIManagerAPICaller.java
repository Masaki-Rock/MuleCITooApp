package mule.ci.tool.app.api;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import mule.ci.tool.app.api.model.APIAlertRequest;
import mule.ci.tool.app.api.model.APIAlertResponse;
import mule.ci.tool.app.api.model.APIAssetsResponse;
import mule.ci.tool.app.api.model.APIInstanceRequest;
import mule.ci.tool.app.api.model.APIInstanceRequestForAssetVersion;
import mule.ci.tool.app.api.model.APIInstanceResponse;
import mule.ci.tool.app.api.model.ExchangeAssetResponse;
import mule.ci.tool.app.api.model.PoliciesResponse;
import mule.ci.tool.app.api.model.PolicyRequest;
import mule.ci.tool.app.api.model.PolicyResponse;
import mule.ci.tool.app.api.model.TierRequest;
import mule.ci.tool.app.api.model.TierResponse;
import mule.ci.tool.app.api.model.TiersResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;
import mule.ci.tool.app.util.HttpClientUtil;

public class APIManagerAPICaller {

	private static final Logger log = LoggerFactory.getLogger(APIManagerAPICaller.class);

	/**
	 * APIインスタンス検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public APIAssetsResponse getAPIInstance() throws AppException {

		String path = String.format(Const.API_INSTANCE_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				StringUtils.EMPTY,StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);

		APIAssetsResponse res = HttpClientUtil.makeResponse(resbody, APIAssetsResponse.class);
		log("getAPIInstance. {}", res);
		return res;
	}

	/**
	 * APIインスタンス作成機能
	 * @param groupId グループID
	 * @param assetId アセットID
	 * @param version バージョン
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public APIInstanceResponse saveAPIInstance(String groupId, String assetId, String version) throws AppException {

		APIInstanceRequest apiInstance = new APIInstanceRequest(groupId, assetId, version);

		String path = String.format(Const.API_INSTANCE_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				StringUtils.EMPTY,StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.POST, apiInstance);

		APIInstanceResponse res = HttpClientUtil.makeResponse(resbody, APIInstanceResponse.class);
		log("saveAPIInstance. {}", res);
		return res;
	}
	
	/**
	 * APIインスタンス作成機能
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public APIInstanceResponse saveAPIInstance() throws AppException {

		ExchangeAssetResponse param = new ExchangeAPICaller().findAsset();
		APIInstanceResponse response = new APIManagerAPICaller().saveAPIInstance(param.getGroupId(), param.getAssetId(),
				param.getVersion());
		return response;
	}

	/**
	 * APIインスタンス更新機能
	 * @param environmentApiId APIインスタンスID
	 * @param assetVersion アセットバージョン
	 * @return 更新結果
	 * @throws AppException アプリケーション例外
	 */
	public APIInstanceResponse updateAPIInstance(Integer environmentApiId, String assetVersion) throws AppException {

		APIInstanceRequestForAssetVersion apiInstance = new APIInstanceRequestForAssetVersion();
		apiInstance.setAssetVersion(assetVersion);
		
		String path = String.format(Const.API_INSTANCE_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				"/", environmentApiId);
		String resbody = HttpClientUtil.sendRequest(path, Const.PATCH, apiInstance);

		APIInstanceResponse res = HttpClientUtil.makeResponse(resbody, APIInstanceResponse.class);
		log("updateAPIInstance. {}", res);
		return res;
	}
	
	/**
	 * APIインスタンス更新機能
	 * @return 更新結果
	 * @throws AppException アプリケーション例外
	 */
	public APIInstanceResponse updateAPIInstance() throws AppException {

		ExchangeAssetResponse param = new ExchangeAPICaller().findAsset();
		APIAssetsResponse api = getAPIInstance();
		APIInstanceResponse response = new APIManagerAPICaller().updateAPIInstance(api.getId(), param.getVersion());
		return response;
	}

	/**
	 * APIインスタンス削除処理
	 * @param environmentApiId APIインスタンスID
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deleteAPIInstance(Integer environmentApiId) throws AppException {
		
		String path = String.format(Const.API_INSTANCE_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				"/", environmentApiId);
		String resbody = HttpClientUtil.sendRequest(path, Const.DELETE, null);

		log("deletePolicy. {}", resbody);
		if (!StringUtils.equals("204", resbody)) {
			throw new AppException();
		}
		return true;
	}
	
	/**
	 * APIインスタンス削除処理
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public void deleteAPIInstance() throws AppException {
		APIAssetsResponse api = getAPIInstance();
		deleteAPIInstance(api.getId());
	}

	/**
	 * SLA層検索機能
	 * @param environmentApiId APIインスタンスID
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public TiersResponse findTier(Integer environmentApiId) throws AppException {

		String path = String.format(Const.TIERS_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);

		TiersResponse res = HttpClientUtil.makeResponse(resbody, TiersResponse.class);
		log("findTier. {}", res);
		return res;
	}

	/**
	 * SLA層登録機能
	 * @param environmentApiId APIインスタンスID
	 * @param name SLA名
	 * @param description 説明
	 * @param autoApprove 自動承認フラグ
	 * @param maximumRequests 受付可能なリクエスト件数
	 * @param timePeriodInMilliSeconds 受付期間
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public TierResponse saveSLATier(Integer environmentApiId, String name,
			String description, Boolean autoApprove, 
			Integer maximumRequests, Integer timePeriodInMilliSeconds) throws AppException {

		TierRequest tier = new TierRequest(name, description,autoApprove,
				maximumRequests, timePeriodInMilliSeconds);

		String path = String.format(Const.TIERS_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.POST, tier);

		TierResponse res = HttpClientUtil.makeResponse(resbody, TierResponse.class);
		log("saveSLATier. {}", res);
		return res;
	}
	
	/**
	 * SLA層登録機能
	 * @param environmentApiId APIインスタンスID
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public void saveSLATiers() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		for (Map<String, Object> tier: Const.TIERS) {

			saveSLATier(param.getId(),
					(String) tier.get("name"),
					(String) tier.get("description"),
					(Boolean) tier.get("autoApprove"),
					(Integer) tier.get("maximumRequests"),
					(Integer) tier.get("timePeriodInMilliSeconds"));
		}
	}

	/**
	 * SLA層削除機能
	 * @param environmentApiId APIインスタンスID
	 * @param tierId SLA層ID
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deleteSLATier(Integer environmentApiId, Integer tierId) throws AppException {

		String path = String.format(Const.TIERS_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, "/", tierId);
		String resbody = HttpClientUtil.sendRequest(path, Const.DELETE, null);

		log("deletePolicy. {}", resbody);
		if (!StringUtils.equals("204", resbody)) {
			throw new AppException();
		}
		return true;
	}

	/**
	 * SLA層削除機能
	 * @throws AppException アプリケーション例外
	 */
	public void deleteSLATiers() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		TiersResponse response = findTier(param.getId());
		if (response.getTiers() == null) {
			return;
		}
		for(Map<String, Object> tier: response.getTiers()) {
			deleteSLATier(param.getId(),(Integer) tier.get("id"));
		}
	}

	/**
	 * ポリシー検索機能
	 * @param environmentApiId APIインスタンスID
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public PoliciesResponse findPolicy(Integer environmentApiId) throws AppException {

		String path = String.format(Const.POLICY_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		PoliciesResponse res = HttpClientUtil.makeResponse(resbody, PoliciesResponse.class);
		log("findPolicy. {}", res);
		return res;
	}

	/**
	 * ポリシー登録機能
	 * @param environmentApiId APIインスタンスID
	 * @param policyName ポリシー名
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public PolicyResponse savePolicy(Integer environmentApiId,
			String policyName) throws AppException {

		PolicyRequest policy = PolicyRequest.factory(policyName);

		String path = String.format(Const.POLICY_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.POST, policy);

		PolicyResponse res = HttpClientUtil.makeResponse(resbody, PolicyResponse.class);
		log("savePolicy. {}", res);
		if (res.getId() == null) {
			throw new AppException();
		}
		return res;
	}
	
	/**
	 * ポリシー登録機能
	 * @throws AppException アプリケーション例外
	 */
	public void savePolicies() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		for (String policyName : Const.POLICIES.keySet()) {
			savePolicy(param.getId(), policyName);
		}
	}

	/**
	 * ポリシー削除機能
	 * @param environmentApiId APIインスタンス
	 * @param policyId ポリシーID
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deletePolicy(Integer environmentApiId, Integer policyId) throws AppException {

		String path = String.format(Const.POLICY_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, "/", policyId);
		String resbody = HttpClientUtil.sendRequest(path, Const.DELETE, null);

		log("deletePolicy. {}", resbody);
		if (!StringUtils.equals("204", resbody)) {
			throw new AppException();
		}
		return true;
	}
	
	/**
	 * ポリシー削除機能
	 * @throws AppException アプリケーション例外
	 */
	public void deletePolicies() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		PoliciesResponse response = findPolicy(param.getId());
		if (response.getPolicies() == null) {
			return;
		}
		for (Map<String, Object> policy: response.getPolicies()) {
			deletePolicy(param.getId(),(Integer) policy.get("policyId"));
		}
	}

	/**
	 * APIアラート検索機能
	 * @param environmentApiId APIインスタンスID
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public APIAlertResponse[] findAPIAlert(Integer environmentApiId) throws AppException {

		String path = String.format(Const.API_ALERT_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);

		APIAlertResponse[] res = HttpClientUtil.makeResponse(resbody, APIAlertResponse[].class);
		log("findAPIAlert. {}", res);
		return res;
	}

	/**
	 * アラート登録機能
	 * @param environmentApiId APIインスタンス
	 * @param alertType アラートタイプ
	 * @param policyId ポリシーID
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public TierResponse saveAPIAlert(Integer environmentApiId, String alertType, Integer policyId) throws AppException {

		APIAlertRequest alert = APIAlertRequest.factory(alertType, policyId);

		String path = String.format(Const.API_ALERT_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequest(path, Const.POST, alert);

		TierResponse res = HttpClientUtil.makeResponse(resbody, TierResponse.class);
		log("saveAPIAlert. {}", res);
		if (res.getId() == null) {
			throw new AppException();
		}
		return res;
	}

	/**
	 * アラート登録機能
	 * @throws AppException アプリケーション例外
	 */
	public void saveAlerts() throws AppException {

		APIAssetsResponse api = getAPIInstance();
		PoliciesResponse policy = findPolicy(api.getId());
		for (String alertType: Const.ALERTS) {
			saveAPIAlert(api.getId(), alertType, policy.get(alertType));
		}
	}

	/**
	 * アラート削除機能
	 * @param environmentApiId APIインスタンスID
	 * @param alertId アラートID
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deleteAPIAlert(Integer environmentApiId, String alertId) throws AppException {

		String path = String.format(Const.API_ALERT_END_POINT, Const.ORGANIZATION_ID, Const.DEV_ENVIRONMENT_ID,
				environmentApiId, "/", alertId);
		String resbody = HttpClientUtil.sendRequest(path, Const.DELETE, null);

		log("deleteAPIAlert. {}", resbody);
		if (!StringUtils.equals("204", resbody)) {
			throw new AppException();
		}
		return true;
	}
	
	/**
	 * アラート削除機能
	 * @param environmentApiId APIインスタンスID
	 * @param alertId アラートID
	 * @throws AppException アプリケーション例外
	 */
	public void deleteAlerts() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		APIAlertResponse[] alerts = findAPIAlert(param.getId());
		if (alerts == null) {
			return;
		}
		for (APIAlertResponse alert: alerts) {
			Boolean flg = deleteAPIAlert(param.getId(), alert.getId());
			if (!flg) {
				throw new AppException();
			}
		}
	}

	/**
	 * JSON形式ログ出力機能
	 * @param marker マーカー 例： "Application Response {}" 
	 * @param res 出力オブジェクト
	 * @throws AppException アプリケーション例外
	 */
	public static void log(String marker, Object res) throws AppException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String json;
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			throw new AppException(e);
		}
		log.debug(marker, json);
	}
}
