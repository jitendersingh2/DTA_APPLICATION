package com.cognizant.dta.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	
	  private static final String CONTENT_TYPE = "Content-Type";
	  private static final String CONTENT_TYPE_VALUE = "application/xml";
	  private static final String USER_AGENT = "User-Agent";
	  private static final String USER_AGENT_VALUE = "Mozilla/5.0";
	  private static final String ENCODING = "UTF-8";	  
	  private static final String GET_URL = GetConfigProp.getURL();	  
	  private static final String POST_URL = GetConfigProp.getURL();
	  private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	  
	  public static String sendGET() throws IOException {
		  
		  	log.info("===== Inside send get");
		    
		    HttpGet httpGet = new HttpGet(GET_URL);
	        httpGet.addHeader(USER_AGENT, USER_AGENT_VALUE);
	        
	        CloseableHttpResponse httpResponse = null; 
	        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
	        	httpResponse = httpClient.execute(httpGet);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("===== Unable to hit the url:"+GET_URL);
			}
	        
	        StringBuilder response = new StringBuilder();
	   	 
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();
				log.error("===== Unable to parse the response from url:"+GET_URL);
	        }
	        
	        log.info("===== Exiting the send get");
	 	                
	        return response.toString();
	      
	  }
	 
	  public static String sendPOST(String xml) throws Exception {
		  
		    log.info("===== Inside send post with xml as :"+xml);
		    
		  	if (POST_URL.equals(GetConfigProp.EXCEPTION_MESSAGE)) {
				throw new Exception(GetConfigProp.EXCEPTION_MESSAGE);
			}
		  	
		    HttpPost httpPost = new HttpPost(POST_URL);
	        httpPost.addHeader(USER_AGENT, USER_AGENT_VALUE);
	        httpPost.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);	        
	        		   
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes(ENCODING));
	        httpPost.setEntity(entity);
	 
	        CloseableHttpResponse httpResponse = null;
	        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
	        	httpResponse = httpClient.execute(httpPost);
			} catch (Exception e) {
				httpResponse.close();
				e.printStackTrace();
				log.error("===== Unable to hit the url:"+POST_URL);
				throw new Exception("Unable to hit the url:" + POST_URL);
			}
	        
	        StringBuilder response = new StringBuilder();
	 
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();
				log.error("===== Unable to parse the response from url:"+POST_URL);
				throw new Exception("Unable to parse the response from url:" + POST_URL);
	        } finally{
	        	httpResponse.close();
	        }
	        
	        log.info("===== Exiting the send post");
	 	                
	        return response.toString();
	 
	  }

}
