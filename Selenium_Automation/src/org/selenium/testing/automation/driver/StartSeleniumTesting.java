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
 * <p>Title       				: StartSeleniumTesting</p>
 * <p>Description 				: This is the trigger point class which is called from testng.xml file</p>
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

package org.selenium.testing.automation.driver;

import java.awt.Robot;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.selenium.testing.automation.utility.Constants;
import org.selenium.testing.automation.utility.PropertiesReader;
import org.selenium.testing.automation.utility.Utility;
import org.testng.annotations.Test;

@Test
public class StartSeleniumTesting
{

	private static Logger log = Logger.getLogger(StartSeleniumTesting.class);
	private static String configpath	= System.getProperty("user.dir") + "\\Config\\config.properties";
	public static WebDriver driver;

	private final static HashMap<String, Object> mapIni	= new HashMap<String, Object>();

	@Test
	public static void run() throws Exception
	{
		log.debug("In run");

		String testingFilePath = PropertiesReader.getProp(Constants.TESTINGFILEPATH, configpath, System.getProperty("user.dir"));
		Robot robot = new Robot();

		try
		{
			//open the IE browser
			LoginMenuNavigator.openBrowser(mapIni);
			
			//Move the cursor to top 
			robot.mouseMove(10,10);
			
			//Navigate to the browser
			LoginMenuNavigator.navigate(mapIni);
			
			//Enter login credentials
			LoginMenuNavigator.login("", "", "", "LOGIN", mapIni);
			
			//Get the IE driver handle
			driver = (WebDriver) mapIni.get(Constants.Driver_KEY);
			
			Utility util = new Utility();
			
			//Start the operation of reading xls and DB to proceed with testing
			if (driver != null){
				util.readBook(driver, testingFilePath, mapIni);
			}else{
				log.fatal("Unable to get the driver");
				System.out.println("Unable to get the driver");
			}
		}
		catch (Exception e)
		{			
			log.fatal("Exception: ",e);
		}
		finally
		{
			LoginMenuNavigator.driver.quit();
		
		}
	}

	
}
