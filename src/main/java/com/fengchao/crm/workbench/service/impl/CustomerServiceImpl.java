package com.fengchao.crm.workbench.service.impl;



import com.fengchao.crm.utils.SqlSessionUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.dao.CustomerDao;
import com.fengchao.crm.workbench.dao.CustomerRemarkDao;
import com.fengchao.crm.workbench.domain.Customer;
import com.fengchao.crm.workbench.service.CustomerService;

import java.util.List;
import java.util.Map;


public class CustomerServiceImpl implements CustomerService{
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    public PagintionVO<Customer> getCustomerList(Map<String, Object> map) {
        int total = customerDao.getTotalByCoundition(map);
        List<Customer> cList = customerDao.getCustomerListByCoundition(map);
        PagintionVO<Customer> pagintionVO = new PagintionVO<Customer>();
        pagintionVO.setTotal(total);
        pagintionVO.setDataList(cList);
        return pagintionVO;
    }

    public boolean save(Customer customer) {
        int count = customerDao.save(customer);
        boolean flag = true;
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public List<String> getCustomerName(String name) {
        List<String> slist = customerDao.getCustomerName(name);
        return slist;
    }
}



