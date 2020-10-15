package com.fengchao.crm.workbench.service;

import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    PagintionVO<Customer> getCustomerList(Map<String,Object> map);

   boolean save(Customer customer);

    List<String> getCustomerName(String name);
}
