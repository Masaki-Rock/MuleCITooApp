/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mule.ci.tool.app;

import org.junit.Test;

import mule.ci.tool.app.util.AppException;

public class AppTest {

	@Test
	public void initAPIInstance() throws AppException {
//		App.initAPIInstance();
//        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
	}
	
	@Test
	public void deleteAPIInstance() throws AppException {
//		App.deleteAPIInstance();
	}
	
	@Test
	public void initPolicy() throws AppException {
		App.initPolicy();
	}
	
	@Test
	public void deletePolicy() throws AppException {
		App.deletePolicy();
	}

	@Test
	public void initRuntimeAlert() throws AppException {
		App.initRuntimeAlert();
	}
	
	@Test
	public void deleteRuntimeAlert() throws AppException {
		App.deleteRuntimeAlert();
	}
}
