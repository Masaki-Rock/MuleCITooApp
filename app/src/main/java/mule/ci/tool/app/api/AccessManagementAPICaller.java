package mule.ci.tool.app.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import mule.ci.tool.app.api.model.ClientCredential;
import mule.ci.tool.app.api.model.CommonResponse;
import mule.ci.tool.app.util.AppException;
import mule.ci.tool.app.util.Const;
import mule.ci.tool.app.util.HttpClientUtil;

public class AccessManagementAPICaller {

	private CommonResponse users;

	/**
	 * ���[�U�����@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V�����G���[
	 */
	public CommonResponse findUser() throws AppException {
		
		String path = String.format(Const.USER_END_POINT, Const.ORGANIZATION_ID);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);
		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		return res;
	}
	
	/**
	 * �����o�[�����@�\
	 * @return ��������
	 * @throws AppException �A�v���P�[�V�����G���[
	 */
	public CommonResponse findMember() throws AppException {
		
		String path = String.format(Const.MEMBER_END_POINT, Const.ORGANIZATION_ID);
		String resbody = HttpClientUtil.sendRequest(path, Const.GET, null);
		CommonResponse res = HttpClientUtil.makeResponse(resbody, CommonResponse.class);
		return res;
	}
	
	/**
	 * �����o�[�����@�\
	 * @return �����o�[ID����
	 * @throws AppException �A�v���P�[�V�����G���[
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
	 * �N���C�A���g�F�؋@�\
	 * @return �F�،���
	 * @throws AppException �A�v���P�[�V�����G���[
	 */
	public ClientCredential getToken() throws AppException {
		
		String resbody = HttpClientUtil.sendRequestForJson(Const.TOKEN_END_POINT, Const.POST, new ClientCredential(), null);
		ClientCredential res = HttpClientUtil.makeResponse(resbody, ClientCredential.class);
		return res;
	}
	
	/**
	 * �A�N�Z�X�g�[�N���擾�@�\
	 * @return �A�N�Z�X�g�[�N��
	 * @throws AppException �A�v���P�[�V�����G���[
	 */
    public String getAccessToken() throws AppException {
    	
    	if (Const.ACCESS_TOKEN != null) return Const.ACCESS_TOKEN;
    
    	ClientCredential credential = getToken();
    	Const.ACCESS_TOKEN = credential.getAccess_token();
    	return Const.ACCESS_TOKEN;
    }
}
