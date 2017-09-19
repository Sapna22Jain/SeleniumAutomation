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
 * <p>Title       				: PropertiesReader</p>
 * <p>Description 				: This is the utility class related to reading the property file</p>
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

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesReader
{
	private static Logger log = Logger.getLogger(PropertiesReader.class);
	public static String configpath	= System.getProperty("user.dir") + "\\Config\\config.properties";

	

	
	/**
	 * This method gets data from property file.
	 * 
	 * @param key
	 * @param propFilePath
	 * @return property values
	 * @throws IOException
	 */
	public static String getProp(String key, String propFilePath) throws Exception
	{
		if (key.contains("PATH"))
		{
			return getProp(key, propFilePath, System.getProperty("user.dir"));

		}
		String propval=null;
		FileReader reader = null;
		try {
			String propfilename = propFilePath;
			reader=new FileReader(propfilename);
			Properties prop = new Properties();
			prop.load(reader);
			propval = prop.getProperty(key);
		} catch (Exception e) {
			log.fatal("Exception: ",e);
			throw e;
		}finally{
			if(reader!=null)
				reader.close();
		}
		return propval;
	}

	/**
	 * This method is overloaded form of getProp(String key,String propfilepath)
	 * and gets configuration data.
	 * 
	 * @param key
	 * @param propfilepath
	 * @param user_workspace
	 * @return path for given resource as key.
	 * @throws IOException
	 */
	public static String getProp(String key, String propfilepath, String user_workspace) throws Exception
	{
		String propfilename = propfilepath;
		String propval=null;
		FileReader reader = null;
		try {
			reader = new FileReader(propfilename);
			Properties prop = new Properties();
			prop.load(reader);
			propval = prop.getProperty(key);
		} catch (Exception e) {
			log.fatal("Exception: ",e);
			throw e;
		}finally{
			if(reader!=null)
				reader.close();
		}
		return user_workspace + propval;
	}


	
		
	
	
	
}
