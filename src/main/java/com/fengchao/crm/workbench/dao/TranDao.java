package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    int getTotal(Map<String,Object> map);

    List<Tran> getTranList(Map<String,Object> map);
}
