package com.yc.netty.tomcat;

import com.yc.netty.tomcat.http.YCRequest;
import com.yc.netty.tomcat.http.YCResponse;
import com.yc.netty.tomcat.http.YCServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yanchen
 * @ClassName YCTomcatHandler
 * @Date 2019/6/17 11:30
 */
public class YCTomcatHandler extends ChannelInboundHandlerAdapter {
    private Map<String,YCServlet> servletMapping=new ConcurrentHashMap<String, YCServlet>();
    public YCTomcatHandler(Map<String,YCServlet> servletMapping) {
        this.servletMapping=servletMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;

            // 转交给我们自己的request实现
            YCRequest request = new YCRequest(ctx,req);
            // 转交给我们自己的response实现
            YCResponse response = new YCResponse(ctx,req);
            // 实际业务处理
            String url = request.getUrl();

            if(servletMapping.containsKey(url)){
                servletMapping.get(url).service(request, response);
            }else{
                response.write("404 - Not Found");
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
