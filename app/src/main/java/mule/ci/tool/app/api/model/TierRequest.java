package mule.ci.tool.app.api.model;

import java.util.ArrayList;
import java.util.List;

public class TierRequest {

	public TierRequest() {}
	
	public TierRequest(String name, String description, Boolean autoApprove,
			Integer timePeriodInMilliseconds ,Integer maximumRequests) {
		this.name = name;
		this.description = description;
		this.autoApprove = autoApprove;
		TierLimitRequest limit = new TierLimitRequest();
		limit.setTimePeriodInMilliseconds(timePeriodInMilliseconds);
		limit.setMaximumRequests(maximumRequests);
		this.addLimit(limit);
	}
	
	private String name;
	
	private String description;

	private Boolean autoApprove = true;
	
	private List<TierLimitRequest> limits;
	
	private String status = "ACTIVE";

	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getAutoApprove() {
		return autoApprove;
	}
	public void setAutoApprove(Boolean autoApprove) {
		this.autoApprove = autoApprove;
	}
	public List<TierLimitRequest> getLimits() {
		return limits;
	}
	public void setLimits(List<TierLimitRequest> limits) {
		this.limits = limits;
	}
	public void addLimit(TierLimitRequest limit) {
		if (this.limits == null) {
			this.limits = new ArrayList<TierLimitRequest>();
		}
		this.limits.add(limit);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public class TierLimitRequest {

		private Boolean visible = true;
		
		private Integer timePeriodInMilliseconds = 3600000;
		
		private Integer maximumRequests = 10000;

		public Boolean getVisible() {
			return visible;
		}

		public void setVisible(Boolean visible) {
			this.visible = visible;
		}

		public Integer getTimePeriodInMilliseconds() {
			return timePeriodInMilliseconds;
		}

		public void setTimePeriodInMilliseconds(Integer timePeriodInMilliseconds) {
			this.timePeriodInMilliseconds = timePeriodInMilliseconds;
		}

		public Integer getMaximumRequests() {
			return maximumRequests;
		}

		public void setMaximumRequests(Integer maximumRequests) {
			this.maximumRequests = maximumRequests;
		}
	}
}
