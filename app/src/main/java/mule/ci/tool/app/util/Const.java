package mule.ci.tool.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


@SuppressWarnings("unchecked")
public class Const {

	public static final String ANYPOINT_END_POINT = "https://anypoint.mulesoft.com";

	public static final String TOKEN_END_POINT = ANYPOINT_END_POINT + "/accounts/api/v2/oauth2/token";

	public static final String USER_END_POINT = ANYPOINT_END_POINT + "/accounts/api/organizations/%s/users?type=all";

	public static final String MEMBER_END_POINT = ANYPOINT_END_POINT + "/accounts/api/organizations/%s/members";

	public static final String EXCHANGE_ASSET_END_POINT = ANYPOINT_END_POINT
			+ "/exchange/api/v2/assets/search?organizationId=%s&search=%s";
	
	public static final String API_ASSET_END_POINT = ANYPOINT_END_POINT
			+ "/apimanager/api/v1/organizations/%s/apiSpecs?latestVersionsOnly=true&limit=50&offset=0&searchTerm=acc";

	public static final String API_MANAGER_API_END_POINT = ANYPOINT_END_POINT
			+ "/apimanager/api/v1/organizations/%s/environments/%s";

	public static final String API_INSTANCE_END_POINT = API_MANAGER_API_END_POINT + "/apis%s%s";

	public static final String TIERS_END_POINT = API_MANAGER_API_END_POINT + "/apis/%s/tiers%s%s";

	public static final String POLICY_END_POINT = API_MANAGER_API_END_POINT + "/apis/%s/policies%s%s";

	public static final String API_ALERT_END_POINT = API_MANAGER_API_END_POINT + "/apis/%s/alerts%s%s";

	public static final String APPLICATION_END_POINT = ANYPOINT_END_POINT + "/cloudhub/api/v2/applications%s%s";

	public static final String RUNTIME_ALERT_END_POINT = ANYPOINT_END_POINT + "/cloudhub/api/v2/alerts%s%s";

	public static final String COUDHUB_ACCOUNT_END_POINT = ANYPOINT_END_POINT + "/cloudhub/api/account";

	public static final String GET = "GET";

	public static final String POST = "POST";

	public static final String PUT = "PUT";

	public static final String PATCH = "PATCH";

	public static final String DELETE = "DELETE";

	public static String CONFIG_YAML_FILE_PATH = "config.yaml";
	
	public static String PROJECT_YAML_FILE_PATH = "project.yaml";
	
	public static String APPLICATION_FILE_PATH;

	public static String ENV;
	
	public static String ORGANIZATION_ID;

	public static String DEV_ENVIRONMENT_ID;

	public static String CLIENT_ID;

	public static String CLIENT_SECRET;

	public static String GRANT_TYPE;

	public static String ACCESS_TOKEN;

	public static String ASSET_ID;

	public static String API_INSTANCE_LABEL;
	
	public static String DOMAIN;
	
	public static String API_ID_KEY;
	
	public static Integer API_ID;
	
	public static String RUNTIME_VERSION;
	
	public static String WORKER_TYPE;
	
	public static Integer WORKERS;
	
	public static String REGION;
	
	public static Map<String, String> RUNTIME_PROPERTIES;
	
	public static Boolean AUTOMATICALLY_RESTART;
	
	public static Boolean PERSISTENT_QUEUES;
	
	public static Boolean USE_OBJECT_STORE_V2;
	
	public static Boolean ENABLE_MONITORING;
	
	public static List<Map<String, Object>> TIERS;
	
	public static Map<String,Object> POLICIES;
	
	public static List<String> ALERTS;
	
	public static List<String> RUNTIME_ALERTS;
	
	public static List<String> ALERT_RECIPIENT_USER_IDS;
	
	public static List<Map<String, Object>> ALERT_RECIPIENTS;

	static {

		Yaml common = new Yaml();
		Path input = Paths.get(CONFIG_YAML_FILE_PATH);
		System.out.println("reading config file path : " + input.toAbsolutePath());
		InputStream in = null;
		try {
			in = Files.newInputStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> commonConf = (Map<String, Object>) common.loadAs(in, Map.class);

		// クライアント認証情報
		Map<String, Object> connApp = (Map<String, Object>) commonConf.get("connectedApplication");
		CLIENT_ID = (String) connApp.get("clientId");
		CLIENT_SECRET = (String) connApp.get("clientSecret");
		GRANT_TYPE = (String) connApp.get("grantType");

		// 組織情報
		Map<String, Object> org = (Map<String, Object>) commonConf.get("organization");
		List<Map<String, Object>> bussinesGroups = (List<Map<String, Object>>) org.get("bussinesGroup");
		
		ORGANIZATION_ID = (String) org.get("organizationId");
		DEV_ENVIRONMENT_ID = (String) env.get("deveper");
		
		// プロジェクトコンフィグファイル
		Map<String, Object> proj = (Map<String, Object>) commonConf.get("project");
		Map<String, Object> conf = (Map<String, Object>) proj.get("config");
		PROJECT_YAML_FILE_PATH = (String) conf.get("path");

		// プロジェクト毎の個別設定
		Yaml project = new Yaml();
		input = Paths.get(PROJECT_YAML_FILE_PATH);
		in = null;
		try {
			in = Files.newInputStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("reading priject config file path : " + input.toAbsolutePath());
		Map<String, Object> projectConf = (Map<String, Object>) project.loadAs(in, Map.class);

		// 環境
		ENV = (String) projectConf.get("env");
		
		// APIインスタンス情報
		Map<String, Object> api = (Map<String, Object>) projectConf.get("apiInstance");
		ASSET_ID = (String) api.get("assetId");
		API_INSTANCE_LABEL = (String) api.get("apiInstanceLabel");

		// ランタイム
		Map<String, Object> runtime = (Map<String, Object>) projectConf.get("runtime");
		DOMAIN = (String) runtime.get("domain");
		APPLICATION_FILE_PATH = (String) runtime.get("filename");
		API_ID_KEY = (String) runtime.get("apiIDkey");
		RUNTIME_VERSION = (String) runtime.get("runtimeVersion");
		Map<String, Object> worker = (Map<String, Object>) runtime.get("worker");
		WORKER_TYPE = (String) worker.get("type");
		WORKERS = (Integer) worker.get("workers");
		REGION = (String) worker.get("region");
		RUNTIME_PROPERTIES = (Map<String, String>) runtime.get("properties");
		AUTOMATICALLY_RESTART = (Boolean) runtime.get("automaticallyRestart");
		PERSISTENT_QUEUES = (Boolean) runtime.get("persistentQueues");
		USE_OBJECT_STORE_V2 = (Boolean) runtime.get("useObjectStorev2");
		ENABLE_MONITORING = (Boolean) runtime.get("enableMonitoring");
		
		// SLA層情報
		TIERS = (List<Map<String, Object>>) projectConf.get("tiers");
		
		// ポリシーリスト
		POLICIES = (Map<String,Object>) projectConf.get("policies");
		
		// アラートリスト
		ALERTS = (List<String>) projectConf.get("alerts");
				
		// ランタイムアラートリスト
		RUNTIME_ALERTS = (List<String>) projectConf.get("runtimeAlerts");
		
		// アラート受信者
		ALERT_RECIPIENTS = (List<Map<String, Object>>) projectConf.get("alertRecipients");
		ALERT_RECIPIENT_USER_IDS = new ArrayList<String>();
		for (Map<String, Object> user: ALERT_RECIPIENTS) {
			ALERT_RECIPIENT_USER_IDS.add((String) user.get("userId"));
		}
	}
}
