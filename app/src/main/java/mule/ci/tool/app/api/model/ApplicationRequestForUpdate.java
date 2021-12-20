package mule.ci.tool.app.api.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.util.Const;

public class ApplicationRequestForUpdate {

	private static final Logger log = LoggerFactory.getLogger(ApplicationRequestForUpdate.class);

	public ApplicationRequestForUpdate(Map<String, String> apiIds) {

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
		this.setWorkerType();
	}

	private void setWorkerType() {

		this.workers = new HashMap<String, Object>();
		this.workers.put("amount", Const.WORKERS);
		log.debug("Workers : {}", Const.WORKERS);
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
			log.debug("vCore : {}", "xLarge");
		}
		if (StringUtils.equals("xxLarge", Const.WORKER_TYPE)) {
			workerType.put("name", "xxLarge");
			workerType.put("weight", 8.0);
			workerType.put("cpu", "8 vCores");
			workerType.put("memory", "15 GB memory");
			log.debug("vCore : {}", "xxLarge");
		}
		if (StringUtils.equals("4xLarge", Const.WORKER_TYPE)) {
			workerType.put("name", "4xLarge");
			workerType.put("weight", 16.0);
			workerType.put("cpu", "16 vCores");
			workerType.put("memory", "32 GB memory");
		}
		this.workers.put("type", workerType);
	}

	private String fileChecksum = "";

	private String fileSource = "";

	private Version muleVersion = new Version();

	private Map<String, String> properties = new HashMap<String, String>();

	private String[] logLevels = new String[0];

	private Map<String, String> trackingSettings = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("trackingLevel", "DISABLED");
		}
	};

	private String region = Const.REGION;

	private Boolean multitenanted = false;

	private Boolean vpnEnabled = false;

	private Boolean secureDataGatewayEnabled = false;

	private Boolean persistentQueues = Const.PERSISTENT_QUEUES;

	private Boolean persistentQueuesEncrypted = false;

	private Boolean monitoringEnabled = Const.ENABLE_MONITORING;

	private Boolean monitoringAutoRestart = true;

	private Map<String, Object> workers;

	public String getFileChecksum() {
		return fileChecksum;
	}

	public void setFileChecksum(String fileChecksum) {
		this.fileChecksum = fileChecksum;
	}

	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public Version getMuleVersion() {
		return muleVersion;
	}

	public void setMuleVersion(Version muleVersion) {
		this.muleVersion = muleVersion;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String[] getLogLevels() {
		return logLevels;
	}

	public void setLogLevels(String[] logLevels) {
		this.logLevels = logLevels;
	}

	public Map<String, String> getTrackingSettings() {
		return trackingSettings;
	}

	public void setTrackingSettings(Map<String, String> trackingSettings) {
		this.trackingSettings = trackingSettings;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Boolean getMultitenanted() {
		return multitenanted;
	}

	public void setMultitenanted(Boolean multitenanted) {
		this.multitenanted = multitenanted;
	}

	public Boolean getVpnEnabled() {
		return vpnEnabled;
	}

	public void setVpnEnabled(Boolean vpnEnabled) {
		this.vpnEnabled = vpnEnabled;
	}

	public Boolean getSecureDataGatewayEnabled() {
		return secureDataGatewayEnabled;
	}

	public void setSecureDataGatewayEnabled(Boolean secureDataGatewayEnabled) {
		this.secureDataGatewayEnabled = secureDataGatewayEnabled;
	}

	public Boolean getPersistentQueues() {
		return persistentQueues;
	}

	public void setPersistentQueues(Boolean persistentQueues) {
		this.persistentQueues = persistentQueues;
	}

	public Boolean getPersistentQueuesEncrypted() {
		return persistentQueuesEncrypted;
	}

	public void setPersistentQueuesEncrypted(Boolean persistentQueuesEncrypted) {
		this.persistentQueuesEncrypted = persistentQueuesEncrypted;
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

	public class Version {

		private String version = Const.RUNTIME_VERSION;

		private String updateId;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getUpdateId() {
			return updateId;
		}

		public void setUpdateId(String updateId) {
			this.updateId = updateId;
		}

	}
}
