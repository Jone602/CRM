package com.fengchao.crm.workbench.web.controller;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.*;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.ActivityRemark;
import com.fengchao.crm.workbench.service.ActivitySerivice;
import com.fengchao.crm.workbench.service.ClueService;
import com.fengchao.crm.workbench.service.impl.ActivitySeriviceImpl;
import com.fengchao.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到了 线索的控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
                System.out.println("执行clue的getUserList()方法");
            getUserList(request,response);
        }

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
