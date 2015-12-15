package com.cognizant.dta.main;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import com.cognizant.dta.utils.GetConfigProp;
import com.cognizant.dta.utils.HttpClient;



public class Request {
	
	 static final String PORT = GetConfigProp.getPORT();
	 	 
	 public static void main(String[] args) {
		 
		
		 port(Integer.parseInt(PORT));
		 
		 get("/getUI_Manual", (request, response) -> {
			 	
			 
			 String str = "";
			 
			 InputStream inputStream = Request.class.getResourceAsStream("dta_manual.html");
			 
	            if (inputStream != null) {
	                
	                response.status(200);

	                byte[] buf = new byte[1024];
	                OutputStream os = response.raw().getOutputStream();
	                OutputStreamWriter outWriter = new OutputStreamWriter(os);
	                int count = 0;
	                while ((count = inputStream.read(buf)) >= 0) {
	                    os.write(buf, 0, count);
	                }
	                inputStream.close();
	                outWriter.close();

	            }
				return str; 
		 });
		 
		 
		 get("/getUI_File", (request, response) -> {
			 	
			 
			 String str = "";
			 
			 InputStream inputStream = Request.class.getResourceAsStream("dta_file.html");
			 
	            if (inputStream != null) {
	                
	                response.status(200);

	                byte[] buf = new byte[1024];
	                OutputStream os = response.raw().getOutputStream();
	                OutputStreamWriter outWriter = new OutputStreamWriter(os);
	                int count = 0;
	                while ((count = inputStream.read(buf)) >= 0) {
	                    os.write(buf, 0, count);
	                }
	                inputStream.close();
	                outWriter.close();

	            }
				return str; 
		 });
		 
		 
		 get("/getAGI", (request, response) -> {
			 response.header("Access-Control-Allow-Origin", "*");
			 return HttpClient.sendGET();
	     });
		 
		 
		 get("/getXML", (request, response) -> {
			String body =  request.body();
			System.out.println(body);
			return HttpClient.sendPOST(body);
	     });
	 }

}
