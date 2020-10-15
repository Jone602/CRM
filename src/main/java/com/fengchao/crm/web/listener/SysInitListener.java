package com.fengchao.crm.web.listener;
import com.fengchao.crm.settings.domain.DicValue;
import com.fengchao.crm.settings.service.DicService;
import com.fengchao.crm.settings.service.impl.DicServiceImpl;
import com.fengchao.crm.utils.ServiceFactory;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SysInitListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
            System.out.println("上下文监听器");
            //这个event就指的是 ServletContext的上下文域对象
            ServletContext applivation = event.getServletContext();
            DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
            //获取数据字典
            Map<String, List<DicValue>> map = ds.getAll();
            //将map中的键值对，解析为域对象中解析的键值对
            Set<String> set = map.keySet();
            for (String key:set){
                applivation.setAttribute(key,map.get(key));
            }
        System.out.println("服务器处理数字字典结束");
            //数据字典处理完毕后，需要处理以下文件

    }


}
