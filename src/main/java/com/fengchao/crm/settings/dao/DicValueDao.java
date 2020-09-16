package com.fengchao.crm.settings.dao;

import com.fengchao.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
