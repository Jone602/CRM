package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    int getTotalByCoundition(Map<String,Object> map);

    List<Clue> getClueListByCoundition(Map<String,Object> map);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
