package mule.ci.tool.app.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import mule.ci.tool.app.api.AccessManagementAPICaller;

public class HttpClientUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * RESTAPI���M����
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequest(String urlParam, String method, Object body) throws AppException {

		return sendRequestForJson(urlParam, method, body, makeAccessTokenHeader());
	}

	/**
	 * CloudHubAPI�pRESTAPI���M����
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequestforCloudHubAPI(String urlParam, String method, Object body) throws AppException {

		return sendRequestForJson(urlParam, method, body, makeAccessTokenHeaderforCloudHubAPI());
	}
	
	/**
	 * CloudHubAPI�pRESTAPI���M����
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequestForMultipartAndCloudHubAPI(String urlParam, String method, String body, String boundary) throws AppException {

		return sendRequestForMutipart(urlParam, method, body, makeAccessTokenHeaderforCloudHubAPI(), boundary);
	}

	/**
	 * RESTAPI���M����
	 * 
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @param headers  �w�b�_�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequestForJson(String urlParam, String method, Object body, Map<String, String> headers)
			throws AppException {

		return sendRequestForJson(urlParam, method, toJson(body), headers);
	}

	/**
	 * RESTAPI���M����
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @param headers  �w�b�_�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequestForJson(String urlParam, String method, String body, Map<String, String> headers)
			throws AppException {
		StringEntity entity = null;
		if (body != null) {
			entity = new StringEntity(body, "UTF-8");
			entity.setContentType("application/json");
		}
		log.debug("target path. {}�@: {}", method, urlParam);
		log.debug("headers. {}", headers);
		log.debug("request body. {}", format(body));
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000)
				.setConnectionRequestTimeout(10000).build();
		HttpUriRequest httpUriRequest = RequestBuilder.create(method).setConfig(requestConfig)
				.setHeader("Content-Type", "application/json").setEntity(entity).setUri(urlParam).build();
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpUriRequest.setHeader(header.getKey(), header.getValue());
			}
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpUriRequest);
		} catch (ClientProtocolException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		HttpEntity responseEntity = response.getEntity();
		String res = null;
		if (responseEntity != null) {
			try {
				res = EntityUtils.toString(responseEntity);
				log.debug("response body. {}", format(res));
				return res;
			} catch (ParseException e) {
				throw new AppException(e);
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
		res = response.getStatusLine().toString().trim().split(" ")[1];
		log.debug("statusLine. {}", res);
		return res;
	}
	
	/**
	 * RESTAPI���M����
	 * @param urlParam URL�p�X
	 * @param method   HTTP���\�b�h
	 * @param body     ���N�G�X�g�{�f�B�[
	 * @param headers  �w�b�_�[
	 * @return ���X�|���X�{�f�B�[
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String sendRequestForMutipart(String urlParam, String method, String body, Map<String, String> headers, String boundary)
			throws AppException {
		StringEntity entity = null;
		if (body != null) {
			entity = new StringEntity(body, "UTF-8");
			entity.setContentType("multipart/form-data");
		}
		log.debug("target path. {}�@: {}", method, urlParam);
		log.debug("headers. {}", headers);
		log.debug("request body. {}", body);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000)
				.setConnectionRequestTimeout(10000).build();
		HttpUriRequest httpUriRequest = RequestBuilder.create(method).setConfig(requestConfig)
				.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary).setEntity(entity).setUri(urlParam).build();
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpUriRequest.setHeader(header.getKey(), header.getValue());
			}
		}
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpUriRequest);
		} catch (ClientProtocolException e) {
			throw new AppException(e);
		} catch (IOException e) {
			throw new AppException(e);
		}
		HttpEntity responseEntity = response.getEntity();
		String res = null;
		if (responseEntity != null) {
			try {
				res = EntityUtils.toString(responseEntity);
				log.debug("response body. {}", format(res));
				return res;
			} catch (ParseException e) {
				throw new AppException(e);
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
		res = response.getStatusLine().toString().trim().split(" ")[1];
		log.debug("statusLine. {}", res);
		return res;
	}

	/**
	 * �A�N�Z�X�g�[�N���w�b�_�[
	 * 
	 * @return �w�b�_�[���
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static Map<String, String> makeAccessTokenHeader() throws AppException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + new AccessManagementAPICaller().getAccessToken());
		return headers;
	}

	/**
	 * �A�N�Z�X�g�[�N���w�b�_�[
	 * 
	 * @return �w�b�_�[���
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static Map<String, String> makeAccessTokenHeaderforCloudHubAPI() throws AppException {
		Map<String, String> headers = makeAccessTokenHeader();
		headers.put("X-ANYPNT-ORG-ID", Const.ORGANIZATION_ID);
		headers.put("X-ANYPNT-ENV-ID", Const.DEV_ENVIRONMENT_ID);
		return headers;
	}

	public static <T> T makeResponse(String content, Class<T> valueType) throws AppException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			return mapper.readValue(content, valueType);
		} catch (JsonMappingException e) {
			throw new AppException(e);
		} catch (JsonProcessingException e) {
			throw new AppException(e);
		}
	}

	/**
	 * �C���f���g�`���ɕϊ�����
	 * 
	 * @param msg JSON������
	 * @return ���`��̕�����
	 */
	private static String format(String msg) {
		if (msg == null)
			return StringUtils.EMPTY;
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonElement el = parser.parse(msg);
		return gson.toJson(el);
	}

	/**
	 * �}���`�p�[�g�ϊ�����
	 * @param param ���N�G�X�g
	 * @param boundary �o�E���_���[
	 * @return �}���`�p�[�g�`�����N�G�X�g
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String toMultipart(Object param, String boundary) throws AppException {
	
		String body = "--" + boundary + "\r\n";
		body += "Content-Disposition: form-data; name=\"appInfoJson\"\r\n";
		body += "Content-Type: application/json\r\n\r\n";
		body += toJson(param);
		body += "\r\n--" + boundary + "--";
		return body;
	}
	
	/**
	 * JSON�ϊ��@�\
	 * @param body �I�u�W�F�N�g
	 * @return JSON�f�[�^
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static String toJson(Object body) throws AppException {
		String request = null;
		if (body != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			try {
				request = mapper.writeValueAsString(body);
			} catch (JsonProcessingException e) {
				throw new AppException(e);
			}
		}
		return request;
	}
}
