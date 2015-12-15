package com.cognizant.jitender.DummyServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class HttpClient {
	
	 
	  //GetConfigProp getConfigProp = new GetConfigProp();
	  private static final String USER_AGENT = "Mozilla/5.0";
	  
	  private static final String GET_URL = GetConfigProp.getURL();
	  
	  private static final String POST_URL = GetConfigProp.getURL();
	  	  
	  public static String sendGET() throws IOException {
		    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		    // Increase max total connection to 200
		    cm.setMaxTotal(200);
		    // Increase default max connection per route to 20
		    cm.setDefaultMaxPerRoute(20);
		    // Increase max connections for localhost:80 to 50
		    HttpHost localhost = new HttpHost("locahost", 80);
		    cm.setMaxPerRoute(new HttpRoute(localhost), 50);
	        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();;
	        HttpGet httpGet = new HttpGet(GET_URL);
	        httpGet.addHeader("User-Agent", USER_AGENT);
	        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                httpResponse.getEntity().getContent()));
	        
	        StringBuffer response = new StringBuffer();
	 
	        System.out.println("GET Response Status:: "
	                + httpResponse.getStatusLine().getStatusCode());
	 
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
	        // print result
	        System.out.println(response.toString());
	        
	        return response.toString();
	  }
	 
	    static String sendPOST(String xml) throws IOException {
	 
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(POST_URL);
	        httpPost.addHeader("User-Agent", USER_AGENT);
	        httpPost.setHeader("Content-Type", "application/xml");
	        
	        HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
	        httpPost.setEntity(entity);
	 
	        /*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	        urlParameters.add(new BasicNameValuePair("userName", "Pankaj Kumar"));
	 
	        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
	        httpPost.setEntity(postParams);*/
	 
	        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
	 
	        System.out.println("POST Response Status:: "
	                + httpResponse.getStatusLine().getStatusCode());
	 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                httpResponse.getEntity().getContent()));
	 
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	 
	        while ((inputLine = reader.readLine()) != null) {
	            response.append(inputLine);
	        }
	        reader.close();
	 
	        // print result
	        System.out.println(response.toString());
	        httpClient.close();
	        return response.toString();
	 
	    }

}
