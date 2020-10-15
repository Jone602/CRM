package com.fengchao.crm.settings.service.impl;
import com.fengchao.crm.exception.LoginException;
import com.fengchao.crm.settings.dao.UserDao;
import com.fengchao.crm.settings.domain.User;
import com.fengchao.crm.settings.service.UserService;
import com.fengchao.crm.utils.DateTimeUtil;
import com.fengchao.crm.utils.SqlSessionUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userDao.login(map);
        if (user == null) {
            throw new LoginException("账号密码错误");
        }
        //如果代码能成功的执行到该行，说明账号密码正确
        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw  new LoginException("登陆期限已失效");
        }
        //判断锁定状态
        if (user.getLockState().equals("0")){
            throw new LoginException("账号已被锁定，请联系管理员");
        }
        //判断IP地址是否合法
        String ips = ip;
        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("登陆IP不合法");
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> ulist = userDao.getUserList();
        return ulist;
    }
}







































