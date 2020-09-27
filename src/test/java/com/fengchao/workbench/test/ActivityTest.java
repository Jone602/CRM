package com.fengchao.workbench.test;

import com.fengchao.crm.utils.ServiceFactory;
import com.fengchao.crm.utils.UUIDUtil;
import com.fengchao.crm.workbench.domain.Activity;
import com.fengchao.crm.workbench.service.ActivitySerivice;
import com.fengchao.crm.workbench.service.impl.ActivitySeriviceImpl;
import org.junit.Test;

public class ActivityTest {
    @Test
    public void testSave(){
        //System.out.println(123);
        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("推广会");
        ActivitySerivice as = (ActivitySerivice) ServiceFactory.getService(new ActivitySeriviceImpl());
        Boolean flag = as.save(a);
        System.out.println(flag);
    }

}
