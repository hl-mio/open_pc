package com.example.demo;

import com.example.demo.bll.rec.TcpSocketServer;
import com.example.demo.bll.send.SendOpenMsgServer;
import com.example.demo.bll.send.SendShutdownMsgServer;
import lombok.extern.java.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Log
@SpringBootApplication
@MapperScan("com.example.demo.dao")
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        // 启动接收消息的线程
        TcpSocketServer tcpSocketServer = context.getBean(TcpSocketServer.class);
        tcpSocketServer.start();

        // 启动回复开机消息的线程
        SendOpenMsgServer sendOpenMsgServer = context.getBean(SendOpenMsgServer.class);
        sendOpenMsgServer.start();

        // 启动回复关机消息的线程
        SendShutdownMsgServer sendShutdownMsgServer = context.getBean(SendShutdownMsgServer.class);
        sendShutdownMsgServer.start();

    }

}
