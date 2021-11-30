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
	 * アプリケーション検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
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
     * アプリケーション更新機能
     * @param domain ドメイン名
     * @return 更新結果
     * @throws AppException アプリケーション例外
     */
    public ApplicationResponse updateApplication(String domain) throws AppException {
    	
    	// リクエストメッセージ作成
    	String boundary = "----mulesaveapplication";
    	ApplicationRequest application = new ApplicationRequest();
    	String request = HttpClientUtil.toMultipart(application, boundary);
 
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type","multipart/form-data; boundary=" + boundary);
    	
		// リクエスト送信
    	String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestForMultipartAndCloudHubAPI(path, Const.PUT, request, boundary);

		// レスポンス変換
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		log("saveAPIInstance. response:{}", res);
		return res;
    }
    
    public static void deleteApplication() throws AppException {
    	
    }

    /**
     * ランタイムアラート検索機能
     * @param applicationName アプリケーション名
     * @return 検索結果
     * @throws AppException アプリケーション例外
     */
    public CommonResponse findRuntimeAlert() throws AppException {
    	
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		log("findRuntimeAlert. {}", res);
		return res;
    }
    
    /**
     * ランタイムアラート作成処理
     * @param applicationName アプリケーション名
     * @return 登録結果
     * @throws AppException アプリケーション例外
     */
    public RuntimeAlertResponse saveRuntimeAlert(String key) throws AppException {
    	
    	// リクエストメッセージ作成
    	RuntimeAlertRequest alert = RuntimeAlertRequest.factory(key);
		
		// リクエスト送信
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.POST, alert);

		// レスポンス変換
		RuntimeAlertResponse res = HttpClientUtil.makeResponse(resbody, RuntimeAlertResponse.class);
		log("saveRuntimeAlert. response:{}", res);
		return res;
    }
   
    /**
     * ランタイムアラート作成処理
     * @param applicationName アプリケーション名
     * @return 登録結果
     * @throws AppException アプリケーション例外
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
     * ランタイムアラート削除機能
     * @param alertId ランタイムアラートID
     * @return 削除結果
     * @throws AppException アプリケーション例外
     */
    public Boolean deleteRuntimeAlert(String alertId) throws AppException {
    	
    	// リクエストメッセージ作成
//    	RuntimeAlertRequest alert = new RuntimeAlertRequest();
    			
		// リクエスト送信
    	String path = String.format(Const.RUNTIME_ALERT_END_POINT, "/", alertId);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		log("deleteRuntimeAlert. response:{}", resbody);
		if (StringUtils.equals("204",resbody)) {
			return true;
		}
		return false;	
    }
    
    /**
     * ランタイムアラート削除機能
     * @param alertId ランタイムアラートID
     * @return 削除結果
     * @throws AppException アプリケーション例外
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
	 * アセット検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
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
