package com.fengchao.crm.workbench.dao;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int delectByAids(String[] ids);
}
