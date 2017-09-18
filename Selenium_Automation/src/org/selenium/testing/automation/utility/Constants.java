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
 * <p>Title       				: Constants</p>
 * <p>Description 				: This is the constants class having constants related to operations</p>
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

public interface Constants
{
	public static final String	CONFIGPATH	               = System.getProperty("user.dir")
	                                                               + "\\Config\\config.properties";
	public static final String	ORPATH	                   = System.getProperty("user.dir")
	                                                               + "\\Object_Repository\\OR.properties";
	public static final String	DRIVER	                   = "DRIVER";
	public static final String	DRIVERPATH	               = "DRIVERPATH";
	public static final String	LOGLEVELATT	               = "LOGLEVELATT";
	public static final String	LOGLEVEL	               = "LOGLEVEL";
	public static final String	LOGFILEATT	               = "LOGFILEATT";
	public static final String	LOGFILE	                   = "LOGFILE";
	public static final String	pageLoadStrategy_key	   = "pageLoadStrategy";
	public static final String	pageLoadStrategy_val	   = "eager";

	public static final String	clickLinkByXPath_XPath1	   = "//*[contains(text(),'";
	public static final String	clickLinkByXPath_XPath3	   = "')]";

	public static final String	MenuFrame_Val	           = "MenuFrame";
	public static final String	SearchFrame_Val	           = "SearchFrame";
	public static final String	Register_Val	           = "IndexRegister";
	public static final String	ScreenHeaderxpath1	       = "//*[@id='pagehdr']/tbody/tr[";
	public static final String	ScreenHeaderxpath2	       = "]/td[";
	public static final String	ScreenHeaderxpath3	       = "]";
	public static final String	ScreenResultxpath4	       = "//*[@id='result_tbl']/tbody/tr[";
	public static final String	ScreenResultxpath5	       = "]/td[";
	public static final String	ScreenResultxpath6	       = "]";

	public static final String	UserName_label	           = "ArmorTicket";

	
	public static final String	navigationSeperator	       = ">";
	public static final String	Driver_KEY	               = "Driver";
	public static final String	DB_IP_ADDRESS	           = "DBIPADDRESS";
	public static final String	DB_USER_NAME	           = "DBUSERNAME";
	public static final String	DB_PASSWORD	               = "DBPASSWORD";
	public static final String	RADIO_BUTTON	           = "RadioButton";
	public static final String	CHECK_BOX	               = "CheckBox";
	public static final String	DROP_DOWN	               = "Dropdown";
	public static final String	DIV	                       = "div";
	public static final String	HYPER_LINK	               = "HyperLink";
	public static final String	OPEN_WINDOW	               = "OpenWindow";
	public static final String	CLICK_SUB_TAB	           = "clickSubTab";
	public static final String	ACTION_BUTTON	           = "button";
	public static final String	CUSTOMIZED_OPERATION	   = "CustomizedOperation";
	public static final String TEXTBOX = "TextBox";
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	public static final String MADNATORY_CONFIG_CELL_COUNT = "MADNATORY_CONFIG_CELL_COUNT";
	public static final String SWITCH_TO_MAIN_MENU = "SwitchToMainMenu";
	public static final String BYNAME = "ByName";
	public static final String ID = "Id";
	public static final String HASHSPLITSEPARATOR = "#";
	public static final String TESTINGFILEPATH = "TESTINGFILEPATH";

}
