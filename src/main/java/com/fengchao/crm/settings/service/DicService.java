package com.fengchao.crm.settings.service;
import com.fengchao.crm.exception.LoginException;
import com.fengchao.crm.settings.domain.DicValue;
import com.fengchao.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public interface DicService {

    Map<String, List<DicValue>> getAll();
}
