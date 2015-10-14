package com.jurin_n;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

public class FutureCallbackTest {

	@Test
	public void testQuickStartSample3() throws InterruptedException, IOException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		
		//Start the client.
		httpclient.start();
		
		/*
		 * Example 3
		 * */
		// In real world one most likely would also want to stream
		// request and response body content.

		AsyncCharConsumer<HttpResponse> consumer = new AsyncCharConsumer<HttpResponse>(){

			HttpResponse response;
			
			@Override
			protected void onCharReceived(CharBuffer arg0, IOControl arg1) throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected HttpResponse buildResult(final HttpContext context) throws Exception {
				return this.response;
			}

			@Override
			protected void onResponseReceived(final HttpResponse response) throws HttpException, IOException {
				this.response = response;
			}
		};
		HttpHost targetHost = new HttpHost("localhost", 9080);
		final CountDownLatch latch = new CountDownLatch(10);
		for(int i = 1; i <= 10 ; i++){
			//int tmp = i%2;
			int tmp = 0;
			if(i<=5){
				tmp = 0;
			}else{
				tmp = 1;
			}
			//final HttpGet request = new HttpGet("http://localhost:9080/team/"+tmp+"/TEST001");
			final HttpGet request = new HttpGet("/team/"+tmp+"/TEST00" + i );
			//HttpAsyncRequestProducer producer = HttpAsyncMethods.create(request);
			HttpAsyncRequestProducer producer = new MyRequestProducer(targetHost, request,i);
			httpclient.execute(producer, consumer,  new FutureCallback<HttpResponse>(){
				public void completed(final HttpResponse response3){
					latch.countDown();
					System.out.println(request.getRequestLine() + "->" + response3.getStatusLine());
				}
				
				public void failed(final Exception ex){
					latch.countDown();
					System.out.println(request.getRequestLine() + "->" + ex);
				}
				
				public void cancelled(){
					latch.countDown();
					System.out.println(request.getRequestLine() + " cancelled");
				}
			});
		}
		latch.await();
		httpclient.close();
	}

}
