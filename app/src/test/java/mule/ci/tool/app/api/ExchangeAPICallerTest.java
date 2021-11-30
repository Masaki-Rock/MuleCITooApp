package mule.ci.tool.app.api;

import org.junit.Test;

import mule.ci.tool.app.util.AppException;

public class ExchangeAPICallerTest {

//	private static final Logger log = LoggerFactory.getLogger(ExchangeAPICallerTest.class);
	
	/**
	 * アセット検索機能テスト
	 * @throws AppException アプリケーション例外
	 */
	@Test
	public void findAsset() throws AppException {
		ExchangeAPICaller caller = new ExchangeAPICaller();
		caller.findAsset();
	}
}
