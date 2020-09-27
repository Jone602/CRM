package com.fengchao.crm.settings.service.impl;

import com.fengchao.crm.settings.dao.DicTypeDao;

import com.fengchao.crm.settings.dao.DicValueDao;
import com.fengchao.crm.settings.domain.DicType;
import com.fengchao.crm.settings.domain.DicValue;
import com.fengchao.crm.settings.service.DicService;

import com.fengchao.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();
        List<DicType> dtList = dicTypeDao.getTypeList();
        //遍历字典类型
        for (DicType dt: dtList) {
            //遍历取得字典类型的code值
            String code = dt.getCode();
            //根据code取查询dicValue表
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code+"List",dvList);
        }
        return map;
    }
}







































