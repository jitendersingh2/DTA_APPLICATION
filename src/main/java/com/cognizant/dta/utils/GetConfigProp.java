package com.cognizant.dta.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetConfigProp {

	private static Properties prop = new Properties();
	private static boolean errorFlag;
	private static final String PROPERTY_FILE = "/config.properties";
	public static final String EXCEPTION_MESSAGE = "Property file is missing";
	private static final String PROPERTY_FILE_URL = "URL";
	private static final String PROPERTY_FILE_PORT = "PORT";
			
	static {
		InputStream in = null;
		try {

			File jarPath=new File(HttpClient.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			String propertiesPath=jarPath.getParent();
			in = new FileInputStream(propertiesPath+PROPERTY_FILE); 
		    prop.load(in);
		} catch (IOException e1) {
			errorFlag = true;
			e1.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	public static String getURL() {

		if (errorFlag) {
			return EXCEPTION_MESSAGE;
		}
		return prop.getProperty(PROPERTY_FILE_URL);
	}

	public static String getPORT(){
		
		if (errorFlag) {
			return EXCEPTION_MESSAGE;
		}
		return prop.getProperty(PROPERTY_FILE_PORT);
	}
	
	public static String getURL(String defaultURL){

		String temp = prop.getProperty(PROPERTY_FILE_URL);
		if (temp==null) {
			temp = defaultURL;
		}
		return temp;
	}

	public static String getPORT(String defaultPORT){

		String temp = prop.getProperty(PROPERTY_FILE_PORT);
		if (temp==null) {
			temp = defaultPORT;
		}
		return temp;
	}
}
