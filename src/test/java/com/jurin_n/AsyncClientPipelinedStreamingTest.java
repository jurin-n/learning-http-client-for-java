package com.jurin_n;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpPipeliningClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;

public class AsyncClientPipelinedStreamingTest {

	//@Test
	@Ignore
	public void test() throws InterruptedException, ExecutionException, IOException {
        CloseableHttpPipeliningClient httpclient = HttpAsyncClients.createPipelining();
        try {
            httpclient.start();

            HttpHost targetHost = new HttpHost("hc.apache.org", 80);
            HttpGet[] resquests = {
                    new HttpGet("/docs/index.html"),
                    new HttpGet("/docs/introduction.html"),
                    new HttpGet("/docs/setup.html"),
                    new HttpGet("/docs/config/index.html")
            };

            List<MyRequestProducer> requestProducers = new ArrayList<MyRequestProducer>();
            List<MyResponseConsumer> responseConsumers = new ArrayList<MyResponseConsumer>();
            for (HttpGet request: resquests) {
                requestProducers.add(new MyRequestProducer(targetHost, request,1));
                responseConsumers.add(new MyResponseConsumer(request));
            }

            Future<List<Boolean>> future = httpclient.execute(
                    targetHost, requestProducers, responseConsumers, null);
            future.get();
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
	}
	
	//@Test
	@Ignore
	public void test2() throws InterruptedException, ExecutionException, IOException {
        CloseableHttpPipeliningClient httpclient = HttpAsyncClients.createPipelining();
        try {
            httpclient.start();

            HttpHost targetHost = new HttpHost("localhost", 9080);
            for(int i = 0; i < 10 ; i++){
            	int tmp = i%2;
            	HttpGet request =  new HttpGet("/team/"+tmp+"/TEST001");
	            Future<Boolean> future = httpclient.execute(
	                      new MyRequestProducer(targetHost, request,i)
	                    , new MyResponseConsumer(request)
	                    , null);
	            future.get();
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
	}
}
