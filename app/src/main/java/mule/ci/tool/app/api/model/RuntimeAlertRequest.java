package mule.ci.tool.app.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.util.Const;

public class RuntimeAlertRequest {

	private static final Logger log = LoggerFactory.getLogger(RuntimeAlertRequest.class);

	public RuntimeAlertRequest() {
	}

	public static RuntimeAlertRequest factory(String type) {

		log.debug("runtime alert type : {}", type);
		RuntimeAlertRequest alert = new RuntimeAlertRequest();
		if (StringUtils.equals(type, "cpu")) {
			alert.setCPUUsageAlert();
		}
		if (StringUtils.equals(type, "deployment-failed")) {
			alert.setDeployfailedAlert();
		}
		if (StringUtils.equals(type, "memory")) {
			alert.setMemoryUsageAlert();
		}
		if (StringUtils.equals(type, "worker-unresponsive")) {
			alert.setWorkerNotRespondingAlert();
		}
		if (StringUtils.equals(type, "application-notification")) {
			alert.setCustomApplicationNortificationAlert();
		}
		if (StringUtils.equals(type, "event-threshold-exceeded")) {
			alert.setExceedEventTrafficThreshholdAlert();
		}
		return alert;
	}

	private void setCPUUsageAlert() {
		this.name = String.format("[%s] CPU usage threshold exceeded alert", Const.ENVIRONMENT_NAME);
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject", String.format("[%s] ${severity}: CPU usage ${state}", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nThe application ${resource} is now in an ${state} state, based on the condition 'CPU ${operator} ${value}%'.");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "cpu");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
		this.condition.put("operator", "GREATER_THAN");
		this.condition.put("value", 80);
		this.condition.put("periodCount", 10);
		this.condition.put("periodMins", 1);
	}

	private void setDeployfailedAlert() {
		this.name = String.format("[%s] Deploy failed alert", Const.ENVIRONMENT_NAME);
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject", String.format("[%s] ${severity}: Deployment Failed", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nThe deployment of the application ${resource} has failed. The error was ${message}.\nPlease see https://anypoint.mulesoft.com/cloudhub/#/console/applications/${resource} for more details.");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "deployment-failed");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
	}

	private void setMemoryUsageAlert() {
		this.name = String.format("[%s] Memory usage threshold exceeded alert", Const.ENVIRONMENT_NAME);
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject", String.format("[%s] ${severity}: memory usage ${state}", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nThe application ${resource} is now in an ${state} state, based on the condition 'memory ${operator} ${value}%'.");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "memory");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
		this.condition.put("operator", "GREATER_THAN");
		this.condition.put("value", 80);
		this.condition.put("periodCount", 10);
		this.condition.put("periodMins", 1);
	}

	private void setWorkerNotRespondingAlert() {
		this.name = String.format("[%s] Worker not responding alert", Const.ENVIRONMENT_NAME);
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject", String.format("[%s] ${severity}: Worker unresponsive", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nOne of the workers from the application ${resource} has encountered a problem.\nPlease check the notification and logs for more details.");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "worker-unresponsive");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
	}

	private void setCustomApplicationNortificationAlert() {
		this.name = String.format("[%s] Custom application nortification alert", Const.ENVIRONMENT_NAME);
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject",
				String.format("[%s] ${severity}: Custom Application Notification", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nThe application ${resource} has a new ${priority} notification:\n${message}");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "application-notification");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
		this.condition.put("priority", "INFO");
		this.condition.put("text", "keyword");
	}

	private void setExceedEventTrafficThreshholdAlert() {
		this.name = String.format("[%s] Event traffic threshold exceeded alert", Const.ENVIRONMENT_NAME);
		// this.severity = "CRITICAL";
		Map<String, Object> action = new HashMap<String, Object>();
		action.put("type", "email");
		action.put("subject", String.format("[%s] ${severity}: Event Threshold Exceeded", Const.ENVIRONMENT_NAME));
		action.put("content",
				"Hello,\nYou are receiving this alert because:\nThe application ${resource} has exceeded the configured event threshold");
		action.put("userIds", Const.ALERT_RECIPIENT_USER_IDS);
		this.actions.add(action);
		this.condition.put("type", "event-threshold-exceeded");
		this.condition.put("resourceType", "cloudhub-application");
		this.condition.put("resources", new String[] { "*" });
		this.condition.put("period", 15);
		this.condition.put("periodTimeUnit", "HOUR");
		this.condition.put("threshold", 5);
	}

	private String name;

	private String severity = "WARNING";

	// public String[] severities = new String[] {"WARNING","CRITICAL","INFO"};

	private List<Map<String, Object>> actions = new ArrayList<Map<String, Object>>();

	private Map<String, Object> condition = new HashMap<String, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public List<Map<String, Object>> getActions() {
		return actions;
	}

	public void setActions(List<Map<String, Object>> actions) {
		this.actions = actions;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}
}