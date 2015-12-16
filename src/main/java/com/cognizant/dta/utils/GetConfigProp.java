package com.cognizant.dta.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetConfigProp {

	private static Properties prop = new Properties();
	private static boolean errorFlag;
	private static final String PROPERTY_FILE = "/config.properties";
	public static final String EXCEPTION_MESSAGE = "Property file is missing";
	private static final String PROPERTY_FILE_URL = "URL";
	private static final String PROPERTY_FILE_PORT = "PORT";
	private static final Logger log = LoggerFactory.getLogger(GetConfigProp.class);
			
	static {
		InputStream in = null;
		try {
			File jarPath = new File(HttpUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			String propertiesPath = jarPath.getParent();
			in = new FileInputStream(propertiesPath+PROPERTY_FILE); 
		    prop.load(in);
		} catch (IOException e1) {
			errorFlag = true;
			e1.printStackTrace();
			log.error("======= Unable to load the property file");
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("======= Unable to close the input stream.");
			}
		}

	}
	
	
	public static String getURL() {
		
		log.info("====== Inside get URL with url:"+PROPERTY_FILE_URL);
		if (errorFlag) {
			return EXCEPTION_MESSAGE;
		}
		
		log.info("====== Exiting get URL");
		return prop.getProperty(PROPERTY_FILE_URL);
	}

	public static String getPORT(){
		
		log.info("====== Inside get PORT");
		if (errorFlag) {
			return EXCEPTION_MESSAGE;
		}
		
		log.info("====== Exiting get PORT");
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
