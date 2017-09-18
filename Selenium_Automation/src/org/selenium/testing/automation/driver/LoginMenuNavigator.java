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
 * <p>Title       				: LoginMenuNavigator</p>
 * <p>Description 				: This is the class related to login and opening the browser</p>
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenium.testing.automation.utility.Constants;
import org.selenium.testing.automation.utility.PropertiesReader;

public class LoginMenuNavigator
{

	public static WebDriver	     driver;	
	private static StopWatch	 stopWatch	             = new StopWatch();
	private static WebDriverWait	wait	             = null;
	private static Logger log = Logger.getLogger(LoginMenuNavigator.class);

	
	/**
	 * Method to open IE browser. It also initiate the driver
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> openBrowser(HashMap<String, Object> c) throws Exception
	{
		log.debug("###### Entering openBrowser ######");
		Long openBrowserTime;
		openBrowserTime = 0L;
	
		try
		{
			//IOUtilities.readtextdata(Constants.CHECKPATH);
			if (!stopWatch.isStarted())
			{
				stopWatch.reset();
				stopWatch.start();
			}

			// Initialize the Driver
			System.setProperty(PropertiesReader.getProp(Constants.DRIVER,
			        Constants.CONFIGPATH), PropertiesReader.getProp(
			        Constants.DRIVERPATH, Constants.CONFIGPATH));
			System.setProperty(PropertiesReader.getProp(Constants.LOGLEVELATT,
			        Constants.CONFIGPATH), PropertiesReader.getProp(
			        Constants.LOGLEVEL, Constants.CONFIGPATH));
			System.setProperty(PropertiesReader.getProp(Constants.LOGFILEATT,
			        Constants.CONFIGPATH), PropertiesReader.getProp(
			        Constants.LOGFILE, Constants.CONFIGPATH));
			
			// Set the DesiredCapabilities for IE browser
			Map<String, String> capabilities = new HashMap<String, String>();
			capabilities.put(Constants.pageLoadStrategy_key,
			        Constants.pageLoadStrategy_val);
			driver = new InternetExplorerDriver(new DesiredCapabilities(capabilities));
			if (driver == null)
			{
				throw new Exception("Driver could not be initiated");
			}
			stopWatch.stop();
			openBrowserTime = stopWatch.getTime() / 1000;
			stopWatch.reset();

			// Putting success message in the HashMap for the Excel
			c.put("Error Message", "Open Browser Successful");
			c.put("result", "true");
			c.put("executionTime", openBrowserTime);
			c.put(Constants.Driver_KEY, driver);
			
			if(log.isDebugEnabled())
				log.debug("Driver in open browser: " + driver);
		}
		catch (Exception ex)
		{
			c.put("Error Message", "openBrowser Method Unsuccessfull||" + ex.getMessage());
			c.put("result", "false");
			c.put("executionTime", openBrowserTime);
			throw ex;
		}

		log.debug("###### Exiting openBrowser ######");
		return c;

	}

	/**
	 * Navigate method to navigate
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> navigate(HashMap<String, Object> c) throws Exception
	{
		log.debug("###### Entering Navigate ######");
		Long navigateTime;
		navigateTime = 0L;
	
		try
		{
			//IOUtilities.readtextdata(Constants.CHECKPATH);
			stopWatch.reset();
			stopWatch.start();
			String URL = PropertiesReader.getProp("URL", Constants.CONFIGPATH);
			driver.navigate().to(URL);
			stopWatch.stop();
			navigateTime = stopWatch.getTime() / 1000;
			stopWatch.reset();

			if (driver.findElement(By.id(Constants.UserName_label)).getText()
			        .contains("User Name"))
			{
				c.put("Error Message", "Navigate Successful");
				c.put("result", "true");
				c.put("executionTime", navigateTime);
			}

		}

		catch (Exception e)
		{
			c.put("Error Message", "Navigate Unsuccessful");
			c.put("result", "false");
			c.put("executionTime", navigateTime);
			throw e;
		}
		log.debug("###### Exiting Navigate ######");
		return c;
	}


	/**
	 * login method used to Enter Checker or Maker Credential in PSH
	 */
	/**
	 * @param tcid
	 * @param tsid
	 * @param tdid
	 * @param sUser
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> login(String tcid, String tsid, String tdid, String sUser, HashMap<String, Object> c)
	        throws Exception
	{
		log.debug("###### Entering Login ######");
		Long loginTime;
		loginTime = 0L;
		try
		{
			//IOUtilities.readtextdata(Constants.CHECKPATH);
			stopWatch.start();
			driver.getWindowHandle();
			driver.findElement(By.xpath(PropertiesReader.getProp("USERNAME", Constants.ORPATH)))
			        .clear();
			driver.findElement(By.xpath(PropertiesReader.getProp("USERNAME", Constants.ORPATH)))
			        .sendKeys(PropertiesReader.getProp(sUser, Constants.CONFIGPATH));
			new Select(driver.findElement(By.xpath(PropertiesReader.getProp("APPLICATION",
			        Constants.ORPATH)))).selectByVisibleText(PropertiesReader.getProp("APPLICATION",
			        Constants.CONFIGPATH));

			driver.findElement(By.xpath(PropertiesReader.getProp("LOGINBUTTON", Constants.ORPATH)))
			        .click();
			log.debug("in login title = " + driver.getTitle());
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Set<String> win = driver.getWindowHandles();
			log.debug("size  " + win.size());
			ArrayList<String> arr = new ArrayList<String>();

			for (String wind : win)
			{
				//log.debug("wind: " + wind);

				arr.add(wind);

			}

			driver.switchTo().window(arr.get(1));
			log.debug("In login: " + driver.getTitle() + "  |2.  " + driver.getCurrentUrl());

			stopWatch.stop();
			loginTime = stopWatch.getTime() / 1000;
			stopWatch.reset();
			c.put("Error Message", "Login Successful");
			c.put("result", "true");
			c.put("executionTime", loginTime);

		}
		catch (Exception e)
		{
			c.put("Error Message", "Login Page unavailable|| " + e.getMessage());
			c.put("result", "false");
			c.put("executionTime", loginTime);
			e.printStackTrace();
			//IOUtilities.writetextdata(GENERIC_TESTSOL_XPATH_CONSTANTS.CHECKPATH, "N", false);

		}

		log.debug("###### Exiting Login ######");
		return c;
	}

	
	/**
	 * Method to click on menu
	 * @param linkName
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> clickOnMenu(String linkName,HashMap<String, Object> c) throws Exception
	{
		log.debug("###### Entering clickOnMenu ######");
		WebElement clickable = null;
		Long clickOnMenuTime;
		clickOnMenuTime = 0L;
		try
		{
			//IOUtilities.readtextdata(Constants.CHECKPATH);
			stopWatch.reset();
			stopWatch.start();

			Thread.sleep(1000);
			driver.switchTo().defaultContent();

			driver.switchTo().frame("MenuFrame");
			wait = new WebDriverWait(driver, 20);
			clickable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(linkName)));
			clickable.click();
			stopWatch.stop();
			clickOnMenuTime = stopWatch.getTime() / 1000;
			stopWatch.reset();

			c.put("Error Message", "Click on menu Successful");
			c.put("executionTime", clickOnMenuTime);
			c.put("result", "true");
		}
		catch (Exception ex)
		{
			c.put("Error Message", "clickOnMenu |" + ex.getMessage());
			c.put("executionTime", clickOnMenuTime);
			c.put("result", "false");
			throw ex;
		}
		
		log.debug("###### Exiting clickOnMenu ######");
		return c;
	}
	
}