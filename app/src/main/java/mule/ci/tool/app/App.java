/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mule.ci.tool.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mule.ci.tool.app.api.APIManagerAPICaller;
import mule.ci.tool.app.api.CloudhubAPICaller;
import mule.ci.tool.app.util.AppException;

public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	/**
	 * API�C���X�^���X�o�^�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static void initAPIInstance() throws AppException {
		
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAPIInstance();
	}
	
	/**
	 * API�C���X�^���X�o�^�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static void deleteAPIInstance() throws AppException {
		
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.deleteAPIInstance();
	}
	
	/**
	 * �|���V�[�E�A���[�g�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static void initPolicy() throws AppException {
		
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.saveAllTier();
		caller.saveAllPolicy();
		caller.saveAllAPIAlert();
	}
	
	/**
	 * API�C���X�^���X�폜�@�\
	 * @throws AppException �A�v���P�[�V������O
	 */
	public static void deletePolicy() throws AppException {
		
		APIManagerAPICaller caller = new APIManagerAPICaller();
		caller.deleteAllAPIAlert();
		caller.deleteAllPolicy();
		caller.deleteAllTier();
	}
	
	/**
	 * �����^�C���A���[�g�o�^�@�\
	 */
	public static void initRuntimeAlert() throws AppException {
		
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.saveAllRuntimeAlert();
	}
	
	/**
	 * �����^�C���A���[�g�o�^�@�\
	 */
	public static void deleteRuntimeAlert() throws AppException {
		
		CloudhubAPICaller caller = new CloudhubAPICaller();
		caller.deleteAllRuntimeAlert();
	}

	public static void main(String[] args) {
		log.info("Main Process Start!!");
		try {
			initPolicy();
		} catch (AppException e) {
			log.error(e.getMessage());
		}
		log.info("Main Process Finished!!");
	}
}