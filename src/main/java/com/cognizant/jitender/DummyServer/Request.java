package com.cognizant.jitender.DummyServer;

import static spark.Spark.get;
import static spark.Spark.setPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;



public class Request {
	
	 static final String PORT = GetConfigProp.getPORT();
	 
	 //setPort(1234);
	 public static void main(String[] args) {
		 
		 setPort(Integer.parseInt(PORT));
		 get("/getUI", (request, response) -> {
			 	
			 
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
		 
		 get("/getAGI", (request, response) -> {
			 //HttpClient httpClient = new HttpClient();
			 response.header("Access-Control-Allow-Origin", "*");
			 return HttpClient.sendGET();
			 
		 });
	 }

}
