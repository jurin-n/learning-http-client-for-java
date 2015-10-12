package com.jurin_n;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpException;
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

public class HttpAsyncClientTest {

	@Test
	public void testQuickStartSample1() throws InterruptedException, ExecutionException, IOException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		
		//Start the client.
		httpclient.start();
		
		/*
		 * Example 1
		 * */
		//Execute request.
		final HttpGet request1 = new HttpGet("http://www.yahoo.co.jp/");
		Future<HttpResponse> future = httpclient.execute(request1, null);
		
		//and wait until a response is received.
		HttpResponse response1 = future.get();
		System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());

		httpclient.close();
	}

	
	@Test
	public void testQuickStartSample2() throws InterruptedException, IOException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

		//Start the client.
		httpclient.start();
		
		/*
		 * Example 2
		 * */
		//One most likely would want to use a callback for operation result.
		final CountDownLatch latch = new CountDownLatch(1);
		final HttpGet request = new HttpGet("http://www.yahoo.co.jp/");
		httpclient.execute(request, new FutureCallback<HttpResponse>(){
			public void completed(final HttpResponse response){
				latch.countDown();
				System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
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
		latch.await();
		httpclient.close();
	}
	
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
		final CountDownLatch latch = new CountDownLatch(1);
		final HttpGet request = new HttpGet("http://www.yahoo.co.jp/");
		HttpAsyncRequestProducer producer = HttpAsyncMethods.create(request);
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
		latch.await();
		httpclient.close();
	}
}
