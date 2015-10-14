package com.jurin_n;

import java.io.IOException;
import java.nio.CharBuffer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.protocol.HttpContext;

public class MyResponseConsumer extends AsyncCharConsumer<Boolean> {
    private final HttpRequest request;

    MyResponseConsumer(final HttpRequest request) {
        this.request = request;
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) {
        System.out.println();
        System.out.println("Response received: " + response.getStatusLine() + " -> " + this.request.getRequestLine());
        System.out.println("=================================================");
    }

    @Override
    protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
        while (buf.hasRemaining()) {
            System.out.print(buf.get());
        }
    }

    @Override
    protected void releaseResources() {
    }

    @Override
    protected Boolean buildResult(final HttpContext context) {
        System.out.println();
        System.out.println("=================================================");
        System.out.println();
        return Boolean.TRUE;
    }

}
