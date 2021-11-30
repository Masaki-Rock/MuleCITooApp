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
	 * API�C���X�^���X�����@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
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
	 * API�C���X�^���X�쐬�@�\
	 * @param groupId �O���[�vID
	 * @param assetId �A�Z�b�gID
	 * @param version �o�[�W����
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
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
	 * API�C���X�^���X�쐬�@�\
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public APIInstanceResponse saveAPIInstance() throws AppException {

		ExchangeAssetResponse param = new ExchangeAPICaller().findAsset();
		APIInstanceResponse response = new APIManagerAPICaller().saveAPIInstance(param.getGroupId(), param.getAssetId(),
				param.getVersion());
		return response;
	}

	/**
	 * API�C���X�^���X�X�V�@�\
	 * @param assetVersion �A�Z�b�g�o�[�W����
	 * @return �X�V����
	 * @throws AppException �A�v���P�[�V������O
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
	 * API�C���X�^���X�X�V�@�\
	 * @return �X�V����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public APIInstanceResponse updateAPIInstance() throws AppException {

		ExchangeAssetResponse param = new ExchangeAPICaller().findAsset();
		APIAssetsResponse api = getAPIInstance();
		APIInstanceResponse response = new APIManagerAPICaller().updateAPIInstance(api.getId(), param.getVersion());
		return response;
	}

	/**
	 * API�C���X�^���X�폜����
	 * @param environmentApiId API�C���X�^���X
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
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
	 * API�C���X�^���X�폜����
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void deleteAPIInstance() throws AppException {
		APIAssetsResponse api = getAPIInstance();
		deleteAPIInstance(api.getId());
	}

	/**
	 * SLA�w�����@�\
	 * 
	 * @param environmentApiId API ID
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
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
	 * SLA�w�o�^�@�\
	 * @param environmentApiId ��ID
	 * @param name SLA��
	 * @param description ����
	 * @param autoApprove �������F�t���O
	 * @param maximumRequests ��t�\�ȃ��N�G�X�g����
	 * @param timePeriodInMilliSeconds ��t����
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public TierResponse saveTier(Integer environmentApiId, String name,
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
	 * SLA�w�o�^�@�\
	 * @param environmentApiId ��ID
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void saveAllTier() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		for (Map<String, Object> tier: Const.TIERS) {

			saveTier(param.getId(),
					(String) tier.get("name"),
					(String) tier.get("description"),
					(Boolean) tier.get("autoApprove"),
					(Integer) tier.get("maximumRequests"),
					(Integer) tier.get("timePeriodInMilliSeconds"));
		}
	}

	public void updateTier(Integer environmentApiId) throws AppException {

	}

	/**
	 * SLA�w�폜�@�\
	 * @param environmentApiId API�C���X�^���X
	 * @param tierId SLA�wID
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public Boolean deleteTier(Integer environmentApiId, Integer tierId) throws AppException {

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
	 * SLA�w�폜�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void deleteAllTier() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		TiersResponse response = findTier(param.getId());
		for(Map<String, Object> tier: response.getTiers()) {
			deleteTier(param.getId(),(Integer) tier.get("id"));
		}
	}

	/**
	 * �|���V�[�����@�\
	 * @param environmentApiId APIID
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
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
	 * �|���V�[�o�^�@�\
	 * @param environmentApiId API�C���X�^���XID
	 * @param policyName �|���V�[��
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
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
	 * �|���V�[�o�^�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void saveAllPolicy() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		for (String policyName : Const.POLICIES.keySet()) {
			savePolicy(param.getId(), policyName);
		}
	}

	public void updatePolicy() throws AppException {

	}

	/**
	 * �|���V�[�폜�@�\
	 * @param environmentApiId API�C���X�^���X
	 * @param policyId �|���V�[ID
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
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
	 * �|���V�[�폜�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void deleteAllPolicy() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		PoliciesResponse response = findPolicy(param.getId());
		for (Map<String, Object> policy: response.getPolicies()) {
			deletePolicy(param.getId(),(Integer) policy.get("policyId"));
		}
	}

	/**
	 * API�A���[�g�����@�\
	 * @param environmentApiId API�C���X�^���XID
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
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
	 * �A���[�g�o�^�@�\
	 * @param environmentApiId API�C���X�^���X
	 * @param alertType �A���[�g�^�C�v
	 * @param policyId �|���V�[ID
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
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
	 * �A���[�g�o�^�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void saveAllAPIAlert() throws AppException {

		APIAssetsResponse api = getAPIInstance();
		PoliciesResponse policy = findPolicy(api.getId());
		for (String alertType: Const.ALERTS) {
			saveAPIAlert(api.getId(), alertType, policy.get(alertType));
		}
	}
	
	public void updateAPIAlert(String environmentApiId) throws AppException {

	}

	/**
	 * �A���[�g�폜�@�\
	 * @param environmentApiId API�C���X�^���XID
	 * @param alertId �A���[�gID
	 * @throws AppException �A�v���P�[�V������O
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
	 * �A���[�g�폜�@�\
	 * @param environmentApiId API�C���X�^���XID
	 * @param alertId �A���[�gID
	 * @throws AppException �A�v���P�[�V������O
	 */
	public void deleteAllAPIAlert() throws AppException {

		APIAssetsResponse param = getAPIInstance();
		APIAlertResponse[] alerts = findAPIAlert(param.getId());
		for (APIAlertResponse alert: alerts) {
			Boolean flg = deleteAPIAlert(param.getId(), alert.getId());
			if (!flg) {
				throw new AppException();
			}
		}
	}

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
