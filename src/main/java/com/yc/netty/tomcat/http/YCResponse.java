package com.yc.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.UnsupportedEncodingException;

/**
 * @author Yanchen
 * @ClassName YCResponse
 * @Date 2019/6/17 10:56
 */
public class YCResponse {
    private ChannelHandlerContext chc;

    private HttpRequest httpRequest;

    public YCResponse(ChannelHandlerContext chc, HttpRequest httpRequest) {
        this.chc = chc;
        this.httpRequest = httpRequest;
    }

    public void write(String out) {
        try {
            if (out == null || out.length() == 0) {
                return;
            }
            FullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html;");
            chc.write(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            chc.flush();
            chc.close();
        }
    }
}
