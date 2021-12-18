package mule.ci.tool.app.api.model;

public class APIInstanceRequest {

	public APIInstanceRequest() {
	}

	public APIInstanceRequest(String groupId, String assetId,
			String version, String instanceLabel) {
		this.spec.setGroupId(groupId);
		this.spec.setAssetId(assetId);
		this.spec.setVersion(version);
		this.instanceLabel = instanceLabel;
	}

	private String instanceLabel;

	private APIInstanceSpecRequest spec = new APIInstanceSpecRequest();

	private APIInstanceEndpointRequest endpoint = new APIInstanceEndpointRequest();

	public APIInstanceEndpointRequest getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(APIInstanceEndpointRequest endpoint) {
		this.endpoint = endpoint;
	}

	public String getInstanceLabel() {
		return instanceLabel;
	}

	public void setInstanceLabel(String instanceLabel) {
		this.instanceLabel = instanceLabel;
	}

	public APIInstanceSpecRequest getSpec() {
		return spec;
	}

	public void setSpec(APIInstanceSpecRequest spec) {
		this.spec = spec;
	}

	public class APIInstanceEndpointRequest {

		private String deploymentType = "CH";

		private Boolean isCloudHub;

		private Boolean muleVersion4OrAbove = true;

		private String proxyUri;

		private String responseTimeout;

		private String type = "raml";

		private String uri = "https://api.accenture.com/";

		public String getDeploymentType() {
			return deploymentType;
		}

		public void setDeploymentType(String deploymentType) {
			this.deploymentType = deploymentType;
		}

		public Boolean getIsCloudHub() {
			return isCloudHub;
		}

		public void setIsCloudHub(Boolean isCloudHub) {
			this.isCloudHub = isCloudHub;
		}

		public Boolean getMuleVersion4OrAbove() {
			return muleVersion4OrAbove;
		}

		public void setMuleVersion4OrAbove(Boolean muleVersion4OrAbove) {
			this.muleVersion4OrAbove = muleVersion4OrAbove;
		}

		public String getProxyUri() {
			return proxyUri;
		}

		public void setProxyUri(String proxyUri) {
			this.proxyUri = proxyUri;
		}

		public String getResponseTimeout() {
			return responseTimeout;
		}

		public void setResponseTimeout(String responseTimeout) {
			this.responseTimeout = responseTimeout;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
	}

	public class APIInstanceSpecRequest {

		private String assetId;

		private String groupId;

		private String version;

		public APIInstanceSpecRequest() {

		}

		public APIInstanceSpecRequest(String groupId, String assetId, String version) {
			this.assetId = assetId;
			this.groupId = groupId;
			this.version = version;
		}

		public String getAssetId() {
			return assetId;
		}

		public void setAssetId(String assetId) {
			this.assetId = assetId;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}
}
