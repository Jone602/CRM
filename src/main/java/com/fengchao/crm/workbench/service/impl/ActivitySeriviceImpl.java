package com.fengchao.crm.workbench.service.impl;

import com.fengchao.crm.settings.dao.UserDao;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.utils.SqlSessionUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.dao.ActivityDao;
import com.fengchao.crm.workbench.dao.ActivityRemarkDao;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.ActivityRemark;
import com.fengchao.crm.workbench.service.ActivitySerivice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivitySeriviceImpl implements ActivitySerivice {
    private ActivityDao  activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao .class);
    private ActivityRemarkDao  activityRemarkDao = (ActivityRemarkDao) SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao .class);
    public boolean save(Activity a) {
        boolean flag = true;
        int count = activityDao.save(a);
        if (count != 1) {
            flag = false;
        }
            return flag;

    }

    public PagintionVO<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total = activityDao.getTotalByCoundition(map);
        //取得活动列表集合
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        //将total和datalist封装到 VO中
        PagintionVO<Activity> vo = new PagintionVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //返回 vo
        return vo;
    }

    public Boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回收到影响的条数
        int count2 = activityRemarkDao.delectByAids(ids);
        if (count1!=count2){

            flag = false;
        }
    //删除市场活动
        int count3 = activityDao.delete(ids);

        return flag;
    }
    //修改市场活动
    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList
        List<User> uList = userDao.getUserList();
        //取activity
        Activity a = activityDao.getById(id);
        //将uList和activity打包进map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("a",a);
        //返回
        return map;
    }

    public boolean update(Activity a) {
        boolean flag = true;
        int count = activityDao.update(a);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> sList = activityRemarkDao.getRemarkListByAid(activityId);
        return sList;
    }

    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count!=1){
            flag = false;
        }
        return flag;
    }
}
