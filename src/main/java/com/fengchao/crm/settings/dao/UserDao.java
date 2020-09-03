package com.fengchao.crm.settings.dao;

import com.fengchao.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {


    User login(Map<String,String> map);
}
