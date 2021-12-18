package mule.ci.tool.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

import mule.ci.tool.app.api.AccessManagementAPICaller;

public class HttpClientUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequest(String urlParam, String method, Object body) throws AppException {

		return sendRequestForJson(urlParam, method, body, makeAccessTokenHeader());
	}

	/**
	 * CloudHubAPI用RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequestforCloudHubAPI(String urlParam, String method, Object body) throws AppException {

		return sendRequestForJson(urlParam, method, body, makeAccessTokenHeaderforCloudHubAPI());
	}

	/**
	 * CloudHubAPI用RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequestForMultipartAndCloudHubAPI(String urlParam, String method, String body,
			String boundary) throws AppException {

		return sendRequestForMutipart(urlParam, method, body, makeAccessTokenHeaderforCloudHubAPI(), boundary);
	}

	/**
	 * RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @param headers  ヘッダー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequestForJson(String urlParam, String method, Object body, Map<String, String> headers)
			throws AppException {

		return sendRequestForJson(urlParam, method, toJson(body), headers);
	}

	/**
	 * RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @param headers  ヘッダー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequestForJson(String urlParam, String method, String body, Map<String, String> headers)
			throws AppException {
		StringEntity entity = null;
		if (body != null) {
			entity = new StringEntity(body, "UTF-8");
			entity.setContentType("application/json");
		}
		log.debug("target path. {}　: {}", method, urlParam);
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
	 * RESTAPI送信処理
	 * 
	 * @param urlParam URLパス
	 * @param method   HTTPメソッド
	 * @param body     リクエストボディー
	 * @param headers  ヘッダー
	 * @return レスポンスボディー
	 * @throws AppException アプリケーション例外
	 */
	public static String sendRequestForMutipart(String urlParam, String method, String body,
			Map<String, String> headers, String boundary) throws AppException {
		StringEntity entity = null;
		if (body != null) {
			entity = new StringEntity(body, "UTF-8");
			entity.setContentType("multipart/form-data");
		}
		log.debug("target path. {}　: {}", method, urlParam);
		log.debug("headers. {}", headers);
		log.debug("request body. {}", body);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(120000)
				.setConnectionRequestTimeout(120000).build();
		HttpUriRequest httpUriRequest = RequestBuilder.create(method).setConfig(requestConfig)
				.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary).setEntity(entity)
				.setUri(urlParam).build();
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

	public static String sendRequestForMultipart(String urlParam, String method, MultiPart multiPart)
			throws AppException {

		// リクエスト送信
		log.debug("target path. {}　: {}", method, urlParam);
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(MultiPartWriter.class);
		Client client = Client.create(config);
		WebResource resource = client.resource(urlParam);
		ClientResponse response = null;
		if (StringUtils.equals(Const.POST, method)) {
			response = resource.type("multipart/form-data")
					.header("Authorization", "Bearer " + new AccessManagementAPICaller().getAccessToken())
					.header("X-ANYPNT-ORG-ID", Const.ORGANIZATION_ID)
					.header("X-ANYPNT-ENV-ID", Const.ENVIRONMENT_ID)
					.post(ClientResponse.class, multiPart);
		}
		if (StringUtils.equals(Const.PUT, method)) {
			response = resource.type("multipart/form-data")
					.header("Authorization", "Bearer " + new AccessManagementAPICaller().getAccessToken())
					.header("X-ANYPNT-ORG-ID", Const.ORGANIZATION_ID)
					.header("X-ANYPNT-ENV-ID", Const.ENVIRONMENT_ID)
					.put(ClientResponse.class, multiPart);
		}
		String res = HttpClientUtil.getStringFromInputStream(response.getEntityInputStream());
		log.debug("response body. {}", format(res));
		client.destroy();

		return res;
	}

	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * アクセストークンヘッダー
	 * 
	 * @return ヘッダー情報
	 * @throws AppException アプリケーション例外
	 */
	public static Map<String, String> makeAccessTokenHeader() throws AppException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + new AccessManagementAPICaller().getAccessToken());
		return headers;
	}

	/**
	 * アクセストークンヘッダー
	 * 
	 * @return ヘッダー情報
	 * @throws AppException アプリケーション例外
	 */
	public static Map<String, String> makeAccessTokenHeaderforCloudHubAPI() throws AppException {
		Map<String, String> headers = makeAccessTokenHeader();
		headers.put("X-ANYPNT-ORG-ID", Const.ORGANIZATION_ID);
		headers.put("X-ANYPNT-ENV-ID", Const.ENVIRONMENT_ID);
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
	 * インデント形式に変換する
	 * 
	 * @param msg JSON文字列
	 * @return 整形後の文字列
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
	 * JSON変換機能
	 * 
	 * @param body オブジェクト
	 * @return JSONデータ
	 * @throws AppException アプリケーション例外
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
