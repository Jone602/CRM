package com.fengchao.crm.workbench.dao;

import com.fengchao.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactsListByName(String cname);
}
