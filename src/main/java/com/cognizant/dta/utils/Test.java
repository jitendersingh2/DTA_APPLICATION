package com.cognizant.dta.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	
	private static final String USER_AGENT = "User-Agent";
	private static final String USER_AGENT_VALUE = "Mozilla/5.0";
	private static final String ENCODING = "UTF-8";	  

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CONTENT_TYPE_VALUE = "application/vnd.ccadllc.dta-application-gateway+xml";
	private static final String POST_URL = GetConfigProp.getURL();
	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	  

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] certificate, String type) {
                return true;
            }
        };
	  
	    KeyStore clientStore=null;
	    try {
		    clientStore = KeyStore.getInstance("PKCS12");
	    } catch (KeyStoreException e) {
		// TODO Auto-generated catch block
		    e.printStackTrace();
	    }
	    InputStream p12 = HttpUtil.class.getResourceAsStream("client.p12");
      
	    clientStore.load(p12, "cognizant".toCharArray());
	
        
	    
	    SSLContext sslContext = SSLContexts.custom()
//                .loadKeyMaterial(clientStore,"cognizant".toCharArray())
		            .loadTrustMaterial(acceptingTrustStrategy)
		            .build();
	    
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"}, // Allow TLSv1 protocol only
                null,
                new DTAHostVerifier());
	    
	    HttpPost httpPost = new HttpPost("https://192.168.102.80:8443/dta-application-gateway/agm");
        httpPost.addHeader(USER_AGENT, USER_AGENT_VALUE);
        httpPost.setHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);	        
        		   
        HttpEntity entity = new ByteArrayEntity("<xml/>".getBytes(ENCODING));
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
 	                
        //return response.toString();
 
  }

}


