/**
 *  Copyright ©  2017   Intellect Design Arena Ltd. All rights reserved.
 *
 *  These materials are confidential and proprietary to Intellect Design Arena Ltd.
 *  and no part of these materials should be reproduced, published, transmitted or
 *  distributed  in any form or by any means, electronic, mechanical, photocopying,
 *  recording or otherwise, or stored in any information storage or retrieval system
 *  of any nature nor should the materials be disclosed to third parties or used in
 *  any other manner for which this is not authorized, without the prior express
 *  written authorization of Intellect Design Arena Ltd.
 *
 * <p>Title       				: InputOutputDtoObject</p>
 * <p>Description 				: This is the DTO object class for the configuration</p>
 * <p>SCF NO      				: 1.0</p>
 * <p>Copyright   				: Copyright © 2017 Intellect Design Arena Ltd. All rights reserved.</p>
 * <p>Company     				: Intellect Design Arena Ltd</p>
 * <p>Date of Creation 			: 15-Sep-2017</p>
 * 
 * @author XXX
 * @version 1.0
 * 
 * <p>--------------------------------------------------------------------------------------</p>
 * <p>MODIFICATION HISTORY:</p>
 * <p>--------------------------------------------------------------------------------------</p>
 * <p>SERIAL	AUTHOR				DATE					SCF				DESCRIPTION		</p>
 * <p>--------------------------------------------------------------------------------------</p>
 *   1        	RAVI/SAPNA      	15-SEP-2017				                 Initial Version
 *
 **/

package org.selenium.testing.automation.utility;
import java.util.ArrayList;

// This class contains all the getters and setters for excel sheet 1 which contains primary details
public class InputOutputDtoObject  {

	private ArrayList<Object> arrInputData;
	private String sTestCaseId;
	private String sMainEntityName;
	private String sChildEntityName;
	private String sMenuEntityName;
	private String sUserName;
	private String sOperation;
	private boolean bOperationStatus;
	private String sOperationMessage;
	private String primaryKey;
	private String tabToSwitch;
	private ArrayList<String> sNavigationEntityName;
	

	public String toString(){
		StringBuffer buff = new StringBuffer();		
		buff.append("sTestCaseId =").append(sTestCaseId).append("|")
		.append("sUserName =").append(sUserName).append("|")
		.append("sMenuEntityName =").append(sMenuEntityName).append("|")
		.append("sMainEntityName =").append(sMainEntityName).append("|")
		.append("sChildEntityName =").append(sChildEntityName).append("|")
		.append("arrInputData =").append(arrInputData);
		return buff.toString();
	}
	
	public void setsTestCaseId(String sTestCaseId) {
		this.sTestCaseId = sTestCaseId;
	}

	public InputOutputDtoObject(){
		arrInputData = new ArrayList<Object>();
		
	}
	
	public void addObject(Object object){
		arrInputData.add(object);
	}
	
	public ArrayList<Object> getDataDao(){
		return arrInputData;
	}

	public String getMainEntityName() {
		return sMainEntityName;
	}

	public void setMainEntityName(String mainEntityName) {
		sMainEntityName = mainEntityName;
	}
	
	public void setNavigationEntityName(ArrayList<String> navigationEntityName) {
		sNavigationEntityName = navigationEntityName;
		
	}
	
	public ArrayList<String> getNavigationEntityname(){
		return sNavigationEntityName;
	}

	public String getChildEntityName() {
		return sChildEntityName;
	}

	public void setChildEntityName(String childEntityName) {
		sChildEntityName = childEntityName;
	}

	public String getMenuEntityName() {
		return sMenuEntityName;
	}

	public void setMenuEntityName(String menuEntityName) {
		sMenuEntityName = menuEntityName;
	}
	
	
	public String getTestCaseId() {
		return sTestCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		sTestCaseId = testCaseId;
	}

	public String getUserName() {
		return sUserName;
	}

	public void setUserName(String userName) {
		sUserName = userName.toUpperCase();
	}

	public String getOperation() {
		return sOperation;
	}

	public void setOperation(String operation) {
		sOperation = operation;
	}

	public boolean isOperationStatus() {
		return bOperationStatus;
	}

	public void setOperationStatus(boolean operationStatus) {
		bOperationStatus = operationStatus;
	}

	public String getOperationMessage() {
		return sOperationMessage;
	}

	public void setOperationMessage(String operationReqId) {
		sOperationMessage = operationReqId;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getTabToSwitch() {
		return tabToSwitch;
	}

	public void setTabToSwitch(String tabToSwitch) {
		this.tabToSwitch = tabToSwitch;
	}

	

	
	
}
