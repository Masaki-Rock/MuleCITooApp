package mule.ci.tool.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.util.AppException;

public class AccessManagementAPICallerTest {
	
	private static final Logger log = LoggerFactory.getLogger(AccessManagementAPICallerTest.class);
	
//	@Test
	public void findUser() throws AppException {
		AccessManagementAPICaller caller = new AccessManagementAPICaller();
		caller.findUser();
	}
	
//	@Test
	public void findMember() throws AppException {
		AccessManagementAPICaller caller = new AccessManagementAPICaller();
		caller.findMember();
	}
	
//	@Test
	public void findMemberBymail() throws AppException {
		AccessManagementAPICaller caller = new AccessManagementAPICaller();
		String id = caller.findMember("masaki.kawaguchi@accenture.com");
		log.debug("mail {}", id);
	}
}
