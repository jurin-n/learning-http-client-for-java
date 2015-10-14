package com.jurin_n;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;

public class MyRequestProducer extends BasicAsyncRequestProducer  {
    private final HttpRequest request;
    private final int count;
    
    MyRequestProducer(final HttpHost target, final HttpRequest request, final int count) {
        super(target, request);
        this.request = request;
        this.count = count;
    }

    @Override
    public void requestCompleted(final HttpContext context) {
        super.requestCompleted(context);
        System.out.println();
        System.out.println("Request sent ["+count+"]: " + this.request.getRequestLine());
        System.out.println("=================================================");
    }
}
