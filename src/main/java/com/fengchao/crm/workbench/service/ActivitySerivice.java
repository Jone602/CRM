package com.fengchao.crm.workbench.service;

import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;

import java.util.HashMap;
import java.util.Map;

public interface ActivitySerivice {

    boolean save(Activity a);

    PagintionVO<Activity> pageList(Map<String,Object> map);

    Boolean delete(String[] ids);

    Map<String,Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detal(String id);
}
