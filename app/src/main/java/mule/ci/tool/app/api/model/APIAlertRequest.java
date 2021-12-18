package mule.ci.tool.app.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import mule.ci.tool.app.util.Const;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIAlertRequest {

	public APIAlertRequest() {
	}

	public static APIAlertRequest factory(String apiInstanceName, String type, Integer policyId) {

		APIAlertRequest alert = new APIAlertRequest();

		if (StringUtils.equals("jwt-validation", type)) {
			alert.setJWTValidation(apiInstanceName, policyId);
		}
		if (StringUtils.equals("rate-limiting-sla-based", type)) {
			alert.setAPIRatelimitingSLABased(apiInstanceName, policyId);
		}
		if (StringUtils.equals("api-response-time", type)) {
			alert.setAPIResponsetime(apiInstanceName);
		}
		if (StringUtils.equals("api-response-code-500", type)) {
			alert.setHTTP500Error(apiInstanceName);
		}
		if (StringUtils.equals("api-response-code-400", type)) {
			alert.setHTTP400Error(apiInstanceName);
		}
		if (StringUtils.equals("api-request-count", type)) {
			alert.setAPIRequestCount(apiInstanceName);
		}
		for (Map<String, Object> user : Const.ALERT_RECIPIENTS) {
			alert.addRecipient((String) user.get("userId"), (String) user.get("lastName"),
					(String) user.get("firstName"));
		}
		return alert;
	}

	private void setJWTValidation(String apiInstanceName, Integer policyId) {
		this.name = String.format("[%s] %s JWT Validation error alert", Const.ENVIRONMENT_NAME, apiInstanceName);
		this.type = "api-policy-violation";
		this.policyId = policyId;
		this.policyTemplate = new APIPolicyTemplate();
		this.policyTemplate.setId("321288");
		this.policyTemplate.setName("JWT Validation");
		setCondition();
		setPeriod();
	}

	private void setAPIRatelimitingSLABased(String apiInstanceName, Integer policyId) {
		this.name = String.format("[%s] %s Rate Limiting SLA based threshold exceeded alert", Const.ENVIRONMENT_NAME,
				apiInstanceName);
		this.type = "api-policy-violation";
		this.policyId = policyId;
		this.policyTemplate = new APIPolicyTemplate();
		this.policyTemplate.setId("320676");
		this.policyTemplate.setName("rate-limiting-sla-based");
		setCondition();
		setPeriod();
	}

	private void setAPIResponsetime(String apiInstanceName) {
		this.name = String.format("[%s] %s Response time out alert", Const.ENVIRONMENT_NAME, apiInstanceName);
		this.type = "api-response-time";
		setCondition();
		List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("property", "responseTime");
		filter.put("operator", "GREATER_THAN");
		filter.put("values", 120000);
		filters.add(filter);
		this.condition.put("filter", filters);
		setPeriod();
	}

	private void setHTTP500Error(String apiInstanceName) {
		this.name = String.format("[%s] %s HTTP500 Error alert", Const.ENVIRONMENT_NAME, apiInstanceName);
		this.type = "api-response-code";
		setCondition();
		List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("property", "responseCode");
		filter.put("operator", "IN");
		filter.put("values", new String[] { "500", "502", "503", "504", "505", "510", "511" });
		filters.add(filter);
		this.condition.put("filter", filters);
		setPeriod();
	}

	private void setHTTP400Error(String apiInstanceName) {
		this.name = String.format("[%s] %s HTTP400 Error alert", Const.ENVIRONMENT_NAME, apiInstanceName);
		this.type = "api-response-code";
		setCondition();
		List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("property", "responseCode");
		filter.put("operator", "IN");
		filter.put("values", new String[] { "400", "401", "403", "404", "408", "429" });
		filters.add(filter);
		this.condition.put("filter", filters);
		setPeriod();
	}

	private void setAPIRequestCount(String apiInstanceName) {
		this.name = String.format("[%s] %s Request Count threshold exceeded alert", Const.ENVIRONMENT_NAME,
				apiInstanceName);
		this.type = "api-request-count";
		setCondition();
		this.condition.put("value", 5000);
		setPeriod();
	}

	private void setCondition() {
		this.condition.put("resourceType", "api-version");
		this.condition.put("aggregate", "COUNT");
		this.condition.put("operator", "GREATER_THAN");
		this.condition.put("value", 0);
	}

	private void setPeriod() {
		this.period = new HashMap<String, Object>();
		this.period.put("repeat", 1);
		Map<String, Object> duration = new HashMap<String, Object>();
		duration.put("count", 1);
		duration.put("weight", "MINUTES");
		this.period.put("duration", duration);
	}

	private String name;

	private String type;

	private String severity = "Warning";

	private Integer policyId;

	private APIPolicyTemplate policyTemplate;

	private String apiAlertsVersion = "1.0.0";

	private Boolean enabled = true;

	private Map<String, Object> condition = new HashMap<String, Object>();

	private Map<String, Object> period = new HashMap<String, Object>();

	private List<APIAlertRecipient> recipients = new ArrayList<APIAlertRecipient>();

	// private String[] severities = new String[] {"Info","Warning","CRITICAL"};

	public String getApiAlertsVersion() {
		return apiAlertsVersion;
	}

	public void setApiAlertsVersion(String apiAlertsVersion) {
		this.apiAlertsVersion = apiAlertsVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public List<APIAlertRecipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<APIAlertRecipient> recipients) {
		this.recipients = recipients;
	}

	public void addRecipient(APIAlertRecipient recipient) {
		this.recipients.add(recipient);
	}

	public void addRecipientforMail(String mailaddress) {
		APIAlertRecipient recipient = new APIAlertRecipient();
		recipient.setType("email");
		recipient.setValue(mailaddress);
		this.recipients.add(recipient);
	}

	public void addRecipient(String userId, String lastName, String firstName) {
		APIAlertRecipient recipient = new APIAlertRecipient();
		recipient.setType("user");
		recipient.setValue(userId);
		recipient.setLastName(lastName);
		recipient.setFirstName(firstName);
		this.recipients.add(recipient);
	}

	public Integer getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public APIPolicyTemplate getPolicyTemplate() {
		return policyTemplate;
	}

	public void setPolicyTemplate(APIPolicyTemplate policyTemplate) {
		this.policyTemplate = policyTemplate;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}

	public Map<String, Object> getPeriod() {
		return period;
	}

	public void setPeriod(Map<String, Object> period) {
		this.period = period;
	}

	public class APIPolicyTemplate {

		private String id;

		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public class APIAlertRecipient {

		private String type;

		private String value;

		private String firstName;

		private String lastName;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	}
}
