package mule.ci.tool.app.api;

import org.junit.Test;

import mule.ci.tool.app.util.AppException;

public class CloudhubAPICallerTest {

//	private static final Logger log = LoggerFactory.getLogger(CloudhubAPICallerTest.class);
	
	@Test
	public void findApplication() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.findApplication();
	}
	
	@Test
	public void updateApplication() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.updateApplication("account-v1");
	}

	@Test
	public void findRuntimeAlert() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.findRuntimeAlert();
	}
	
	@Test
	public void saveRuntimeAlert() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.saveRuntimeAlert("event-threshold-exceeded");
	}
	
	@Test
	public void deleteRuntimeAlert() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.deleteRuntimeAlert("5424fce6-b84c-4d9a-b840-429c83c36ee4");
	}

	@Test
	public void saveAllRuntimeAlert() throws AppException {
		
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.saveAllRuntimeAlert();
	}
	
	@Test
	public void deleteAllRuntimeAlert() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.deleteAllRuntimeAlert();
	}
	
	
	/**
	 * アカウント検索機能テスト
	 * @throws AppException アプリケーション例外
	 */
	@Test
	public void findAcount() throws AppException {
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.findAccount();
	}
}
