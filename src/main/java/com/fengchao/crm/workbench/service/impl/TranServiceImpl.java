package com.fengchao.crm.workbench.service.impl;

import com.fengchao.crm.utils.SqlSessionUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.dao.ActivityDao;
import com.fengchao.crm.workbench.dao.ContactsDao;
import com.fengchao.crm.workbench.dao.TranDao;
import com.fengchao.crm.workbench.dao.TranHistoryDao;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.domain.Contacts;
import com.fengchao.crm.workbench.domain.Tran;
import com.fengchao.crm.workbench.service.TranService;
import sun.security.krb5.internal.PAData;

import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = activityDao.getActivityListByName(aname);
        return aList;
    }

    public List<Contacts> getContactsListByName(String cname) {
        List<Contacts> cList = contactsDao.getContactsListByName(cname);
        return cList;
    }

    public PagintionVO<Tran> getTranList(Map<String, Object> map) {
       int total = tranDao.getTotal(map);
       List<Tran> tList = tranDao.getTranList(map);
       PagintionVO<Tran> pagintionVO = new PagintionVO<Tran>();
        pagintionVO.setTotal(total);
        pagintionVO.setDataList(tList);
        return pagintionVO;
    }



}



