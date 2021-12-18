package mule.ci.tool.app.api;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import mule.ci.tool.app.api.model.ApplicationRequest;
import mule.ci.tool.app.api.model.ApplicationRequestForUpdate;
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
	 * 
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public ApplicationResponse[] findApplication() throws AppException {

		String path = String.format(Const.APPLICATION_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		ApplicationResponse[] res = HttpClientUtil.makeResponse(resbody, ApplicationResponse[].class);
		return res;
	}

	/**
	 * アプリケーション検索機能
	 * 
	 * @param domain アプリケーション名
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public ApplicationResponse findApplication(String domain) throws AppException {

		ApplicationResponse[] applications = findApplication();
		for (ApplicationResponse application : applications) {
			if (StringUtils.equals(domain, application.getDomain())) {
				return application;
			}
		}
		return null;
	}

	/**
	 * アプリケーション登録機能
	 * 
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public ApplicationResponse saveApplication(String domain, Map<String, String> apiIds) throws AppException {

		ApplicationRequest application = new ApplicationRequest(domain, apiIds);
		log.debug("file path : {}", Const.APPLICATION_FILE_PATH);
		Path inpath = Paths.get(Const.APPLICATION_FILE_PATH);
		File applicationfile = inpath.toFile();
		log.debug("Mule Application File Path : {} {} {}", applicationfile.getAbsolutePath(), applicationfile.exists(),
				applicationfile.getName());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", applicationfile,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		fileDataBodyPart.setContentDisposition(
				FormDataContentDisposition.name("file").fileName(applicationfile.getName()).build());

		@SuppressWarnings("resource")
		MultiPart multiPart = new FormDataMultiPart()
				.field("autoStart", "true", MediaType.TEXT_PLAIN_TYPE)
				.field("appInfoJson", HttpClientUtil.toJson(application), MediaType.APPLICATION_JSON_TYPE)
				.bodyPart(fileDataBodyPart);
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		String path = String.format(Const.APPLICATION_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestForMultipart(path, Const.POST, multiPart);

		// レスポンス変換
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		return res;
	}

	/**
	 * アプリケーション更新機能
	 * 
	 * @param domain ドメイン名
	 * @return 更新結果
	 * @throws AppException アプリケーション例外
	 */
	public ApplicationResponse updateApplication(String domain, Map<String, String> apiIds) throws AppException {

		ApplicationRequestForUpdate application = new ApplicationRequestForUpdate(apiIds);
		Path inpath = Paths.get(Const.APPLICATION_FILE_PATH);
		File applicationfile = inpath.toFile();
		log.debug("Mule Application File Path : {} {} {}", applicationfile.getAbsolutePath(), applicationfile.exists(),
				applicationfile.getName());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", applicationfile,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		fileDataBodyPart.setContentDisposition(
				FormDataContentDisposition.name("file").fileName(applicationfile.getName()).build());

		@SuppressWarnings("resource")
		MultiPart multiPart = new FormDataMultiPart()
				.field("appInfoJson", HttpClientUtil.toJson(application), MediaType.APPLICATION_JSON_TYPE)
				.bodyPart(fileDataBodyPart);
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

		// リクエスト送信
		String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestForMultipart(path, Const.PUT, multiPart);

		// レスポンス変換
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		return res;
	}

	/**
	 * アプリケーション削除機能
	 * 
	 * @param domain アプリケーション名
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deleteApplication(String domain) throws AppException {
		// リクエスト送信
		String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		if (StringUtils.equals("204", resbody)) {
			return true;
		}
		return false;
	}

	/**
	 * ランタイムアラート検索機能
	 * 
	 * @param applicationName アプリケーション名
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public CommonResponse findRuntimeAlert() throws AppException {

		String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		return res;
	}

	/**
	 * ランタイムアラート作成処理
	 * 
	 * @param alertType アプリケーション名
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public RuntimeAlertResponse saveRuntimeAlert(String alertType) throws AppException {

		// リクエストメッセージ作成
		RuntimeAlertRequest alert = RuntimeAlertRequest.factory(alertType);

		// リクエスト送信
		String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.POST, alert);

		// レスポンス変換
		RuntimeAlertResponse res = HttpClientUtil.makeResponse(resbody, RuntimeAlertResponse.class);
		return res;
	}

	/**
	 * ランタイムアラート登録処理
	 * 
	 * @return 登録結果
	 * @throws AppException アプリケーション例外
	 */
	public void saveRuntimeAlerts() throws AppException {

		CloudhubAPICaller caller = new CloudhubAPICaller();
		for (String key : Const.RUNTIME_ALERTS) {
			RuntimeAlertResponse response = caller.saveRuntimeAlert(key);
			if (StringUtils.isBlank(response.getId())) {
				throw new AppException();
			}
		}
	}

	/**
	 * ランタイムアラート削除機能
	 * 
	 * @param alertId ランタイムアラートID
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public Boolean deleteRuntimeAlert(String alertId) throws AppException {

		// リクエスト送信
		String path = String.format(Const.RUNTIME_ALERT_END_POINT, "/", alertId);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		if (StringUtils.equals("204", resbody)) {
			return true;
		}
		return false;
	}

	/**
	 * ランタイムアラート削除機能
	 * 
	 * @return 削除結果
	 * @throws AppException アプリケーション例外
	 */
	public void deleteRuntimeAlerts() throws AppException {

		CloudhubAPICaller caller = new CloudhubAPICaller();
		CommonResponse alerts = caller.findRuntimeAlert();
		if (alerts.getData() == null) {
			return;
		}
		for (Map<String, Object> alert : alerts.getData()) {
			Boolean resflg = caller.deleteRuntimeAlert((String) alert.get("id"));
			if (!resflg) {
				throw new AppException();
			}
		}
	}

	/**
	 * アセット検索機能
	 * 
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public ExchangeAssetResponse findAccount() throws AppException {

		String resbody = HttpClientUtil.sendRequest(Const.COUDHUB_ACCOUNT_END_POINT, Const.GET, null);
		ExchangeAssetResponse[] res = HttpClientUtil.makeResponse(resbody, ExchangeAssetResponse[].class);
		log.debug("findAccount. {}", HttpClientUtil.toJson(res));
		return res[0];
	}
}
