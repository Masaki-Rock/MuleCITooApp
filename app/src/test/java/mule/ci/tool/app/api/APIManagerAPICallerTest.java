package mule.ci.tool.app.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.api.model.APIAssetsResponse;
import mule.ci.tool.app.api.model.ExchangeAssetResponse;
import mule.ci.tool.app.api.model.PoliciesResponse;
import mule.ci.tool.app.api.model.TierResponse;
import mule.ci.tool.app.api.model.TiersResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;

public class APIManagerAPICallerTest {

	private static final Logger log = LoggerFactory.getLogger(APIManagerAPICallerTest.class);

	// @Test
	public void getAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.findAPIInstance();
	}

	// @Test
	public void saveAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			Map<String, String> ins = Const.API_INSTANCES.get(apiInstanceName);
			caller.saveAPIInstance(ins.get("assetId"), ins.get("apiInstanceLabel"));
		}
	}

	// @Test
	public void updateAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		ExchangeAPICaller excaller = new ExchangeAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			Map<String, String> ins = Const.API_INSTANCES.get(apiInstanceName);
			ExchangeAssetResponse exres = excaller.findAsset(ins.get("assetId"));
			caller.updateAPIInstance(param.getId(apiInstanceName), exres.getVersion());
		}
	}

	// @Test
	public void deleteAPIInstance() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.deleteAPIInstance(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void findTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.findTier(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void saveAllTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.saveSLATiers(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void deleteAllTier() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			TiersResponse response = caller.findTier(param.getId(apiInstanceName));
			for (Map<String, Object> tier : response.getTiers()) {
				caller.deleteSLATier(param.getId(apiInstanceName), (Integer) tier.get("id"));
			}
		}
	}

	// @Test
	public void findPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.findPolicy(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void saveAllPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.savePolicies(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void deleteAllPolicy() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.deletePolicies(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void findAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.findAPIAlert(param.getId(apiInstanceName));
		}
	}

	// @Test
	public void saveAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			PoliciesResponse policy = caller.findPolicy(param.getId(apiInstanceName));
			TierResponse response = caller.saveAPIAlert(param.getId(apiInstanceName), apiInstanceName,
					"rate-limiting-sla-based",
					policy.get("rate-limiting-sla-based"));
			log.info("saveAPIAlert. {}", response.getId());
		}
	}

	// @Test
	public void saveAllAPIAlert() throws AppException {
		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {
			caller.saveAlerts(param.getId(apiInstanceName), apiInstanceName);
		}
	}

	// @Test
	public void deleteAllAPIAlert() throws AppException {

		APIManagerAPICaller caller = new APIManagerAPICaller();
		APIAssetsResponse param = caller.findAPIInstance();
		for (String apiInstanceName : Const.API_INSTANCES.keySet()) {

			caller.deleteAlerts(param.getId(apiInstanceName));
		}
	}
}
