package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

//    int save(Customer cus);

    int getTotalByCoundition(Map<String,Object> map);

    List<Customer> getCustomerListByCoundition(Map<String,Object> map);

    int save(Customer cus);

    List<String> getCustomerName(String name);
}
