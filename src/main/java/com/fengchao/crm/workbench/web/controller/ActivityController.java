package com.fengchao.crm.workbench.web.controller;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.DateTimeUtil;
import com.fengchao.crm.utils.PrintJson;
import com.fengchao.crm.utils.ServiceFactory;
import com.fengchao.crm.utils.UUIDUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.ActivityRemark;
import com.fengchao.crm.workbench.service.ActivitySerivice;
import com.fengchao.crm.workbench.service.impl.ActivitySeriviceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 市场活动的控制器");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
                System.out.println("执行getUserList()方法");
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            System.out.println("执行save()方法");
            save(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            System.out.println("执行pageList()方法");
            pageList(request,response);
        }else if("/workbench/activity/delete.do".equals(path)){
            System.out.println("执行delete()方法");
            delete(request,response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            System.out.println("执行getUserListAndActivity()方法");
            getUserListAndActivity(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            System.out.println("执行update()方法");
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            System.out.println("执行detail()方法");
            detail(request,response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            System.out.println("执行getRemarkListByAid()方法");
            getRemarkListByAid(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            System.out.println("执行deleteRemark()方法");
            deleteRemark(request,response);
        }

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        String activityId = request.getParameter("activityId");
        List<ActivityRemark> rList = as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response,rList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到详细信息页中");
        String id = request.getParameter("id");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        Activity a = as.detail(id);
        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
        System.out.println("执行完毕");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的更新确认操作！！！");
            String id = request.getParameter("id");
            String owner = request.getParameter("owner");
            String name  = request.getParameter("name");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String cost = request.getParameter("cost");
            String description = request.getParameter("description");
            //更新时间
            String editTime = DateTimeUtil.getSysTime();
            //修改人
            String editBy = ((User)request.getSession().getAttribute("user")).getName();
            Activity a = new Activity();
                a.setId(id);
                a.setOwner(owner);
                a.setName(name);
                a.setCost(cost);
                a.setStartDate(startDate);
                a.setEndDate(endDate);
                a.setDescription(description);
                a.setEditTime(editTime);
                a.setEditBy(editBy);
            ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
            boolean flag = as.update(a);
            PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改更新操作之前的 获取 uList和 Activity");
        String id = request.getParameter("id");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        Map<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");
        String ids[] = request.getParameterValues("id");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        Boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动页面请求操作！！！");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        PagintionVO<Activity> pagintionVO = as.pageList(map);
        PrintJson.printJsonObj(response,pagintionVO);
        System.out.println("获取到了活动信息列表以及查询出来的总条数");
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作！！！");
                String id = UUIDUtil.getUUID();
                String owner = request.getParameter("owner");
                String name  = request.getParameter("name");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String cost = request.getParameter("cost");
                String description = request.getParameter("description");
                String createTime = DateTimeUtil.getSysTime();
                //修改人
                String createBy = ((User)request.getSession().getAttribute("user")).getName();

                Activity a = new Activity();
                a.setId(id);
                a.setOwner(owner);
                a.setName(name);
                a.setCost(cost);
                a.setStartDate(startDate);
                a.setEndDate(endDate);
                a.setDescription(description);
                a.setCreateTime(createTime);
                a.setCreateBy(createBy);
                ActivitySerivice as = (ActivitySerivice)ServiceFactory.getService(new ActivitySeriviceImpl());
                boolean flag = as.save(a);
                PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> slist = us.getUserList();
        PrintJson.printJsonObj(response,slist);
        System.out.println("获取到了用户列表，返回给前端");
    }


}
