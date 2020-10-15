package com.fengchao.crm.workbench.web.controller;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.settings.service.impl.UserServiceImpl;
import com.fengchao.crm.utils.*;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.*;
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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到了 线索的控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){

            save(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)){

            pageList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){

            detail(request,response);
        }else if ("/workbench/clue/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbund.do".equals(path)){

            unbund(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)){

            bund(request,response);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            System.out.println("=============convert.do===================");
            convert(request,response);
        }

    }
    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到传统请求的线索转换操作");
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        Tran t = null;
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)){
            t = new Tran();
            //接收从前端传过来的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate =  request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setId(id);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,t,createBy);
        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }
    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String aname = request.getParameter("aname");
        List<Activity> aList = cs.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过输入的name模糊查询出市场活动");
        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");
        Map<String,String> map = new HashMap<String, String>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除活动于线索的关联关系");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行通过clue的ID获取 活动列表");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        String clueId = request.getParameter("clueId");
        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索详细信息的获取备注方法");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String clueId = request.getParameter("clueId");
        List<ClueRemark> dataList = cs.getRemarkListByAid(clueId);
        PrintJson.printJsonObj(response,dataList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("执行线索详细信息的方法中了，需要请求转发的方式发回给前端");
        //接受前端返回的id参数
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
        System.out.println("请求完毕！！！");
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String fullname=request.getParameter("fullname");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String phone=request.getParameter("phone");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PagintionVO<Clue> pagintionVO = cs.pageList(map);
        PrintJson.printJsonObj(response,pagintionVO);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id=UUIDUtil.getUUID();
        String fullname=request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");
        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean falg = cs.sava(c);
        PrintJson.printJsonFlag(response,falg);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
