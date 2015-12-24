package com.cognizant.dta.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	
	  private static final String CONTENT_TYPE = "Content-Type";
	  private static final String CONTENT_TYPE_VALUE = "application/vnd.ccadllc.dta-application-gateway+xml";
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
		  
		  
//		  	KeyStore clientStore = KeyStore.getInstance("PKCS12");
//		  	InputStream p12 = HttpUtil.class.getResourceAsStream("client.p12");
//	        clientStore.load(p12, "cognizant".toCharArray());
//
//	        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//	        kmf.init(clientStore, "cognizant".toCharArray());
//	        KeyManager[] kms = kmf.getKeyManagers();
//
//	        KeyStore trustStore = KeyStore.getInstance("JKS");
//	        InputStream jks = HttpUtil.class.getResourceAsStream("client.jks");
//	        trustStore.load(jks, "keep challenging".toCharArray());
//
//	        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//	        tmf.init(trustStore);
//	        TrustManager[] tms = tmf.getTrustManagers();
//
//	        SSLContext sslContext = SSLContext.getInstance("TLS");
//	        sslContext.init(kms, tms, new SecureRandom());
		  	
		  TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
	            public boolean isTrusted(X509Certificate[] certificate, String type) {
	                return true;
	            }
	      };
		  
		  KeyStore clientStore = KeyStore.getInstance("PKCS12");
		  InputStream p12 = HttpUtil.class.getResourceAsStream("client.p12");
	      clientStore.load(p12, "cognizant".toCharArray());
	        
		  SSLContext sslContext = SSLContexts.custom()
//	                .loadKeyMaterial(clientStore,"cognizant".toCharArray())
	                .loadTrustMaterial(acceptingTrustStrategy)
	                .build();
		  
//		  String[] protocols = sslContext.getSupportedSSLParameters().getProtocols();
//          List<String> newProtocolList = new ArrayList<String>();
//          for (String protocol : protocols) {
//              if( !protocol.equalsIgnoreCase("SSLv2Hello")){
//                  newProtocolList.add( protocol );
//              }
//          }
//          String[] newProtocolArray = new String[newProtocolList.size()];
//          newProtocolArray = newProtocolList.toArray(newProtocolArray);                                
//          sslContext.getSupportedSSLParameters().setProtocols(newProtocolArray);
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslContext,
	                new String[]{"TLSv1"}, // Allow TLSv1 protocol only
	                null,
	                new DTAHostVerifier());
		  
//		  SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
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
	        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build()){
	        //try (CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new DTAHostVerifier()).build()){
	        	httpResponse = httpClient.execute(httpPost);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("===== Unable to hit the url:"+POST_URL+" with exception:"+e.getMessage());
				throw new Exception("Unable to hit the url:" + POST_URL);
			}
	        log.info("===== status :"+httpResponse.toString());
	        StringBuilder response = new StringBuilder();
	 
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
	        	String inputLine;
	 	         	 
	 	        while ((inputLine = reader.readLine()) != null) {
	 	            response.append(inputLine);
	 	        }
	        	
	        } catch (Exception e) {
				e.printStackTrace();
				log.error("===== Unable to parse the response from url:"+POST_URL+" exception"+e);
				throw new Exception("Unable to parse the response from url:" + POST_URL);
	        } finally{
	        	httpResponse.close();
	        }
	        
	        log.info("===== Exiting the send post");
	 	                
	        return response.toString();
	 
	  }

}
