package com.fengchao.crm.workbench.web.controller;

import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.DateTimeUtil;
import com.fengchao.crm.utils.PrintJson;
import com.fengchao.crm.utils.ServiceFactory;
import com.fengchao.crm.utils.UUIDUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Customer;
import com.fengchao.crm.workbench.service.CustomerService;
import com.fengchao.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 客户控制器");
        String path = request.getServletPath();
        System.out.println(path);
        if ("/workbench/customer/getCustomerList.do".equals(path)){
            getCustomerList(request,response);
        }else if ("/workbench/customer/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/customer/save.do".equals(path)){
            save(request,response);
            System.out.println(123);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String description = request.getParameter("description");
        String nextContactTime = request.getParameter("nextContactTime");
        String contactSummary = request.getParameter("contactSummary");
        String address = request.getParameter("address1");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setAddress(address);
        customer.setWebsite(website);
        customer.setPhone(phone);
        customer.setOwner(owner);
        customer.setNextContactTime(nextContactTime);
        customer.setName(name);
        customer.setDescription(description);
        customer.setCreateTime(createTime);
        customer.setCreateBy(createBy);
        customer.setContactSummary(contactSummary);
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        boolean flag = cs.save(customer);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

    private void getCustomerList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取客户信息列表");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.parseInt(pageNoStr);
        String  pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的数据条数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        PagintionVO<Customer> pagintionVO = cs.getCustomerList(map);
        PrintJson.printJsonObj(response,pagintionVO);

    }
}
