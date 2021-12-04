package mule.ci.tool.app.api;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
	 * �A�v���P�[�V���������@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
	 */
	public ApplicationResponse[] findApplication() throws AppException {

		String path = String.format(Const.APPLICATION_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.GET, null);

		ApplicationResponse[] res = HttpClientUtil.makeResponse(resbody, ApplicationResponse[].class);
		return res;
	}
	
	/**
	 * �A�v���P�[�V���������@�\
	 * @param domain �A�v���P�[�V������
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
	 */
	public ApplicationResponse findApplication(String domain) throws AppException {

		ApplicationResponse[] applications = findApplication();
		for (ApplicationResponse application: applications) {
			if (StringUtils.equals(domain, application.getDomain())) {
				return application;
			}
		}
		return null;
	}

	/**
	 * �A�v���P�[�V�����o�^�@�\
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public ApplicationResponse saveApplication() throws AppException {

		ApplicationRequest application = new ApplicationRequest();
		log.debug("file path : {}", Const.APPLICATION_FILE_PATH);
		Path inpath = Paths.get(Const.APPLICATION_FILE_PATH);
		File applicationfile = inpath.toFile();
		log.debug("Mule Application File Path : {} {} {}", applicationfile.getAbsolutePath(), applicationfile.exists(), applicationfile.getName());
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

		// ���X�|���X�ϊ�
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		return res;
	}

	/**
	 * �A�v���P�[�V�����X�V�@�\
	 * @param domain �h���C����
	 * @return �X�V����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public ApplicationResponse updateApplication(String domain) throws AppException {

		ApplicationRequestForUpdate application = new ApplicationRequestForUpdate();
		Path inpath = Paths.get(Const.APPLICATION_FILE_PATH);
		File applicationfile = inpath.toFile();
		log.debug("Mule Application File Path : {} {} {}", applicationfile.getAbsolutePath(), applicationfile.exists(), applicationfile.getName());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", applicationfile,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		fileDataBodyPart.setContentDisposition(
				FormDataContentDisposition.name("file").fileName(applicationfile.getName()).build());

		@SuppressWarnings("resource")
		MultiPart multiPart = new FormDataMultiPart()
				.field("appInfoJson", HttpClientUtil.toJson(application), MediaType.APPLICATION_JSON_TYPE)
				.bodyPart(fileDataBodyPart);
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		
		// ���N�G�X�g���M
		String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestForMultipart(path, Const.PUT, multiPart);

		// ���X�|���X�ϊ�
		ApplicationResponse res = HttpClientUtil.makeResponse(resbody, ApplicationResponse.class);
		return res;
	}

	/**
	 * �A�v���P�[�V�����폜�@�\
	 * @param domain �A�v���P�[�V������
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public Boolean deleteApplication(String domain) throws AppException {
		// ���N�G�X�g���M
		String path = String.format(Const.APPLICATION_END_POINT, "/", domain);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		if (StringUtils.equals("204", resbody)) {
			return true;
		}
		return false;
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
		return res;
	}

	/**
	 * �����^�C���A���[�g�쐬����
	 * @param alertType �A�v���P�[�V������
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public RuntimeAlertResponse saveRuntimeAlert(String alertType) throws AppException {

		// ���N�G�X�g���b�Z�[�W�쐬
		RuntimeAlertRequest alert = RuntimeAlertRequest.factory(alertType);

		// ���N�G�X�g���M
		String path = String.format(Const.RUNTIME_ALERT_END_POINT, StringUtils.EMPTY, StringUtils.EMPTY);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.POST, alert);

		// ���X�|���X�ϊ�
		RuntimeAlertResponse res = HttpClientUtil.makeResponse(resbody, RuntimeAlertResponse.class);
		return res;
	}

	/**
	 * �����^�C���A���[�g�o�^����
	 * @return �o�^����
	 * @throws AppException �A�v���P�[�V������O
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
	 * �����^�C���A���[�g�폜�@�\
	 * @param alertId �����^�C���A���[�gID
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
	 */
	public Boolean deleteRuntimeAlert(String alertId) throws AppException {

		// ���N�G�X�g���M
		String path = String.format(Const.RUNTIME_ALERT_END_POINT, "/", alertId);
		String resbody = HttpClientUtil.sendRequestforCloudHubAPI(path, Const.DELETE, null);

		if (StringUtils.equals("204", resbody)) {
			return true;
		}
		return false;
	}

	/**
	 * �����^�C���A���[�g�폜�@�\
	 * @return �폜����
	 * @throws AppException �A�v���P�[�V������O
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
	 * �A�Z�b�g�����@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V������O
	 */
	public ExchangeAssetResponse findAccount() throws AppException {

		String resbody = HttpClientUtil.sendRequest(Const.COUDHUB_ACCOUNT_END_POINT, Const.GET, null);
		ExchangeAssetResponse[] res = HttpClientUtil.makeResponse(resbody, ExchangeAssetResponse[].class);
		log.debug("findAccount. {}", HttpClientUtil.toJson(res));
		return res[0];
	}
}
