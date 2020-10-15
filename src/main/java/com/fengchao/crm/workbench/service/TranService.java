package com.fengchao.crm.workbench.service;

import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.Contacts;
import com.fengchao.crm.workbench.domain.Customer;
import com.fengchao.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    List<Activity> getActivityListByName(String aname);

    List<Contacts> getContactsListByName(String cname);

    PagintionVO<Tran> getTranList(Map<String,Object> map);
}
