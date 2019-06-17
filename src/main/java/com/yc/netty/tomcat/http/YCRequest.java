package com.yc.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Yanchen
 * @ClassName YCRequest
 * @Date 2019/6/17 10:56
 */
public class YCRequest {

    private ChannelHandlerContext chc;
    private HttpRequest httpRequest;

    public YCRequest(ChannelHandlerContext chc, HttpRequest httpRequest) {
        this.chc = chc;
        this.httpRequest = httpRequest;
    }

    public String getMethod() {
        return httpRequest.method().name();
    }

    public String getUrl() {
        return httpRequest.uri();
    }
}
