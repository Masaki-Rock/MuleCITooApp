package mule.ci.tool.app.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import mule.ci.tool.app.api.model.ClientCredential;
import mule.ci.tool.app.api.model.CommonResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;
import mule.ci.tool.app.util.HttpClientUtil;

/**
 * 認証やユーザ検索関連クラス
 * @author masaki.kawaguchi
 */
public class AccessManagementAPICaller {

	/**
	 * ユーザ検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーション例外
	 */
	public CommonResponse findUser() throws AppException {
		
		String path = String.format(Const.USER_END_POINT, Const.ORGANIZATION_ID);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);
		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		return res;
	}
	
	/**
	 * メンバー検索機能
	 * @return 検索結果
	 * @throws AppException アプリケーションエラー
	 */
	public CommonResponse findMember() throws AppException {
		
		String path = String.format(Const.MEMBER_END_POINT, Const.ORGANIZATION_ID);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);
		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		return res;
	}
	
	private CommonResponse users;
	
	/**
	 * メンバー検索機能
	 * @param mailAddress メールアドレス
	 * @return メンバーID結果
	 * @throws AppException アプリケーション例外
	 */
	public String findMember(String mailAddress) throws AppException {
		
		if (this.users == null) {
			this.users = findMember();
		}
		List<Map<String, Object>> users = this.users.getData();
		String id = null;
		for (Map<String, Object> user: users) {
			if (StringUtils.equals((String)user.get("email"), mailAddress)) {
				id = (String)user.get("id");
				break;
			}
		}
		return id;
	}
	
	/**
	 * クライアント認証機能
	 * @return 認証結果
	 * @throws AppException アプリケーション例外
	 */
	public ClientCredential getToken() throws AppException {
		
		String resbody = HttpClientUtil.sendRequestForJson(Const.TOKEN_END_POINT, Const.POST, new ClientCredential(), null);
		ClientCredential res = HttpClientUtil.makeResponse(resbody, ClientCredential.class);
		return res;
	}
	
	/**
	 * アクセストークン取得機能
	 * @return アクセストークン
	 * @throws AppException アプリケーション例外
	 */
    public String getAccessToken() throws AppException {
    	
    	if (Const.ACCESS_TOKEN != null) return Const.ACCESS_TOKEN;
    
    	ClientCredential credential = getToken();
    	Const.ACCESS_TOKEN = credential.getAccess_token();
    	return Const.ACCESS_TOKEN;
    }
}
