package mule.ci.tool.app.api;

import java.util.Map;

import org.junit.Test;

import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;

public class ExchangeAPICallerTest {

	// private static final Logger log =
	// LoggerFactory.getLogger(ExchangeAPICallerTest.class);

	/**
	 * Exchangeアセット検索機能
	 * 
	 * @throws AppException アプリケーション例外
	 */
	@Test
	public void findAsset() throws AppException {
		ExchangeAPICaller caller = new ExchangeAPICaller();
		for (String key : Const.API_INSTANCES.keySet()) {
			Map<String, String> ins = (Map<String, String>) Const.API_INSTANCES.get(key);
			caller.findAsset(ins.get("assetId"));
		}
	}
}
