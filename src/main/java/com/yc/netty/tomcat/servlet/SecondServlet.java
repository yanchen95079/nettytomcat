package com.yc.netty.tomcat.servlet;

import com.yc.netty.tomcat.http.YCRequest;
import com.yc.netty.tomcat.http.YCResponse;
import com.yc.netty.tomcat.http.YCServlet;

/**
 * @author Yanchen
 * @ClassName SecondServlet
 * @Date 2019/6/17 11:09
 */
public class SecondServlet extends YCServlet {
    @Override
    protected void doGet(YCRequest req, YCResponse res) {
        doPost(req,res);
    }
    @Override
    protected void doPost(YCRequest req, YCResponse res) {
        res.write("second2");
    }



}
