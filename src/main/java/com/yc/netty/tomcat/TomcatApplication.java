package com.yc.netty.tomcat;

import com.yc.netty.tomcat.http.YCServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TomcatApplication {

    private int port = 8080;

    private Map<String,YCServlet> servletMapping = new ConcurrentHashMap<String, YCServlet>();

    private Properties webxml = new Properties();

    private void init(){
        //类似spring handermapper

        try {
            String path=this.getClass().getClassLoader().getResource("web.properties").getPath();
            String decode = URLDecoder.decode(path, "utf-8");
            FileInputStream fis = new FileInputStream(decode);
            webxml.load(fis);
            for (Object o : webxml.keySet()) {
                String key=String.valueOf(o);
                if(key.endsWith(".url")){
                    String url=webxml.getProperty(key);
                    String servletName = key.replaceAll("\\.url$", "");
                    String  className=webxml.getProperty(servletName+".className");
                    YCServlet ycServlet = (YCServlet)Class.forName(className).newInstance();
                    servletMapping.put(url, ycServlet);
                }
            }        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void start(){

            init();
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap=new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new YCTomcatHandler(servletMapping));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            // 启动服务器
            ChannelFuture f = serverBootstrap.bind(port).sync();
            System.out.println("YC Tomcat 已启动，监听的端口是：" + port);
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
            new TomcatApplication().start();
    }

}
