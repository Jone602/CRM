package com.fengchao.crm.workbench.service;

import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.*;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean sava(Clue c);

    PagintionVO<Clue> pageList(Map<String,Object> map);

    Clue detail(String id);

    List<ClueRemark> getRemarkListByAid(String clueId);

    Boolean unbund(String id);

    Boolean bund(String cid, String[] aids);

    List<Activity> getActivityListByName(String aname);


    boolean convert(String clueId, Tran t, String createBy);
}
