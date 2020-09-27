package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
