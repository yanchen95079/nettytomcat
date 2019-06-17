package com.yc.netty.tomcat.http;

/**
 * @author Yanchen
 * @ClassName YCServlet
 * @Date 2019/6/17 10:56
 */
public abstract class YCServlet {
    public void service(YCRequest req,YCResponse res) throws Exception{
        if("GET".equalsIgnoreCase(req.getMethod())){
            doGet(req,res);
        }else {
            doPost(req,res);
        }
    }
    protected abstract void doGet(YCRequest req, YCResponse res);
    protected abstract void doPost(YCRequest req, YCResponse res);



}
