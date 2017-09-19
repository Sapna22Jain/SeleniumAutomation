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
 * <p>Title       				: ElementNavigator</p>
 * <p>Description 				: This is the class to get the element</p>
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

//import atu.testrecorder.ATUTestRecorder;

public class ElementNavigator {

	private static WebDriver driver;
	//static ATUTestRecorder recorder;
	private static Logger log = Logger.getLogger(ElementNavigator.class);

	/**
	 * The constructor gets the Driver object from the hashmap to find elements
	 * 
	 * @param mapIni
	 */
	public ElementNavigator(HashMap<String, Object> mapIni) {
		ElementNavigator.driver = (WebDriver) mapIni.get(Constants.Driver_KEY);
	}

	public ElementNavigator() {

	}

	/**
	 * @param sEntityName
	 * @return
	 */
	public WebElement getElementByxPath(String sEntityName) {
		if (log.isDebugEnabled())
			log.debug("sEntityName =>" + sEntityName);
		WebElement webObj = null;
		try {
			webObj = driver.findElement(By.xpath(sEntityName));

		} catch (org.openqa.selenium.NoSuchElementException e) {
			return null;
		} catch (org.openqa.selenium.NoSuchWindowException exe) {
			return null;
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		return webObj;
	}

	/**
	 * Method to get the elemnet by its name
	 * 
	 * @param sEntityName
	 * @return
	 */
	public WebElement getElementByName(String sEntityName) {
		if (log.isDebugEnabled())
			log.debug("sEntityName =>" + sEntityName);
		WebElement webObj = null;

		try {
			webObj = driver.findElement(By.name(sEntityName));

		} catch (org.openqa.selenium.NoSuchElementException e) {
			return null;
		} catch (org.openqa.selenium.NoSuchWindowException exe) {
			return null;
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		return webObj;
	}

	/**
	 * Method to find the element by Id, ByName or By Xpath
	 * 
	 * @param findMethod
	 * @param sEntityName
	 * @return
	 * @throws Exception
	 */
	public WebElement findElement(String findMethod, String sEntityName) throws Exception {
		if (log.isDebugEnabled())
			log.debug("sEntityName =>" + sEntityName + " findMethod: " + findMethod);
		WebElement webObj = null;
		try {
			if (findMethod.equalsIgnoreCase(Constants.BYNAME)) {
				webObj = getElementByName(sEntityName);
			} else if (findMethod.equalsIgnoreCase(Constants.ID)) {
				webObj = getElementById(sEntityName);
			} else {
				webObj = getElementByxPath(sEntityName);
			}

			while (webObj == null) {
				Utility.wait(driver, 300);
				webObj = findElement(findMethod, sEntityName);
			}

		} catch (org.openqa.selenium.NoSuchFrameException exe) {
			log.fatal("Exception: ", exe);
			throw exe;
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		return webObj;
	}

	/**
	 * Method to get element by its Id
	 * 
	 * @param sEntityName
	 * @return
	 */
	private WebElement getElementById(String sEntityName) {
		if (log.isDebugEnabled())
			log.debug("sEntityName =>" + sEntityName);
		WebElement webObj = null;

		try {
			driver.switchTo().defaultContent();
			driver.switchTo().frame("SearchFrame");
			webObj = driver.findElement(By.id(sEntityName));

		} catch (org.openqa.selenium.NoSuchElementException e) {
			return null;
		} catch (org.openqa.selenium.NoSuchWindowException exe) {
			return null;
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		return webObj;
	}

	/**
	 * Method to click on element
	 * 
	 * @param element
	 * @param iTime
	 * @throws InterruptedException
	 */
	public void click(WebElement element, int iTime) throws InterruptedException {
		element.click();
		Utility.wait(driver, 400);
	}

	/**
	 * Method to populate the value and click on the element along with tab out
	 * operation
	 * 
	 * @param element
	 * @param sInputValues
	 * @throws InterruptedException
	 */
	public void populateTxt(WebElement element, String sInputValues) throws InterruptedException {

		element.click();
		element.clear();
		element.sendKeys(sInputValues);
		element.sendKeys(Keys.TAB);
	}

	/**
	 * Method to select the input value from dropdown
	 * 
	 * @param element
	 * @param sInputValues
	 * @throws InterruptedException
	 */
	public void selectValue(WebElement element, String sInputValues) throws InterruptedException {
		if (log.isDebugEnabled())
			log.debug("Entering In selectValue sInputValues:::: " + element);
		Select dropdown = new Select(element);
		dropdown.selectByVisibleText(sInputValues);
		if (log.isDebugEnabled())
			log.debug("Leaving Value selected");
	}

	/**
	 * Method to select the element by its img
	 * 
	 * @param sMenuName
	 * @param isSleepCount
	 */
	public void getImages(String sMenuName, int isSleepCount) {
		
		if (log.isDebugEnabled())
			log.debug("Name of the Menu is ::" + sMenuName);
		try {
			List<WebElement> links = driver.findElements(By.tagName("img"));
			if (log.isDebugEnabled())
				log.debug("Size of Image ArrayList is ::" + links.size());
			for (WebElement myElement : links) {
				String link = myElement.getText();
				// log.debug(link);
				if (link.trim().equalsIgnoreCase(sMenuName)) {
					// log.debug("Menu found ");
					myElement.click();
					break;
				}
				Utility.wait(driver, isSleepCount);
			}
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");
	}

	/**
	 * Method to add the string based on provided separator and add the
	 * objects in an arraylist
	 * 
	 * @param sKey
	 * @param sSplitChar
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> formatRecord(String sKey, String sSplitChar) throws Exception {
		if(log.isDebugEnabled())
			log.debug("Entering");
		ArrayList<String> arrKey = null;
		try {
			if (sKey != null && sKey.length() > 0)
				arrKey = new ArrayList<String>(Arrays.asList((sKey.split(sSplitChar))));
		} catch (Exception exe) {
			log.fatal("Exception in formatRecord: ", exe);
			throw exe;
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");
		return arrKey;
	}

	/**
	 * Method to switch to modal window
	 * 
	 * @param driver
	 * @param parent
	 */
	public static void switchToModalDialog(WebDriver driver, String parent) {
		if(log.isDebugEnabled())
			log.debug("Entering");
		// Switch to Modal dialog
		if (driver.getWindowHandles().size() == 2) {
			for (String window : driver.getWindowHandles()) {
				if (!window.equals(parent)) {

					driver.switchTo().window(window);
					if (log.isDebugEnabled()) {
						log.debug(driver.getTitle());
						log.debug("Modal dialog found");
					}
					break;
				}
			}
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");
	}	

	/**
	 * Method to wait for a window
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public static void waitForWindow(WebDriver driver) throws InterruptedException {
		if(log.isDebugEnabled())
			log.debug("Entering");

		int timecount = 1;
		do {
			driver.getWindowHandles();
			Thread.sleep(200);
			timecount++;
			if (timecount > 30) {
				break;
			}
		} while (driver.getWindowHandles().size() != 2);
		if(log.isDebugEnabled())
			log.debug("Leaving");

	}

	/**
	 * Method to switch to modal dialog window
	 * 
	 * @param driver
	 * @param parent
	 * @param iWindowSize
	 */
	public static void switchToModalDialog(WebDriver driver, String parent, int iWindowSize) {
		if(log.isDebugEnabled())
			log.debug("Entering");
		// Switch to Modal dialog
		if (driver.getWindowHandles().size() == iWindowSize) {
			for (String window : driver.getWindowHandles()) {

				log.debug(window);

				if (!window.equals(parent)) {
					driver.switchTo().window(window);
					if (log.isDebugEnabled())
						log.debug(driver.getTitle() + " Modal dialog found");

					break;
				}
			}
		}
		
		if(log.isDebugEnabled())
			log.debug("Leaving");
	}

	/**
	 * Method to switch to modal dialog window
	 * 
	 * @param driver
	 * @param parent
	 * @param iWindowSize
	 */
	public static void switchToModalDialog(WebDriver driver, StringBuffer parent, int iWindowSize) {
		log.debug("Swiching to new window!!" + parent.toString());
		try {

			String currentWindow = driver.getWindowHandle();
			log.debug("currentWindow: " + currentWindow);

			Set<String> availableWindows = driver.getWindowHandles();
			log.debug("availableWindows: " + availableWindows.size());
			if (!availableWindows.isEmpty()) {
				for (String windowId : availableWindows) {
					log.debug("windowId: " + windowId);
					log.debug("windowId getTitle: " + driver.switchTo().window(windowId).getTitle());
					if (driver.switchTo().window(windowId).getTitle().equals(parent.toString())) {
						log.debug(driver.getTitle() + " Finallty ");
						break;
					} else {
						driver.switchTo().window(currentWindow);
						log.debug(driver.getTitle() + "In ELSE ");
					}
				}
			}

		} catch (Exception e) {
			log.fatal("Exception in switchToModalDialog->", e);
			throw e;

		}
		if(log.isDebugEnabled())
			log.debug("Leaving");

	}
	
	/**
	 * Method to find the element by Id, ByName or By Xpath
	 * 
	 * @param findMethod
	 * @param sEntityName
	 * @return
	 * @throws Exception
	 */
	public List<WebElement> findElements(String findMethod, String sEntityName) throws Exception {
		if (log.isDebugEnabled())
			log.debug("sEntityName =>" + sEntityName + " findMethod: " + findMethod);
		List<WebElement> elementList = null;
		try {
			
			//List oCheckBox = driver.findElements(By.name(sEntityName));
			
			if (findMethod.equalsIgnoreCase(Constants.BYNAME)) {
				elementList = driver.findElements(By.name(sEntityName));
			}else if (findMethod.equalsIgnoreCase(Constants.ID)) {
				elementList = driver.findElements(By.id(sEntityName));
			} else {
				elementList = driver.findElements(By.xpath(sEntityName));
			}
			
			while (elementList == null) {
				Utility.wait(driver, 300);
				elementList = findElements(findMethod, sEntityName);
			}

		} catch (org.openqa.selenium.NoSuchFrameException exe) {
			log.fatal("Exception: ", exe);
			throw exe;
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");
		return elementList;
	}


	/**
	 * @param oCheckBox
	 * @param pair
	 */
	public void clickRadioButtonOrCheckBox(List<WebElement> oCheckBox,  String pair) {	
		if(log.isDebugEnabled())
			log.debug("Entering");
		
		for (int i = 0; i < oCheckBox.size(); i++) {

			String sValue = ((WebElement) oCheckBox.get(i)).getAttribute("value");
			
			if(log.isDebugEnabled())
				log.debug("Value -------------" + sValue);

			if (sValue.equalsIgnoreCase(pair.trim())) {
				((WebElement) oCheckBox.get(i)).click();
				break;
			}
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");
	}
	
	/**
	 * Method to click on Tabs
	 * @param driver
	 * @param tcid
	 * @param tsid
	 * @param clickSubTabNavigation
	 * @throws InterruptedException
	 */
	public void clickSubTab(String clickSubTabNavigation) throws Exception {
		if(log.isDebugEnabled())
			log.debug("Entering clickSubTab : " + clickSubTabNavigation + "++++++++++++++++++++++++");

		try {
		
			if (clickSubTabNavigation!=null && clickSubTabNavigation.contains("Key Info")) {				
				WebElement kib = driver.findElement(By.xpath("//div[@id='tabpane3']/table/tbody/tr/td[1]"));
				if(log.isDebugEnabled())
					log.debug(kib.getText());
				JavascriptExecutor jee = (JavascriptExecutor) driver;
				jee.executeScript("arguments[0].click();", kib);
			} else {				
				WebElement tabname = driver.findElement(By.xpath("//td[text()='" + clickSubTabNavigation + "'] "));
				JavascriptExecutor je = (JavascriptExecutor) driver;
				je.executeScript("arguments[0].click();", tabname);
			}			
			
		} catch (Exception e) {
			log.fatal("Exception in clickSubTab");
			throw e;			
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");

	}

	/**
	 * @param xPathNavigation
	 * @throws InterruptedException
	 */
	public void clickOnLink(String xPathNavigation) throws InterruptedException {
		if(log.isDebugEnabled())
			log.debug("Entering");

		try{
			StringTokenizer xpathtokenizer = new StringTokenizer(xPathNavigation, "!");
			while (xpathtokenizer.hasMoreTokens()) {
				String tokenpath = xpathtokenizer.nextToken();
				Thread.sleep(3000);
				WebElement xpathClick = driver.findElement(By.xpath(tokenpath));			
				if(log.isDebugEnabled())
					log.debug("xpathClick: " + xpathClick);
				xpathClick.click();
				log.debug("clicked on ele--------------->>>>>>>>>>>>>>>>>");				
			}			
			
		}catch (InterruptedException e) {
			log.fatal("Exception: ",e);
			throw e;
		
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");


	}

	
	/**
	 * @param sFrameName
	 */
	public void switchToFrame(String sFrameName) {
		if(log.isDebugEnabled())
			log.debug("Entering");

		if (StringUtils.isNotEmpty(sFrameName)) {
			driver.switchTo().defaultContent();
			StringTokenizer frameTokens = new StringTokenizer(sFrameName, "*");
			while (frameTokens.hasMoreTokens()) {
				driver.switchTo().frame(frameTokens.nextToken());
			}
		}
		if(log.isDebugEnabled())
			log.debug("Leaving");

	}

	/**
	 * @param xPathNavigation
	 */
	public void clickOnButton(String xPathNavigation) {
		if(log.isDebugEnabled())
			log.debug("Entering");

		//Find the button and click on it
		WebElement tabname = driver.findElement(By.xpath(xPathNavigation));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", tabname);
		
		
		if(log.isDebugEnabled())
			log.debug("Leaving after cluck");

		
		
	}
		
	
}
