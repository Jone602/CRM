package com.fengchao.crm.workbench.web.controller;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.PrintJson;
import com.fengchao.crm.utils.ServiceFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 市场活动的控制器");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
                System.out.println("执行getUserList()方法");
            getUserList(request,response);
        }else if("/workbench/activity/***.do".equals(path)){
            System.out.println("执行***()方法");
        }

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> slist = us.getUserList();
        PrintJson.printJsonObj(response,slist);
        System.out.println("获取到了用户列表，返回给前端");
    }


}
