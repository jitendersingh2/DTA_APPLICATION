package com.cognizant.dta.main;

import static spark.Spark.*;
import spark.*;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cognizant.dta.utils.GetConfigProp;
import com.cognizant.dta.utils.HttpClient;


public class RequestURL {

	private static final String PORT = GetConfigProp.getPORT();
	private static final String DTA_MANUAL = "dta_manual.html";
	private static final String DTA_FILE = "dta_file.html";
	private static final String CONTENT_TYPE = "Access-Control-Allow-Origin";
	private static final String CONTENT_TYPE_VALUE = "*";
	private static final int STATUS = 200;
	private static final int DEFAULT_PORT = 4567;
	private static final String str = "";

	public static void main(String[] args) {


		if (PORT.equals(GetConfigProp.EXCEPTION_MESSAGE)) 
			setPort(DEFAULT_PORT);
		else
			setPort(Integer.parseInt(PORT));
		
		get(new Route("/getUI_Manual") {
				@Override
	            public Object handle(Request request, Response response) {

	    			try (InputStream inputStream = RequestURL.class.getResourceAsStream(DTA_MANUAL);) {

	    				if (inputStream != null) {

	    					response.status(STATUS);

	    					byte[] buf = new byte[1024];
	    					OutputStream os = response.raw().getOutputStream();
	    					int count = 0;
	    					while ((count = inputStream.read(buf)) >= 0) {
	    						os.write(buf, 0, count);
	    					}

	    				}
	    			} catch (IOException e)  {
	    				e.printStackTrace();
	    			}
	    			return str; 
	            }

				
	    });

		get(new Route("/getUI_File") {
			@Override
            public Object handle(Request request, Response response) {
            	
    			try (InputStream inputStream = RequestURL.class.getResourceAsStream(DTA_FILE);) {

    				if (inputStream != null) {

    					response.status(STATUS);

    					byte[] buf = new byte[1024];
    					OutputStream os = response.raw().getOutputStream();
    					int count = 0;
    					while ((count = inputStream.read(buf)) >= 0) {
    						os.write(buf, 0, count);
    					}

    				}
    			} catch (IOException e)  {
    				e.printStackTrace();
    			}
    			return str; 
            }

		});
		
		get(new Route("/getAGI") {
			@Override
            public Object handle(Request request, Response response) {
            	
    			try {
    				response.header(CONTENT_TYPE, CONTENT_TYPE_VALUE);
					return HttpClient.sendGET();
				} catch (IOException e) {
					e.printStackTrace();
				}
    			return str;
            }

        });

		get(new Route("/getXML") {
			@Override
            public Object handle(Request request, Response response) {
            	String body =  request.body();
    			try {
					return HttpClient.sendPOST(body);
				} catch (Exception e) {
					e.printStackTrace();
				}
    			return str;
            }

        });

	}

}

