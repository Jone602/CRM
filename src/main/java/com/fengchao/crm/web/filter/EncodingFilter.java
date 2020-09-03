package com.fengchao.crm.web.filter;


import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器！");
        req.setCharacterEncoding("utf-8");
        System.out.println("进入到过滤响应流中文乱码");
        //过滤响应流中文乱码
        resp.setContentType("text/html;charset=utf-8");
        chain.doFilter(req,resp);
    }
}
