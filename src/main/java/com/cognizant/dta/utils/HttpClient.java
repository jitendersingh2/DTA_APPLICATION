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
		  
	  private static final String USER_AGENT = "Mozilla/5.0";
	  
	  private static final String GET_URL = GetConfigProp.getURL();
	  
	  private static final String POST_URL = GetConfigProp.getURL();
	  	  
	  public static String sendGET() throws IOException {
		    
		    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();		    
		    cm.setMaxTotal(200);		    
		    cm.setDefaultMaxPerRoute(20);
		    
		    HttpHost localhost = new HttpHost("locahost", 80);
		    cm.setMaxPerRoute(new HttpRoute(localhost), 50);
	        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();;
	        HttpGet httpGet = new HttpGet(GET_URL);
	        httpGet.addHeader("User-Agent", USER_AGENT);
	        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                httpResponse.getEntity().getContent()));
	        
	        StringBuffer response = new StringBuffer();
	 
	        System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());
	 
	        try {
	        	String inputLine;
		       
		        while ((inputLine = reader.readLine()) != null) {
		            response.append(inputLine);
		        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();
			
	        } finally {
	        	reader.close();
	        	httpClient.close();
			}
	        
	        return response.toString();
	  }
	 
	    public static String sendPOST(String xml) throws IOException {
	 
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(POST_URL);
	        httpPost.addHeader("User-Agent", USER_AGENT);
	        httpPost.setHeader("Content-Type", "application/xml");
	        
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
	        httpPost.setEntity(entity);
	 
	        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
	        
	        StringBuffer response = new StringBuffer();
	 
	        System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());
	 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
	        
	        try {
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (IOException e) {
				e.printStackTrace();
			
	        } finally {
	        	reader.close();
	        	httpClient.close();
			}
	 	                
	        return response.toString();
	 
	    }

}
