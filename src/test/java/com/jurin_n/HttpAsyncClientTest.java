package com.jurin_n;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

public class HttpAsyncClientTest {

	@Test
	public void testQuickStartSample() {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		
		try{
			//Start the client.
			httpclient.start();
			
			//Execute request.
			final HttpGet request1 = new HttpGet("http://www.yahoo.co.jp/x");
			Future<HttpResponse> future = httpclient.execute(request1, null);
			
			//and wait until a response is received.
			HttpResponse response1 = future.get();
			System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
	}

}
