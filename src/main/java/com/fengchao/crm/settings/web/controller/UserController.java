package com.fengchao.crm.settings.web.controller;

import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.MD5Util;
import com.fengchao.crm.utils.PrintJson;
import com.fengchao.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 用户控制器");
        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/login.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到了用户验证方法");
        String loginAct = request.getParameter("loginAct");

        System.out.println(loginAct);

        String loginPwd = request.getParameter("loginPwd");

        System.out.println(loginPwd);

        loginPwd = MD5Util.getMD5(loginPwd);
        //获取浏览器的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("============ip"+ip);
        //未来开发业务，统一使用代理类形态的接口对象,传入具体的实现类对象，得到代理类对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            //如果程序能够执行到此处，说明service层没有抛出异常，则需要给前端返回一个true
            // {"success"true}  这个时候需要使用工具类 Jackson来处理json数据
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            //程序执行到这里说明，service层抛出了异常，登陆失败了，需要给前端返回false以及msg错误消息
            //{"success"false,"msg" ?}
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);


        }

    }
}
