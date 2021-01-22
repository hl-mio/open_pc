package com.example.demo.common.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

//@DependsOn(value= {"applicationContextUtil","bean加载测试类"})
@Configuration
public class _优先加载某些bean {

    @Autowired
    ApplicationContextUtil applicationContextUtil;


    public _优先加载某些bean(){
//        System.out.println("** 优先加载某些bean");
    }

}