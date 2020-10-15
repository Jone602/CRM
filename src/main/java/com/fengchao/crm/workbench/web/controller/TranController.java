package com.fengchao.crm.workbench.web.controller;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.PrintJson;
import com.fengchao.crm.utils.ServiceFactory;
import com.fengchao.crm.utils.SqlSessionUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.Contacts;
import com.fengchao.crm.workbench.domain.Tran;
import com.fengchao.crm.workbench.service.CustomerService;
import com.fengchao.crm.workbench.service.TranService;
import com.fengchao.crm.workbench.service.impl.CustomerServiceImpl;
import com.fengchao.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 交易控制器");
        String path = request.getServletPath();
        System.out.println(path);
        if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if ("/workbench/transaction/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if ("/workbench/transaction/getContactsListByName.do".equals(path)){
            getContactsListByName(request,response);
        }else if ("/workbench/transaction/getTranList.do".equals(path)){
            getTranList(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过模糊查询，获取用户姓名列表");
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);
    }

    private void getTranList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到请求交易列表操作");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customer = request.getParameter("customer");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contacts = request.getParameter("contacts");
        String pageNoStr  = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("customer",customer);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contacts",contacts);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PagintionVO<Tran> pagintionVO = ts.getTranList(map);
        PrintJson.printJsonObj(response,pagintionVO);
    }

    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {
        String cname = request.getParameter("cname");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Contacts> cList = ts.getContactsListByName(cname);
        PrintJson.printJsonObj(response,cList);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到搜索活动列表的操作");
        String aname = request.getParameter("aname");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Activity> aList = ts.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转交易添加页的操作");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }


}
