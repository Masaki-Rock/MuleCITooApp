	package mule.ci.tool.app.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import mule.ci.tool.app.api.model.ApplicationRequest;
import mule.ci.tool.app.api.model.ApplicationResponse;
import mule.ci.tool.app.api.model.CommonResponse;
import mule.ci.tool.app.api.model.ExchangeAssetResponse;
import mule.ci.tool.app.api.model.RuntimeAlertRequest;
import mule.ci.tool.app.api.model.RuntimeAlertResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;
import mule.ci.tool.app.util.HttpClientUtil;

public class CloudhubAPICaller {

	private static final Logger log = LoggerFactory.getLogger(CloudhubAPICaller.class);

	/**
	 * �A�v���P�[�V���������@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
	 */
    public ApplicationResponse[] findApplication() throws AppException {
    	
    	String path = String.format(Const.APPLICATION_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		ApplicationResponse[] res = HttpClientUtil.makeResponse(resbody, ApplicationResponse[].class);
		
		log("findApplication. {}", res);
		return res;
    }
    
    public void saveApplication() throws AppException {

    }
    
    /**
     * �A�v���P�[�V�����X�V�@�\
     * @param domain �h���C����
     * @return �X�V����
     * @throws AppException �A�v���P�[�V������O
     */
    public ApplicationResponse updateApplication(String domain) throws AppException {
    	
    	// ���N�G�X�g���b�Z�[�W�쐬
    	String boundary = "----mulesaveapplication";
    	ApplicationRequest application = new ApplicationRequest();
    	String request = HttpClientUtil.toMultipart(application, boundary);
 
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type","multipart/form-data; boundary=" + boundary);
    	
		// ���N�G�X�g���M
    	String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestForMultipartAndCloudHubAPI(path, Const.PUT, request, boundary);

		// ���X�|���X�ϊ�
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		log("saveAPIInstance. response:{}", res);
		return res;
    }
    
    public static void deleteApplication() throws AppException {
    	
    }

    /**
     * �����^�C���A���[�g�����@�\
     * @param applicationName �A�v���P�[�V������
     * @return ��������
     * @throws AppException �A�v���P�[�V������O
     */
    public CommonResponse findRuntimeAlert() throws AppException {
    	
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		log("findRuntimeAlert. {}", res);
		return res;
    }
    
    /**
     * �����^�C���A���[�g�쐬����
     * @param applicationName �A�v���P�[�V������
     * @return �o�^����
     * @throws AppException �A�v���P�[�V������O
     */
    public RuntimeAlertResponse saveRuntimeAlert(String key) throws AppException {
    	
    	// ���N�G�X�g���b�Z�[�W�쐬
    	RuntimeAlertRequest alert = RuntimeAlertRequest.factory(key);
		
		// ���N�G�X�g���M
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.POST, alert);

		// ���X�|���X�ϊ�
		RuntimeAlertResponse res = HttpClientUtil.makeResponse(resbody, RuntimeAlertResponse.class);
		log("saveRuntimeAlert. response:{}", res);
		return res;
    }
   
    /**
     * �����^�C���A���[�g�쐬����
     * @param applicationName �A�v���P�[�V������
     * @return �o�^����
     * @throws AppException �A�v���P�[�V������O
     */
    public void saveAllRuntimeAlert() throws AppException {
    	
		CloudhubAPICaller caller = new CloudhubAPICaller();
		for (String key: Const.RUNTIME_ALERTS) {
			RuntimeAlertResponse response = caller.saveRuntimeAlert(key);
			if (StringUtils.isBlank(response.getId())) {
				throw new AppException();
			}
		}
    }
    
    public void updateRuntimeAlert(String alertId) throws AppException {
    	 	
    }
    
    /**
     * �����^�C���A���[�g�폜�@�\
     * @param alertId �����^�C���A���[�gID
     * @return �폜����
     * @throws AppException �A�v���P�[�V������O
     */
    public Boolean deleteRuntimeAlert(String alertId) throws AppException {
    	
    	// ���N�G�X�g���b�Z�[�W�쐬
//    	RuntimeAlertRequest alert = new RuntimeAlertRequest();
    			
		// ���N�G�X�g���M
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, "/", alertId);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		log("deleteRuntimeAlert. response:{}", resbody);
		if (StringUtils.equals("204",resbody)) {
			return true;
		}
		return false;	
    }
    
    /**
     * �����^�C���A���[�g�폜�@�\
     * @param alertId �����^�C���A���[�gID
     * @return �폜����
     * @throws AppException �A�v���P�[�V������O
     */
    public void deleteAllRuntimeAlert() throws AppException {
    	
    	CloudhubAPICaller caller = new CloudhubAPICaller();
		CommonResponse alerts = caller.findRuntimeAlert();
		for (Map<String, Object> alert: alerts.getData()) {
			Boolean resflg = caller.deleteRuntimeAlert((String)alert.get("id"));
			if (!resflg) {
				throw new AppException();
			}
		}	
    }
    
	/**
	 * �A�Z�b�g�����@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
	 */
    public ExchangeAssetResponse findAccount() throws AppException {
    	
		String resbody = HttpClientUtil.sendRequest(Const.COUDHUB_ACCOUNT_END_POINT, Const.GET, null);
		ExchangeAssetResponse[] res = HttpClientUtil.makeResponse(resbody, ExchangeAssetResponse[].class);
		return res[0];
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
