package com.fengchao.crm.settings.service;
import com.fengchao.crm.exception.LoginException;
import com.fengchao.crm.settings.domain.User;

import java.util.List;

/**
 * Author 北京动力节点
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();

}
