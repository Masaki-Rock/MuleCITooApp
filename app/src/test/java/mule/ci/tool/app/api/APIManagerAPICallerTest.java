package mule.ci.tool.app.api;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.api.model.APIAssetsResponse;
import mule.ci.tool.app.api.model.PoliciesResponse;
import mule.ci.tool.app.api.model.TierResponse;
import mule.ci.tool.app.api.model.TiersResponse;
import mule.ci.tool.app.util.AppException;

public class APIManagerAPICallerTest {

	private static final Logger log = LoggerFactory.getLogger(APIManagerAPICallerTest.class);

	@Test
	public void getAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.getAPIInstance();
	}
	
	@Test
	public void saveAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAPIInstance();
	}

	@Test
	public void updateAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.updateAPIInstance();
	}
	
	@Test
	public void deleteAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.deleteAPIInstance();
	}

	@Test
	public void findTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.getAPIInstance();
		caller.findTier(param.getId());
	}
	
	@Test
	public void saveAllTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAllTier();
	}
	
	@Test
	public void deleteAllTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.getAPIInstance();
		TiersResponse response = caller.findTier(param.getId());
		for(Map<String, Object> tier: response.getTiers()) {
			caller.deleteTier(param.getId(),(Integer) tier.get("id"));
		}
	}

	@Test
	public void findPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.getAPIInstance();
		caller.findPolicy(param.getId());
	}

	@Test
	public void saveAllPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAllPolicy();
	}

	@Test
	public void deleteAllPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.deleteAllPolicy();
	}

	@Test
	public void findAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.getAPIInstance();
		caller.findAPIAlert(param.getId());
	}

	@Test
	public void saveAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse api = caller.getAPIInstance();
		PoliciesResponse policy = caller.findPolicy(api.getId());
		TierResponse response = caller.saveAPIAlert(api.getId(), "rate-limiting-sla-based",
				policy.get("rate-limiting-sla-based"));
		log.info("saveAPIAlert. {}", response.getId());
	}

	@Test
	public void saveAllAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAllAPIAlert();
	}

	@Test
	public void deleteAllAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.deleteAllAPIAlert();
	}
}
