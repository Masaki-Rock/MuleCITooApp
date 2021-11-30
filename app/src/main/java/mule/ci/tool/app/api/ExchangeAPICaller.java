package mule.ci.tool.app.api;

import mule.ci.tool.app.api.model.ExchangeAssetResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;
import mule.ci.tool.app.util.HttpClientUtil;

public class ExchangeAPICaller {

//	private static final Logger log = LoggerFactory.getLogger(ExchangeAPICaller.class);
	
	/**
	 * アセット検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
    public ExchangeAssetResponse findAsset() throws AppException {
    	
    	String path = String.format(Const.EXCHANGE_ASSET_END_POINT, Const.ORGANIZATION_ID, Const.ASSET_ID);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);

		ExchangeAssetResponse[] res = HttpClientUtil.makeResponse(resbody, ExchangeAssetResponse[].class);
		return res[0];
    }
}
