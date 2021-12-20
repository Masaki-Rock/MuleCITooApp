package mule.ci.tool.app.api.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.util.Const;

public class ApplicationRequest {

	private static final Logger log = LoggerFactory.getLogger(ApplicationRequest.class);

	public ApplicationRequest(String domain, Map<String, String> apiIds) {

		this.domain = domain;
		this.muleVersion = new HashMap<String, Object>();
		this.muleVersion.put("version", Const.RUNTIME_VERSION);
		if (apiIds != null) {
			for (String key : Const.API_ID_KEYS.keySet()) {
				if (apiIds.get(Const.API_ID_KEYS.get(key)) != null) {
					this.properties.put(key, apiIds.get(Const.API_ID_KEYS.get(key)));
					this.properties.put("anypoint.platform.client_id", Const.ENVIRONMENT_CLIENT_ID);
					this.properties.put("anypoint.platform.client_secret", Const.ENVIRONMENT_CLIENT_SECRET);
				}
			}
		}
		this.properties.put("env", Const.ENVIRONMENT_NAME.toLowerCase());
		for (String key : Const.RUNTIME_PROPERTIES.keySet()) {
			this.properties.put(key, Const.RUNTIME_PROPERTIES.get(key));
		}
		Map<String, Object> workerType = new HashMap<String, Object>();
		log.debug("vCore : {}", Const.WORKER_TYPE);
		if (StringUtils.equals("Micro", Const.WORKER_TYPE)) {
			workerType.put("name", "Micro");
			workerType.put("weight", 0.1);
			workerType.put("cpu", "0.1 vCores");
			workerType.put("memory", "500 MB memory");
			log.debug("vCore : {}", "Micro");
		}
		if (StringUtils.equals("Small", Const.WORKER_TYPE)) {
			workerType.put("name", "Small");
			workerType.put("weight", 0.2);
			workerType.put("cpu", "0.2 vCores");
			workerType.put("memory", "1 GB memory");
			log.debug("vCore : {}", "Small");
		}
		if (StringUtils.equals("Medium", Const.WORKER_TYPE)) {
			workerType.put("name", "Medium");
			workerType.put("weight", 1.0);
			workerType.put("cpu", "1 vCore");
			workerType.put("memory", "1.5 GB memory");
			log.debug("vCore : {}", "Medium");
		}
		if (StringUtils.equals("Large", Const.WORKER_TYPE)) {
			workerType.put("name", "Large");
			workerType.put("weight", 2.0);
			workerType.put("cpu", "2 vCores");
			workerType.put("memory", "3.5 GB memory");
			log.debug("vCore : {}", "Large");
		}
		if (StringUtils.equals("xLarge", Const.WORKER_TYPE)) {
			workerType.put("name", "xLarge");
			workerType.put("weight", 4.0);
			workerType.put("cpu", "4 vCores");
			workerType.put("memory", "7.5 GB memory");
		}
		if (StringUtils.equals("xxLarge", Const.WORKER_TYPE)) {
			workerType.put("name", "xxLarge");
			workerType.put("weight", 8.0);
			workerType.put("cpu", "8 vCores");
			workerType.put("memory", "15 GB memory");
		}
		if (StringUtils.equals("4xLarge", Const.WORKER_TYPE)) {
			workerType.put("name", "4xLarge");
			workerType.put("weight", 16.0);
			workerType.put("cpu", "16 vCores");
			workerType.put("memory", "32 GB memory");
		}
		this.workers = new HashMap<String, Object>();
		this.workers.put("amount", Const.WORKERS);
		log.debug("Workers : {}", Const.WORKERS);
		this.workers.put("type", workerType);

	}

	private String domain;

	private Map<String, Object> muleVersion;

	private String region = "us-east-1";

	private Boolean monitoringEnabled = true;

	private Boolean monitoringAutoRestart = true;

	private Map<String, String> properties = new HashMap<String, String>();

	private Map<String, Object> workers;

	private Boolean loggingNgEnabled = true;

	private Boolean persistentQueues = false;

	private Boolean objectStoreV2 = false;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Map<String, Object> getMuleVersion() {
		return muleVersion;
	}

	public void setMuleVersion(Map<String, Object> muleVersion) {
		this.muleVersion = muleVersion;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Boolean getMonitoringEnabled() {
		return monitoringEnabled;
	}

	public void setMonitoringEnabled(Boolean monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	public Boolean getMonitoringAutoRestart() {
		return monitoringAutoRestart;
	}

	public void setMonitoringAutoRestart(Boolean monitoringAutoRestart) {
		this.monitoringAutoRestart = monitoringAutoRestart;
	}

	public Map<String, Object> getWorkers() {
		return workers;
	}

	public void setWorkers(Map<String, Object> workers) {
		this.workers = workers;
	}

	public Boolean getLoggingNgEnabled() {
		return loggingNgEnabled;
	}

	public void setLoggingNgEnabled(Boolean loggingNgEnabled) {
		this.loggingNgEnabled = loggingNgEnabled;
	}

	public Boolean getPersistentQueues() {
		return persistentQueues;
	}

	public void setPersistentQueues(Boolean persistentQueues) {
		this.persistentQueues = persistentQueues;
	}

	public Boolean getObjectStoreV2() {
		return objectStoreV2;
	}

	public void setObjectStoreV2(Boolean objectStoreV2) {
		this.objectStoreV2 = objectStoreV2;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
