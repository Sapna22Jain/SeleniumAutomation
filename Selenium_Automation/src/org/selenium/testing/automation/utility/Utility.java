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
 * <p>Title       				: Utility</p>
 * <p>Description 				: This is the utility class to get the element</p>
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.selenium.testing.automation.driver.LoginMenuNavigator;
import org.selenium.testing.automation.externalhook.Operations;

/**
 * @author sapna.jain
 *
 */
public class Utility {

	public static WebDriver driver;
	public static WebDriver driver1 = null;

	private static String configpath = System.getProperty("user.dir") + "\\Config\\config.properties";
	private static Logger log = Logger.getLogger(Utility.class);

	/**
	 * Method to start the mapping of xls test case with database elements to
	 * proceed with execution
	 * 
	 * @param driver
	 * @param excelFilePath
	 * @param mapIni
	 * @throws IOException
	 */
	public void readBook(WebDriver driver, String excelFilePath, HashMap<String, Object> mapIni) throws Exception {
		if (log.isDebugEnabled())
			log.debug("In readbook, excel path: " + excelFilePath);
		FileInputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
			workbook = new HSSFWorkbook(inputStream);
			readOperationSheet(driver, workbook, mapIni);
		} finally {
			if (workbook != null)
				workbook.close();
			if (inputStream != null)
				inputStream.close();
		}

	}

	/**
	 * Method to populate the configuration data from database for the given tab
	 * name and operation
	 * 
	 * @param sEntityName
	 * @param sOperation
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, ArrayList> populateMaintainData(String sEntityName, String sOperation) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("sOpearion" + sOperation);
			log.debug("sEntityName" + sEntityName);
		}

		/*
		 * if (sOperation.equalsIgnoreCase("Update")) { sOperation = "Add"; }
		 * else if (sOperation.equalsIgnoreCase("Select")) { return null; }
		 */

		StringBuilder sQuery = new StringBuilder()
				.append(" select listagg(keyName, ', ') within group (order by seq_num) key_name, ");
		sQuery.append(
				" listagg(typeofattribute, ', ') within GROUP (ORDER BY seq_num) type,MIN(TabName ||'~' ||operation_mode) tab_name, ");
		sQuery.append(" listagg(action_button, ', ') within group (order by seq_num) action_button, ");
		sQuery.append(
				" BEFORE_AC_BU_FRAME,AFTER_AC_BU_FRAME,listagg(elementtype, ', ') within group (order by seq_num) Elementtype from service_fix_details having");
		sQuery.append(" TABNAME = ? and operation_mode = ? ")
				.append(" GROUP BY TabName,operation_mode,BEFORE_AC_BU_FRAME,AFTER_AC_BU_FRAME ");

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		HashMap<String, ArrayList> hashMap = new HashMap<String, ArrayList>();
		try {
			conn = getConnection();
			if (log.isDebugEnabled())
				log.debug(sQuery.toString());
			pst = conn.prepareStatement(sQuery.toString());
			pst.setString(1, sEntityName);
			pst.setString(2, sOperation);

			res = pst.executeQuery();

			ArrayList<String> arNew = new ArrayList<String>();
			while (res.next()) {
				arNew.add(res.getString("key_name"));
				arNew.add(res.getString("type"));
				arNew.add(res.getString("action_button"));
				arNew.add(res.getString("BEFORE_AC_BU_FRAME"));
				arNew.add(res.getString("AFTER_AC_BU_FRAME"));
				arNew.add(res.getString("Elementtype"));
				hashMap.put(res.getString("tab_name"), arNew);
			}

		} catch (Exception exe) {
			log.fatal("populateMaintainData Exception: ", exe);
			throw exe;
		} finally {
			try {
				if (res != null)
					res.close();
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.fatal("populateMaintainData SQLException: ", e);
				throw new Exception(e);
			}
		}
		if (log.isDebugEnabled())
			log.debug("Leaving Key Size ::" + hashMap.size());
		return hashMap;
	}

	/**
	 * Method to take connection as per DB parameters provided in config file
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		Connection conn = null;
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(PropertiesReader.getProp(Constants.DB_IP_ADDRESS, Constants.CONFIGPATH),
					PropertiesReader.getProp(Constants.DB_USER_NAME, Constants.CONFIGPATH),
					PropertiesReader.getProp(Constants.DB_PASSWORD, Constants.CONFIGPATH));

		} catch (Exception exe) {
			log.fatal("Exception: ", exe);
			throw exe;

		}
		return conn;
	}

	/**
	 * Method which reads the test case sheet and adds in DTO object
	 * 
	 * @param driver
	 * @param workbook
	 * @param mapIni
	 * @throws Exception
	 */
	public void readOperationSheet(WebDriver driver, Workbook workbook, HashMap<String, Object> mapIni) throws Exception {
		if (log.isDebugEnabled())
			log.debug("In readOperationSheet");

		try {
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			while (iterator.hasNext()) {
				ArrayList<InputOutputDtoObject> arrEntityDao = new ArrayList<InputOutputDtoObject>();
				InputOutputDtoObject dtoObject = new InputOutputDtoObject();
				Row nextRow = iterator.next();
				log.debug("First Sheet row Num=" + nextRow.getRowNum());

				if (nextRow.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				int iCount = 0;
				// while loop on first test sheet reading the first navigation
				// sheet and putting the values in dto
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();
					if (log.isDebugEnabled())
						log.debug("Cell count" + iCount + "::Cell data" + cell.getStringCellValue());
					switch (iCount) {
					case 0:
						dtoObject.setTestCaseId(cell.getStringCellValue());
						break;
					case 1:
						dtoObject.setMenuEntityName(cell.getStringCellValue());
						break;
					case 2:
						if (!"".equalsIgnoreCase(cell.getStringCellValue().trim())) {
						//	ArrayList<String> entityNameData = new ArrayList<String>();
							//entityNameData = formatRecord(cell.getStringCellValue(), Constants.navigationSeperator);
						//	dtoObject.setNavigationEntityName(entityNameData);
						//	dtoObject.setMainEntityName(getEntityname(entityNameData));
							dtoObject.setMainEntityName(cell.getStringCellValue());
							if (log.isDebugEnabled())
								log.debug("setMainEntityName=" + dtoObject.getMainEntityName());
							readDataSheet(workbook, dtoObject);
						}

						break;
					case 3:
						dtoObject.setChildEntityName(cell.getStringCellValue());
						break;
					case 4:
						dtoObject.setUserName(cell.getStringCellValue());
						break;
					case 5:
						dtoObject.setOperation(cell.getStringCellValue());
						break;
					default:
						break;
					}

					iCount++;
				}

				arrEntityDao.add(dtoObject);

				executeOperation(driver, arrEntityDao, mapIni);

			}

		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}

	}

	/**
	 * Method which reads the test data from the sheet name provided in first
	 * sheet
	 * 
	 * @param workbook
	 * @param dtoObject
	 * @throws Exception
	 */
	public void readDataSheet(Workbook workbook, InputOutputDtoObject dtoObject) throws Exception {
		if (log.isDebugEnabled())
			log.debug("In readDataSheet");
		try {
			// Get the sheet name
			Sheet testCaseSheet = workbook.getSheet(dtoObject.getMenuEntityName());
			if (log.isDebugEnabled())
				log.debug("readData firstSheet=" + testCaseSheet.getSheetName());
			Iterator<Row> iterator = testCaseSheet.iterator();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				log.debug("readDataSheet rowNum=" + nextRow.getRowNum());
				if (nextRow.getRowNum() == 0) {
					log.debug("readDataSheet Header Data");
					continue;
				}

				if (!isTestIdRow(nextRow, dtoObject.getTestCaseId())) {
					log.debug("false isTestIdRow=" + dtoObject.getTestCaseId());
					continue;
				}

				Iterator<Cell> cellIterator = nextRow.cellIterator();

				cellIterator.hasNext();
				StringBuffer buffData = new StringBuffer();

				for (int cn = 0; cn < nextRow.getLastCellNum(); cn++) {

					Cell cell = nextRow.getCell(cn, Row.CREATE_NULL_AS_BLANK);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						buffData.append(cell.getStringCellValue()).append("#");
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						buffData.append(cell.getBooleanCellValue()).append("#");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						buffData.append(cell.getNumericCellValue()).append("#");
						break;
					default:
						buffData.append("#");
						break;
					}

				}

				if (buffData.length() > 0) {
					if(log.isDebugEnabled())
						log.debug("buffData == > " + buffData);
					dtoObject.addObject(buffData.substring(0, (buffData.length() - 1)));
				}
			}
		} catch (Exception e) {
			log.fatal("Exception: ", e);
			throw e;
		}
	}

	/**
	 * Validates for the given test id in both the sheets
	 * @param nextRow
	 * @param sTestCaseId
	 * @return
	 */
	private boolean isTestIdRow(Row nextRow, String sTestCaseId) throws Exception {
		if(log.isDebugEnabled())
			log.debug("Inside isTestIdRow : nextRow=" + nextRow + ":sTestCaseId=" + sTestCaseId);
		Iterator<Cell> cellIterator = nextRow.cellIterator();
		boolean isTestIdFound = false;
		if (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			//log.debug("cell.getStringCellValue()" + cell.getStringCellValue());
			if (sTestCaseId.equalsIgnoreCase(cell.getStringCellValue())) {
				isTestIdFound = true;
			} else {
				isTestIdFound = false;
			}
		}
		if(log.isDebugEnabled())
			log.debug("Inside isTestIdRow isTestIdFound=" + isTestIdFound);
		return isTestIdFound;
	}


	/**
	 * Method to switch to a frame. This is not in use
	 * @param driver
	 * @param dtoObject
	 * @throws InterruptedException
	 */
	public static void getMainFramePage(WebDriver driver, InputOutputDtoObject dtoObject) throws InterruptedException {

		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		Utility.switchToFrame(driver, "TopLevel");
		Utility.getLinks(driver, dtoObject.getMenuEntityName(), 3000);

		driver.switchTo().defaultContent();

		// Thread.sleep(20000);
		Thread.sleep(20000);
		Utility.switchToFrame(driver, "MenuFrame");
		Utility.getLinks(driver, dtoObject.getMainEntityName(), 3000);
		Thread.sleep(20000);

		Utility.getLinks(driver, dtoObject.getChildEntityName(), 3000);

		driver.switchTo().defaultContent();
		Utility.switchToFrame(driver, "SearchFrame");//
		Thread.sleep(3000);

	}

	/**
	 * Method to switch to a frame
	 * @param driver
	 * @param sFrameName
	 */
	public static void switchToFrame(WebDriver driver, String sFrameName) {
		try {
			driver.switchTo().frame(sFrameName);
		} catch (org.openqa.selenium.NoSuchFrameException exe) {
			exe.printStackTrace();
		}

	}

	/**
	 * Method to get links
	 * @param driver
	 * @param sMenuName
	 * @param iSleepCount
	 */
	public static void getLinks(WebDriver driver, String sMenuName, int iSleepCount) {
		try {
			log.debug(sMenuName);
			List<WebElement> links = driver.findElements(By.tagName("a"));			
			for (WebElement myElement : links) {
				String link = myElement.getText();				
				if (link.trim().equalsIgnoreCase(sMenuName)) {
					log.debug("Menu found ");
					myElement.click();
					break;
				}

			}
			Thread.sleep(iSleepCount);
		} catch (Exception e) {
			log.fatal("Exception: ", e);
		}
	}

	/**
	 * Method to wait 
	 * @param driver
	 * @param iSec
	 */
	static public void wait(WebDriver driver, int iSec) {
		driver.manage().timeouts().implicitlyWait(iSec, TimeUnit.SECONDS);
	}

	/**
	 * This method takes the value from xls and from databasse and add the
	 * config and test data in an hashmap
	 * 
	 * @param dbConfigKeyList
	 * @param xlsArrDataList
	 * @param elementTypeList
	 * @param elementFindTypeList
	 * @param actionTypeList
	 * @return
	 * @throws Exception
	 */
	static public LinkedHashMap<String, String> formatHashData(ArrayList<String> dbConfigKeyList, ArrayList<String> xlsArrDataList,
			ArrayList<String> elementTypeList, ArrayList<String> elementFindTypeList, ArrayList<String> actionTypeList) throws Exception {		
		
		if (log.isDebugEnabled()) {
			log.debug("Key =>" + dbConfigKeyList);
			log.debug("arrdata =>" + xlsArrDataList);
		}
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		try {
			int xlsTotalValueCount = Integer
					.parseInt(PropertiesReader.getProp(Constants.MADNATORY_CONFIG_CELL_COUNT, Constants.CONFIGPATH));//
			// int xlsTotalValueCount = 5;
			int xlsMandatoryConfigDataCount = xlsTotalValueCount;

			for (int i = 0; i < dbConfigKeyList.size(); i++) {
				if ((xlsArrDataList.size() - xlsMandatoryConfigDataCount) > i) {
					log.debug("Putting data for : " + dbConfigKeyList.get(i) + " Count: " + xlsTotalValueCount + " Value: "
							+ xlsArrDataList.get(xlsTotalValueCount));

					data.put(dbConfigKeyList.get(i) + "~" + elementTypeList.get(i) + "~" + elementFindTypeList.get(i) + "~" + actionTypeList.get(i),
							xlsArrDataList.get(xlsTotalValueCount));
				} else {
					log.debug("In else part");
					data.put(dbConfigKeyList.get(i) + "~" + elementTypeList.get(i) + "~" + elementFindTypeList.get(i) + "~" + actionTypeList.get(i), null);
				}
				
				xlsTotalValueCount++;
			}
		} catch (Exception exe) {
			log.fatal("Excpetion: ", exe);
			throw exe;
		}
		return data;
	}

	/**
	 * Method to take screenshot
	 * @param driver
	 * @param FilePath
	 * @param dtoObject
	 * @return
	 * @throws Exception 
	 */
	public static String takeScreenshot(WebDriver driver, InputOutputDtoObject dtoObject, ArrayList<String> arrdata)
			throws Exception {
		String filePath = PropertiesReader.getProp("SCREENSHOTSPATH", configpath, System.getProperty("user.dir"));
		String testCaseId = dtoObject.getTestCaseId();
		String soperation = arrdata.get(2);
		String mainEntityName = dtoObject.getMainEntityName();
		if(log.isDebugEnabled())
			log.debug("In takeScreenShot TestcaseId: " + testCaseId + " operation: " + soperation + " mainEntityName: "
				+ mainEntityName);
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		filePath = filePath + testCaseId + "_" + mainEntityName + "_" + soperation + "_" + dateFormat.format(date)
				+ ".jpg";
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(filePath));
		filePath = filePath.replace("//", "/");
		if(log.isDebugEnabled())
			log.debug("Screenshot is captured and stored in path:" + filePath);
		return filePath;

	}

	


	/**
	 * Method which start executing of testing automation iterating over test
	 * cases and test data sequences against each test case
	 * 
	 * @param driver
	 * @param arrEntityDao
	 * @param mapIni
	 * @throws Exception 
	 */
	public void executeOperation(WebDriver driver, ArrayList<InputOutputDtoObject> arrEntityDao, HashMap<String, Object> mapIni) throws Exception {
		if (log.isDebugEnabled())
			log.debug(
					"In executeOperation Title " + driver.getTitle() + " arrEntityDao.size(): " + arrEntityDao.size());
		try {
			ElementNavigator exe = new ElementNavigator(mapIni);
			String linkPrefix = "//*[text()='";
			String linkSuffix = "']";
			// iterating on test case wise sheet which is the first sheet
			for (InputOutputDtoObject dtoObject : arrEntityDao) {
				driver.navigate().refresh();

				if (log.isDebugEnabled())
					log.debug("executeOperation daoObject=" + dtoObject.toString());

				// Getting the menu link name from the first sheet after separating comma
				StringTokenizer menutokenizer = new StringTokenizer(dtoObject.getMainEntityName(), ",");

				while (menutokenizer.hasMoreTokens()) {
					String Link = linkPrefix + menutokenizer.nextToken() + linkSuffix;
					// Click on menu
					LoginMenuNavigator.clickOnMenu(Link,  mapIni);
				}

				// log.debug("Now starting with the operations..");
				// Switch to search frame
				Utility.wait(driver, 5000);
				driver.switchTo().defaultContent();
				Utility.wait(driver, 300);
				driver.switchTo().defaultContent();
				driver.switchTo().frame("SearchFrame");

				// iterating next sheet which is specified in first sheet
				for (Object strData : dtoObject.getDataDao()) {

					if (log.isDebugEnabled())
						log.debug("strData: " + strData);
					ArrayList<String> arrdata = exe.formatRecord(strData.toString(), Constants.HASHSPLITSEPARATOR);

					String sActionButton = null;
					String sBeforeFrame = null;
					String sAfterFrame = null;
					String entityType = null;

					if (arrdata.isEmpty()) {
						continue;
					}
					try {
						String sOperation = arrdata.get(2);
						String screenshotFlag = arrdata.get(3);

						HashMap<String, ArrayList> keyRecord = new HashMap();
						if (log.isDebugEnabled()){
							log.debug("before populateMaintainData call " + arrdata.get(1).toString() + "and operation"	+ sOperation);
						}
						keyRecord = populateMaintainData(arrdata.get(1).toString(), sOperation);
						if (log.isDebugEnabled()) {
							log.debug(keyRecord + " keyRecord.size()" + keyRecord.size());
							log.debug(dtoObject.getMainEntityName() + "~" + sOperation);
						}

						if (keyRecord != null && keyRecord.size() > 0) {
							ArrayList arKeyRecord = keyRecord.get(arrdata.get(1).toString() + "~" + sOperation);
							if (log.isDebugEnabled())
								log.debug("Printig arKeyRecord ::" + arKeyRecord);

							String sKeyType = (String) arKeyRecord.get(0);
							String sAttributeType = (String) arKeyRecord.get(1);
							sActionButton = (String) arKeyRecord.get(2);
							sBeforeFrame = (String) arKeyRecord.get(3);
							sAfterFrame = (String) arKeyRecord.get(4);
							entityType = (String) arKeyRecord.get(5);

							ArrayList<String> entype = exe.formatRecord(entityType, ",");
							ArrayList<String> Key = exe.formatRecord(sKeyType, ",");
							ArrayList<String> sactype = exe.formatRecord(sActionButton, ",");

							ArrayList<String> type = exe.formatRecord(sAttributeType, ",");
							LinkedHashMap<String, String> hsmMappingData = Utility.formatHashData(Key, arrdata, type,
									entype, sactype);

							if (log.isDebugEnabled())
								log.debug("hsmMappingData is ::" + hsmMappingData);
							
							Iterator it = hsmMappingData.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry pair = (Map.Entry) it.next();
								if (pair.getValue() != null && pair.getValue().toString().trim().length() > 0) {

									if (log.isDebugEnabled()){
										log.debug("Now inside HM loop ---------------" + pair.getKey().toString()
												+ " = " + pair.getValue().toString());
									}
									ArrayList<String> separator = exe.formatRecord(pair.getKey().toString(), "~");																		
									String elementId = "";
									String elementType="";
									String findMethod = "";
									String xPathNavigation = "";
									String xlsValue = pair.getValue().toString().trim();
									
									if(separator.get(0)!=null){
										elementId = separator.get(0).trim();
									}
									if(separator.get(1)!=null){
										elementType = separator.get(1).trim();
									}
									if(separator.get(2)!=null){
										findMethod = separator.get(2).trim();
									}
									if(separator.get(3)!=null){
										xPathNavigation = separator.get(3).trim();
									}									
									
									if(log.isDebugEnabled()){
										log.debug("elementId: " + elementId + " elementType: "+ elementType + " findMethod: " + findMethod+ " xlsValue: "+xlsValue);
									}

									if (elementType!=null && elementType.contains(Constants.RADIO_BUTTON) ||elementType.contains(Constants.CHECK_BOX) ) {
										List<WebElement> elementList = exe.findElements(findMethod, elementId);
										exe.clickRadioButtonOrCheckBox(elementList,xlsValue);
										Thread.sleep(2000);
									}  else if (elementType!=null && elementType.contains(Constants.DROP_DOWN)) {										
										exe.selectValue(exe.findElement(findMethod, elementId),xlsValue);
									} else if (elementType!=null && elementType.contains(Constants.DIV) || elementType.contains(Constants.HYPER_LINK)) {										
										exe.clickOnLink(xPathNavigation);										
										//Take screenshot
										if (elementType.contains(Constants.HYPER_LINK) && "Y".equals(screenshotFlag)) {											
											takeScreenshot(driver, dtoObject, arrdata);
										}										
									}  else if (elementType!=null && elementType.contains(Constants.OPEN_WINDOW)) {
										//Sleep added before getting focus on window so as to have window available for focus
										Thread.sleep(5000);
										focusOnWindow(driver, xPathNavigation);										
										if ("Y".equals(screenshotFlag)) {
											log.debug("inside openwindow screenshot capture");
											takeScreenshot(driver, dtoObject, arrdata);
										}
										exe.switchToFrame(sAfterFrame);
									} else if (elementType!=null && elementType.contains(Constants.CLICK_SUB_TAB)) {
										String clickSubTabNavigation = xPathNavigation;										
										exe.clickSubTab(clickSubTabNavigation);
										Thread.sleep(2000);
									} else if (elementType!=null && elementType.contains(Constants.ACTION_BUTTON)) {
										exe.switchToFrame(sBeforeFrame);	
										exe.clickOnButton(xPathNavigation);										
										exe.switchToFrame(sAfterFrame);
										Thread.sleep(3000);
										//take screen shot post the click of button
										if ("Y".equals(screenshotFlag)) {
											takeScreenshot(driver, dtoObject, arrdata);
										}
									} else if (elementType!=null && elementType.contains(Constants.CUSTOMIZED_OPERATION)) {
										Thread.sleep(5000);
										log.debug("inside customized operation");
										Operations.calling(sOperation, driver, dtoObject, arrdata);
										Thread.sleep(2000);
									} else if (elementType!=null && elementType.contains(Constants.SWITCH_TO_MAIN_MENU)) {
										log.debug("inside the SWITCH_TO_MAIN_MENU");
										Thread.sleep(2000);
										Operations.calling(sOperation, driver, dtoObject, arrdata);
										Thread.sleep(2000);
									} else if (elementType!=null && elementType.contains(Constants.TEXTBOX)) {							
										Utility.wait(driver, 300);
										exe.populateTxt(exe.findElement(separator.get(2).trim(), separator.get(0).trim()),xlsValue);
										Thread.sleep(2000);
									} else {
										Utility.wait(driver, 300);
										exe.populateTxt(exe.findElement(separator.get(2).trim(), separator.get(0).trim()),xlsValue);
										Thread.sleep(2000);
									}
								}
							}
						}
						//Update the second xls with test case result as PASS
						ExcelUtils.updateExcelSheet(dtoObject, Constants.PASS, arrdata);
					} catch (Exception e) {						
						//Update the second xls with test case resul as FAIL and break for that test case id
						ExcelUtils.updateExcelSheet(dtoObject, Constants.FAIL, arrdata);
						log.fatal("Exception: ",e);
						break;
					}
				}
			}

		} catch (Exception e) {
			log.fatal("Exception in executeOperation()", e);
			throw e;
		}
	}


	/**
	 * Method to focus on a window for which title name is passed
	 * @param driver
	 * @param title
	 */
	private static void focusOnWindow(WebDriver driver, String title) throws Exception {

		try {

			if(log.isDebugEnabled())
				log.debug("searching for :-->" + title);
			
			Set<String> window = driver.getWindowHandles();		
			Iterator<String> itr = window.iterator();
			while (itr.hasNext()) {				
				driver.switchTo().window(itr.next());
				if (title.equalsIgnoreCase(driver.getTitle())) {					
					break;
				}
			}

			if(log.isDebugEnabled())
				log.debug("You are on right window" + driver.getTitle());
			
		} catch (Exception ex) {
			log.fatal("Exception in Nfocusonwindow: ",ex);
			throw ex;			
		}
	}

	
}