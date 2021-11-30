package mule.ci.tool.app.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import mule.ci.tool.app.util.Const;

public class PolicyRequest {

	public PolicyRequest() {
	}

	public static PolicyRequest factory(String flg) {
		
		PolicyRequest policy = new PolicyRequest();
		if (StringUtils.equals("jwt-validation", flg)) {
			policy.setJWTValiedationPolicySetting();
		}
		if (StringUtils.equals("rate-limiting-sla-based", flg)) {
			policy.setRatelimitingSLAbasedPolicySetting();
		}
		if (StringUtils.equals("header-injection", flg)) {
			policy.setHeaderInjectionPolicySetting();
		}
		if (StringUtils.equals("header-removal", flg)) {
			policy.setHeaderRemovalPolicySetting();
		}
		if (StringUtils.equals("message-logging", flg)) {
			policy.setMessageLoggingPolicySetting();
		}

		return policy;
	}

	private String groupId;

	private String assetId;

	private String assetVersion;

	private String pointcutData;

	private Integer policyTemplateId;

	private Integer apiVersionId;

	private Object configurationData;

	public Object getConfigurationData() {
		return configurationData;
	}

	public void setConfigurationData(Object configurationData) {
		this.configurationData = configurationData;
	}

	public String getPointcutData() {
		return pointcutData;
	}

	public void setPointcutData(String pointcutData) {
		this.pointcutData = pointcutData;
	}

	public Integer getPolicyTemplateId() {
		return policyTemplateId;
	}

	public void setPolicyTemplateId(Integer policyTemplateId) {
		this.policyTemplateId = policyTemplateId;
	}

	public Integer getApiVersionId() {
		return apiVersionId;
	}

	public void setApiVersionId(Integer apiVersionId) {
		this.apiVersionId = apiVersionId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAssetVersion() {
		return assetVersion;
	}

	public void setAssetVersion(String assetVersion) {
		this.assetVersion = assetVersion;
	}

	/**
	 * JWT検証ポリシー設定
	 */
	private void setJWTValiedationPolicySetting() {
		
		@SuppressWarnings("unchecked")
		Map<String,Object> jwt = (Map<String,Object>) Const.POLICIES.get("jwt-validation");
		
		this.groupId = "68ef9520-24e9-4cf2-b2f5-620025690913";
		this.assetId = "jwt-validation";
		this.assetVersion = "1.2.0";
		this.pointcutData = null;
		this.policyTemplateId = 321288;
		this.apiVersionId = 17452856;
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("jwtOrigin", "customExpression");
		item.put("jwtExpression", "#[attributes.headers['jwt']]");
		item.put("signingMethod", "rsa");
		item.put("signingKeyLength", 256);
		item.put("jwtKeyOrigin", "text");
		item.put("textKey", jwt.get("jwtKey"));
		item.put("jwksUrl", "http://your-jwks-service.example:80/base/path");
		item.put("jwksServiceTimeToLive", 60);
		item.put("jwksServiceConnectionTimeout", 10000);
		item.put("skipClientIdValidation", false);
		item.put("clientIdExpression", jwt.get("clientIDExpression"));
		item.put("validateAudClaim", true);
		item.put("mandatoryAudClaim", true);
		item.put("supportedAudiences", jwt.get("audienceClaimValues"));
		item.put("mandatoryExpClaim", false);
		item.put("mandatoryNbfClaim", false);
		item.put("validateCustomClaim", false);
		this.configurationData = item;
	}

	/**
	 * レート制限設定
	 */
	private void setRatelimitingSLAbasedPolicySetting() {
		
		@SuppressWarnings("unchecked")
		Map<String,Object> ratelimiting = (Map<String,Object>) Const.POLICIES.get("rate-limiting-sla-based");
		
		this.groupId = "68ef9520-24e9-4cf2-b2f5-620025690913";
		this.assetId = "rate-limiting-sla-based";
		this.assetVersion = "1.2.8";
		this.pointcutData = null;
		this.policyTemplateId = 320676;
		this.apiVersionId = 17452856;
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("clientIdExpression", ratelimiting.get("clientIDExpression"));
		item.put("clientSecretExpression", "");
		item.put("clusterizable", true);
		item.put("exposeHeaders", false);
		this.configurationData = item;
	}

	/**
	 * ヘッダーインジェクション設定
	 */
	private void setHeaderInjectionPolicySetting() {

		@SuppressWarnings("unchecked")
		Map<String,Object> headerInjection = (Map<String,Object>) Const.POLICIES.get("header-injection");

		this.groupId = "68ef9520-24e9-4cf2-b2f5-620025690913";
		this.assetId = "header-injection";
		this.assetVersion = "1.2.1";
		this.pointcutData = null;
		this.policyTemplateId = 299242;
		this.apiVersionId = 17452856;
		Map<String, Object> item = new HashMap<String, Object>();
		List<Map<String, String>> inboundHeaders = new ArrayList<Map<String, String>>();
		Map<String, String> inboundHeader = new HashMap<String, String>();
		inboundHeader.put("key", (String) headerInjection.get("inboundItem"));
		inboundHeader.put("value",(String) headerInjection.get("inboundValue"));
		inboundHeaders.add(inboundHeader);
		item.put("inboundHeaders", inboundHeaders);
		this.configurationData = item;
	}

	/**
	 * ヘッダー削除設定
	 */
	private void setHeaderRemovalPolicySetting() {
		
		this.groupId = "68ef9520-24e9-4cf2-b2f5-620025690913";
		this.assetId = "header-removal";
		this.assetVersion = "1.0.3";
		this.pointcutData = null;
		this.policyTemplateId = 299243;
		this.apiVersionId = 17139603;
		Map<String, Object> item = new HashMap<String, Object>();
		List<String> inboundHeaders = new ArrayList<String>();
		inboundHeaders.add("JWT");
		inboundHeaders.add("Jwt");
		inboundHeaders.add("jwt");
		item.put("inboundHeaders", inboundHeaders);
		this.configurationData = item;
	}

	/**
	 * メッセージロギング機能
	 */
	private void setMessageLoggingPolicySetting() {

		this.groupId = "68ef9520-24e9-4cf2-b2f5-620025690913";
		this.assetId = "message-logging";
		this.assetVersion = "1.0.0";
		this.pointcutData = null;
		this.policyTemplateId = 57;
		this.apiVersionId = 17452856;
		List<Object> items = new ArrayList<Object>();
		
		// メッセージログ１
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("itemName", "Default configuration");
		Map<String, Object> itemData = new HashMap<String, Object>();
		itemData.put("message",
				"#[write(if(authentication.properties.claims.sub?) read(authentication.properties.claims.sub,'application/json') ++ { path: attributes.rawRequestUri } else { path: attributes.rawRequestUri }, 'application/json') replace '\n' with '' replace ' ' with '']");
		itemData.put("category", "Access Log");
		itemData.put("level", "INFO");
		itemData.put("firstSection", true);
		itemData.put("secondSection", false);
		item.put("itemData", itemData);
		items.add(item);
		
		// メッセージログ２
		Map<String, Object> item2 = new HashMap<String, Object>();
		item2.put("itemName", "Endpoint Http Status Code");
		Map<String, Object> itemData2 = new HashMap<String, Object>();
		itemData2.put("message",
				"#[(attributes.statusCode as String default '') ++ '' ++(attributes.resonPhrase default ''))^] ");
		itemData2.put("category ", "Access Log");
		itemData2.put("level ", "INFO");
		itemData.put("firstSection", false);
		itemData.put("secondSection", true);
		item2.put("itemData", itemData);
		items.add(item2);
		this.configurationData = items;
	}
}
