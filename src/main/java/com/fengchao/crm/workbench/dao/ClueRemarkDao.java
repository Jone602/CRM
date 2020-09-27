package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarkListByAid(String clueId);

    int delete(ClueRemark clueRemark);
}
