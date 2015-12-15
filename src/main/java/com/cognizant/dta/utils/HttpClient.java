package com.cognizant.dta.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClient {
	
	  private static final String CONTENT_TYPE = "Content-Type";
	  private static final String CONTENT_TYPE_VALUE = "application/xml";
	  private static final String USER_AGENT = "User-Agent";
	  private static final String USER_AGENT_VALUE = "Mozilla/5.0";
	  private static final String ENCODING = "UTF-8";
	  private static final int SET_MAX_CLIENT_CONNECTION = 200;
	  private static final int MAX_PER_ROUTE = 20;
	  private static final String HOST = "localhost";
	  private static final int MAX_HOST_PER_ROUTE = 20;	  
	  private static final String GET_URL = GetConfigProp.getURL();	  
	  private static final String POST_URL = GetConfigProp.getURL();
	  private static PoolingHttpClientConnectionManager cm = null;	  
	  private static final String PORT = GetConfigProp.getPORT();
	  
	  static {
		    cm.setMaxTotal(SET_MAX_CLIENT_CONNECTION);		    
		    cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
	  }
	  
	  public static String sendGET() throws IOException {
		    
		    
		    HttpHost localhost = new HttpHost(HOST, Integer.parseInt(PORT));
		    cm.setMaxPerRoute(new HttpRoute(localhost), MAX_HOST_PER_ROUTE);
		    
	        HttpGet httpGet = new HttpGet(GET_URL);
	        httpGet.addHeader(USER_AGENT, USER_AGENT_VALUE);
	        
	        CloseableHttpResponse httpResponse = null; 
	        try (CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build()){
	        	httpResponse = httpClient.execute(httpGet);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        StringBuilder response = new StringBuilder();
	   	 
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();			
	        } 
	 	                
	        return response.toString();
	      
	  }
	 
	  public static String sendPOST(String xml) throws Exception {
		  
		  	if (POST_URL.equals(GetConfigProp.EXCEPTION_MESSAGE)) {
				throw new Exception(GetConfigProp.EXCEPTION_MESSAGE);
			}
		    HttpPost httpPost = new HttpPost(POST_URL);
	        httpPost.addHeader(USER_AGENT, USER_AGENT_VALUE);
	        httpPost.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
	        
	        
		    HttpHost localhost = new HttpHost(HOST, Integer.parseInt(PORT));
		    cm.setMaxPerRoute(new HttpRoute(localhost), MAX_HOST_PER_ROUTE);
	        
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes(ENCODING));
	        httpPost.setEntity(entity);
	 
	        CloseableHttpResponse httpResponse = null;
	        try (CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build()){
	        	httpResponse = httpClient.execute(httpPost);
			} catch (Exception e) {
				httpResponse.close();
				e.printStackTrace();
				throw new Exception("Unable to hit the url:"+POST_URL);
			}
	        
	        StringBuilder response = new StringBuilder();
	 
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();	
				throw new Exception("Unable to parse the response from url:"+POST_URL);
	        } finally{
	        	httpResponse.close();
	        }
	 	                
	        return response.toString();
	 
	  }

}
