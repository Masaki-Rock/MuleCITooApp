package mule.ci.tool.app.api;

import org.junit.Test;

import mule.ci.tool.app.util.AppException;

public class ExchangeAPICallerTest {

//	private static final Logger log = LoggerFactory.getLogger(ExchangeAPICallerTest.class);
	
	/**
	 * �A�Z�b�g�����@�\�e�X�g
	 * @throws AppException �A�v���P�[�V������O
	 */
	@Test
	public void findAsset() throws AppException {
		ExchangeAPICaller caller = new ExchangeAPICaller();
		caller.findAsset();
	}
}
