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
 * <p>Title       				: ExcelUtils</p>
 * <p>Description 				: This is the utility class related to xls operations</p>
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils
{

	private static Logger log = Logger.getLogger(ExcelUtils.class);
	private static int	   rowCounts	   = 0, colCounts = 0;
	private static boolean	headerCreated	= false;
	private static String configpath = System.getProperty("user.dir") + "\\Config\\config.properties";
		
	/**
	 * @param sheet
	 * @param header
	 * @param resultdata1
	 * @param sTestStatus
	 * @param sUser
	 * @throws IOException
	 */
	public static void prepareRegistrationIdGenXLS(HSSFSheet sheet, String[] header, String[] resultdata1,
	        String sTestStatus, String sUser) throws IOException
	{

		log.debug("header-->>" + header + " size-->" + header.length + "  headerCreatedd-->"
		        + headerCreated);

		if (!headerCreated)
		{
			Row row = sheet.createRow(rowCounts++);
			for (String a : header)
			{
				log.debug(a);
				Cell cell = row.createCell(colCounts++);
				cell.setCellValue(a);
				log.debug("Set cell is -->" + cell.getStringCellValue() + "rowCounts-1->" + rowCounts
				        + "colCounts-1->" + colCounts);
			}
			headerCreated = true;
		}

		HashMap<String, Short> colorMap = new HashMap<String, Short>();
		colorMap.put("PASS", IndexedColors.GREEN.getIndex());
		colorMap.put("FAIL", IndexedColors.RED.getIndex());
		colorMap.put("SKIPPED", IndexedColors.INDIGO.getIndex());
		colorMap.put("Not Executed", IndexedColors.YELLOW.getIndex());
		colCounts = 0;
		CellStyle style = sheet.getWorkbook().createCellStyle();
		log.debug("Sheet name is :***********	" + sheet.getSheetName());
		Row row1 = sheet.createRow(rowCounts++);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(colorMap.get(sTestStatus));

		for (String cellValue : resultdata1)
		{
			Cell cell1 = row1.createCell(colCounts++);
			log.debug("colCounts-2->" + colCounts);
			if (cellValue.equals(sTestStatus))
			{
				cell1.setCellStyle(style);
				cell1.setCellValue(cellValue);
			}
			else
			{
				cell1.setCellValue(cellValue);
			}

		}

	}
	
	/**
	 * Update the excel sheet with test case result as PASS or FAIL and formatting with color
	 * @param dto
	 * @param resultValue
	 * @param arrdata
	 * @throws Exception 
	 */
	public static void updateExcelSheet(InputOutputDtoObject dto, String resultValue, ArrayList<String> arrdata)
			throws Exception {
		
		if(log.isDebugEnabled()){			
			log.debug("Going to Update Request id for TestcaseId=" + dto.getTestCaseId() + " with request id as:"
				+ resultValue);
		}
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		Workbook workbook = null;
		String testingFilePath = PropertiesReader.getProp(Constants.TESTINGFILEPATH, configpath, System.getProperty("user.dir"));
		try {
			
			inputStream = new FileInputStream(new File(testingFilePath));
			workbook = new HSSFWorkbook(inputStream);
			Sheet sheetName = workbook.getSheet(dto.getMenuEntityName());
			Sheet navigationSheet = workbook.getSheetAt(0);				
			String tabdataname = arrdata.get(1);
			String operation = arrdata.get(2);
			if(log.isDebugEnabled()){
				log.debug("navigationSheet: " + navigationSheet.getSheetName());
				log.debug("tabdataname " + tabdataname + " operation " + operation);		
				log.debug("Sheet Updating=" + sheetName.getSheetName() + "test id=" + dto.getTestCaseId());
			}
			for (Row row : sheetName) {

				//for (Cell cell : row) {
				Cell testCaseCell1 = row.getCell(0);
				Cell testCaseCell2 = row.getCell(1);
				Cell testCaseCell3 = row.getCell(2);
		
				if (testCaseCell1!=null && testCaseCell1.getStringCellValue().trim().equals(dto.getTestCaseId())
						&& tabdataname.equals(testCaseCell2.getStringCellValue().trim())
						&& operation.equals(testCaseCell3.getStringCellValue().trim())) {
					CellStyle style = sheetName.getWorkbook().createCellStyle();
					Cell cell1 = row.createCell(4);
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					if (Constants.PASS.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
					} else if (Constants.FAIL.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.RED.getIndex());
					}
					cell1.setCellStyle(style);
					cell1.setCellValue(resultValue);							
				
				}
			}

			for (Row row : navigationSheet) { 
				//for (Cell cell : row) {					
				Cell testCaseCell1 = row.getCell(0);
				Cell testCaseCell2 = row.getCell(1);
				
				
				if (testCaseCell1!=null && testCaseCell1.getStringCellValue().trim().equals(dto.getTestCaseId())
						&& dto.getMenuEntityName().equals(testCaseCell2.getStringCellValue().trim())) {
					
					CellStyle style = sheetName.getWorkbook().createCellStyle();
					Cell cell1 = row.createCell(5);
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					if (Constants.PASS.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
					} else if (Constants.FAIL.equals(resultValue)) {
						style.setFillForegroundColor(IndexedColors.RED.getIndex());
					}
					cell1.setCellStyle(style);
					cell1.setCellValue(resultValue);		

				}				
	
			}
			
			outputStream = new FileOutputStream(new File(testingFilePath));
			workbook.write(outputStream);
		} catch (Exception e) {
			log.fatal("Exception in updateExelSheet(): ", e);			
		} finally {
			if (workbook != null)
				workbook.close();
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}
		if (log.isDebugEnabled())
			log.debug("Finished Updating request id for TestcaseId=" + dto.getTestCaseId() + " with request id as:"
					+ resultValue);

	}
}