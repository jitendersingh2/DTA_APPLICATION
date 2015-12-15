package com.cognizant.dta.main;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cognizant.dta.utils.GetConfigProp;
import com.cognizant.dta.utils.HttpClient;


public class Request {

	private static final String PORT = GetConfigProp.getPORT();
	private static final String DTA_MANUAL = "dta_manual.html";
	private static final String DTA_FILE = "dta_file.html";
	private static final String CONTENT_TYPE = "Access-Control-Allow-Origin";
	private static final String CONTENT_TYPE_VALUE = "*";
	private static final int STATUS = 200;
	private static final int DEFAULT_PORT = 4567;

	public static void main(String[] args) {


		if (PORT.equals(GetConfigProp.EXCEPTION_MESSAGE)) 
			port(DEFAULT_PORT);
		else
			port(Integer.parseInt(PORT));

		
		
		get("/getUI_Manual", (request, response) -> {


			String str = "";

			try (InputStream inputStream = Request.class.getResourceAsStream(DTA_MANUAL);) {

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
		});


		get("/getUI_File", (request, response) -> {


			String str = "";

			try (InputStream inputStream = Request.class.getResourceAsStream(DTA_FILE);) {

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
		});


		get("/getAGI", (request, response) -> {
			response.header(CONTENT_TYPE, CONTENT_TYPE_VALUE);
			return HttpClient.sendGET();
		});


		get("/getXML", (request, response) -> {
			String body =  request.body();
			return HttpClient.sendPOST(body);
		});
	}

}
