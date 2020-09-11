package com.fengchao.crm.workbench.service;

import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.ActivityRemark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ActivitySerivice {

    boolean save(Activity a);

    PagintionVO<Activity> pageList(Map<String,Object> map);

    Boolean delete(String[] ids);

    Map<String,Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);
}
