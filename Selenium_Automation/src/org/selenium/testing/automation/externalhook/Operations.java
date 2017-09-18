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
 * <p>Title       				: Operations</p>
 * <p>Description 				: This is the hook class where any external rules can be written apart from the provided one for the configuration</p>
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
package org.selenium.testing.automation.externalhook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.selenium.testing.automation.utility.Constants;
import org.selenium.testing.automation.utility.ExcelUtils;
import org.selenium.testing.automation.utility.InputOutputDtoObject;
import org.selenium.testing.automation.utility.PropertiesReader;
import org.selenium.testing.automation.utility.Utility;

public class Operations
{
	private static Logger log = Logger.getLogger(Operations.class);
	
	/**
	 * This is the hookup method provided to add any customization logic if needed. 
	 * E.g. Reading of reference id from a window or switching to a menu where there is no title
	 * @param sOperation
	 * @param driver
	 * @param dtoObject 
	 * @throws Exception
	 */
	public static void calling(String sOperation,  WebDriver driver, InputOutputDtoObject dtoObject,ArrayList<String> arrdata)
	        throws Exception
	{
		try {
			String menuEntityName = dtoObject.getMenuEntityName();
			String testCaseId= dtoObject.getTestCaseId();
			if(Constants.SWITCH_TO_MAIN_MENU.equals(sOperation)){
				switchToMainMenu(sOperation, menuEntityName, testCaseId, driver,dtoObject,arrdata);				
			}else{
				getRefId(sOperation, menuEntityName, testCaseId, driver,dtoObject,arrdata);
			}			
			
		} catch (Exception e) {			
			log.fatal("Exception in calling");
			throw e;			
		}
		
	}

	/**
	 * This is the customized method added to switch back to Trade navigation menu as 
	 * it does not have title
	 * @param sOperation
	 * @param menuEntityName
	 * @param testCaseId
	 * @param driver
	 * @param dtoObject
	 * @param arrdata
	 */
	private static void switchToMainMenu(String sOperation, String menuEntityName, String testCaseId, WebDriver driver,
			InputOutputDtoObject dtoObject, ArrayList<String> arrdata) {
		if(log.isDebugEnabled())
			log.debug("Entering");
		
		Set<String> win = driver.getWindowHandles();
		if(log.isDebugEnabled())
			log.debug(("customizedSwitchWindow size  " + win.size()));
		
		ArrayList<String> arr = new ArrayList<String>();
		for (String wind : win)
		{
			arr.add(wind);
		}

		driver.switchTo().window(arr.get(1));
		if(log.isDebugEnabled())
			log.debug("In SWITCH_TO_MAIN_MENU 1: " + driver.getTitle() + "  |2.  " + driver.getCurrentUrl());		
		
		if(log.isDebugEnabled())
			log.debug("Leaving");
		
	}

	/**
	 * This is the method added to get the reference id generated and write in an xls
	 * @param sOperation
	 * @param menuEntityName
	 * @param testCaseId
	 * @param driver
	 * @param dtoObject 
	 * @throws Exception
	 */
	public static void getRefId(String sOperation, String menuEntityName, String testCaseId, WebDriver driver, InputOutputDtoObject dtoObject,ArrayList<String> arrdata)
	        throws Exception
	{
		try {
			if(log.isDebugEnabled())
				log.debug("++++++++++++++in getRefId+++++++++++++++ ");
			Thread.sleep(5000);
			/*Set<String> wind = driver.getWindowHandles();
			log.debug("size   " + wind.size());
			ArrayList<String> arr = new ArrayList<String>();
			for (String win : wind)
			{
				arr.add(win);
				//log.debug("added>>>>>>" + win);
			}
			driver.switchTo().window(arr.get(1));
			driver.switchTo().defaultContent();
			driver.switchTo().frame("newIndex");*/
			
			String screenshotFlag=arrdata.get(3);
			
			//Added to take screenshot of the reference no generated
			if("Y".equals(screenshotFlag)){
				//log.debug("inside actionbutton screenshot getRefId");
				Utility.takeScreenshot(driver, dtoObject,arrdata);			
			}
			
			String ReffId = driver.findElement(By.xpath("/html/body/b/pre")).getText();
			String RefId = ReffId.trim();
			writexls(RefId, testCaseId);
			if(log.isDebugEnabled())
				log.debug("ref Id-->" + RefId);
			driver.switchTo().defaultContent();
			driver.switchTo().frame("BUTTONFRAME");
			driver.findElement(By.xpath("//span[@class='Close']")).click();

			Set<String> winds = driver.getWindowHandles();
			if(log.isDebugEnabled())
				log.debug("size   " + winds.size());
			for (String win : winds)
			{
				driver.switchTo().window(win);
			}
			// driver.navigate().refresh();
		} catch (Exception e) {			
			log.fatal("Exception in getRefId: ",e);
			throw e;			
		}

	}

	/**
	 * This is the method added to write the reference id generated against the test case id 
	 * in Result.xls specified in config property file
	 * @param refid
	 * @param testCaseId
	 * @throws Exception
	 */
	public static void writexls(String refid, String testCaseId) throws Exception
	{
		HSSFWorkbook wb = null;
		HSSFSheet sheet;
		FileInputStream inFile = null;		
		String configpath = System.getProperty("user.dir") + "\\Config\\config.properties";
		String resultfilepath = PropertiesReader.getProp("RESULTFILEPATH", configpath, System.getProperty("user.dir"));
		FileOutputStream outFile = null;
		String[] header1 = { "TestCaseID", "Status" };
		try{
			inFile = new FileInputStream(resultfilepath);
			wb = new HSSFWorkbook(inFile);	
			sheet = wb.getSheetAt(0);
	
			outFile = new FileOutputStream(new File(resultfilepath));	
			//ExcelUtils.setExcelWSheet1(wb, "Report");
			if(log.isDebugEnabled())
				log.debug("resultfilepath" + resultfilepath);
	
			String[] resultdata1 = { testCaseId, refid };			
			ExcelUtils.prepareRegistrationIdGenXLS(sheet, header1, resultdata1, Constants.PASS, "");

			wb.write(outFile);
		}catch(Exception e){			
			log.fatal("Exception: ",e);
			throw e;
		}finally{
			if(inFile!=null)
				inFile.close();
			if(outFile!=null)
				outFile.close();
			if(wb!=null)
				wb.close();
		}

	}
}
