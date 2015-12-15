package com.cognizant.dta.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetConfigProp {
	
	 public static String getURL(){
		 Properties prop = new Properties();
	 
	     try {

	        File jarPath=new File(HttpClient.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParent();//.getAbsolutePath();
	        //(" propertiesPath-"+propertiesPath);
	        prop.load(new FileInputStream(propertiesPath+"/config.properties"));
	     } catch (IOException e1) {
	        e1.printStackTrace();
	     }
	     return prop.getProperty("URL");
	 }
	 
	 public static String getPORT(){
		 Properties prop = new Properties();
	 
	     try {

	        File jarPath=new File(HttpClient.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParent();//.getAbsolutePath();
	        //(" propertiesPath-"+propertiesPath);
	        prop.load(new FileInputStream(propertiesPath+"/config.properties"));
	     } catch (IOException e1) {
	        e1.printStackTrace();
	     }
	     return prop.getProperty("PORT");
	 }

}